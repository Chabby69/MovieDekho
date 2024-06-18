package com.moviedekho.userservie.serviceimpl;

import com.moviedekho.userservie.config.MovieServiceClient;
import com.moviedekho.userservie.config.PaymentServiceClient;
import com.moviedekho.userservie.entity.FavoriteMovieEntity;
import com.moviedekho.userservie.entity.RoleEntity;
import com.moviedekho.userservie.entity.UserEntity;
import com.moviedekho.userservie.enums.RoleName;
import com.moviedekho.userservie.enums.SubscriptionPlan;
import com.moviedekho.userservie.exception.UserAlreadyExistsException;
import com.moviedekho.userservie.model.request.FavoriteMovieRequest;
import com.moviedekho.userservie.model.request.PaymentRequest;
import com.moviedekho.userservie.model.request.UserRequest;
import com.moviedekho.userservie.model.response.FavoriteMovieResponse;
import com.moviedekho.userservie.model.response.MovieDocument;
import com.moviedekho.userservie.model.response.UserLoginResponse;
import com.moviedekho.userservie.model.response.UserResponse;
import com.moviedekho.userservie.repository.FavoriteMovieRepository;
import com.moviedekho.userservie.repository.RoleRepository;
import com.moviedekho.userservie.repository.UserRepository;
import com.moviedekho.userservie.service.UserService;
import com.moviedekho.userservie.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @org.springframework.beans.factory.annotation.Autowired(required = true)
    private PaymentServiceClient paymentServiceClient;

    @Autowired(required = true)
    private MovieServiceClient movieServiceClient;

    @Autowired
    private FavoriteMovieRepository favoriteMovieRepository;




    @Override
    public UserResponse register(UserRequest userRequest) throws Exception {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username already taken");
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use");
        }
        if (userRepository.existsByMobileNumber(userRequest.getMobileNumber())) {
            throw new UserAlreadyExistsException("Mobile number already in use");
        }
        UserEntity userEntity = createUserEntity(userRequest);

        if (userRequest.getSubscriptionPlan() != SubscriptionPlan.NONE) {
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setSubscriptionPlan(userRequest.getSubscriptionPlan().name());
            paymentRequest.setAmount(calculatePaymentAmount(userEntity.getSubscriptionPlan().toString()));
            paymentRequest.setUsername(userRequest.getUsername());
            paymentServiceClient.createPayment(paymentRequest);
        }

        UserEntity entity = userRepository.save(userEntity);

        return createUserResponse(userRequest, entity, userEntity);

    }

    private UserResponse createUserResponse(UserRequest userRequest, UserEntity entity, UserEntity userEntity) {
        UserResponse response = new UserResponse();
        if (entity != null) {
            response.setMessage("User Registered Successfully");
            response.setGender(userEntity.getGender());
            response.setRole(userRequest.getRoleName().name());
            response.setEmail(userEntity.getEmail());
            response.setCountry(userEntity.getCountry());
            response.setMobileNumber(userEntity.getMobileNumber());
            response.setSubscriptionPlan(userEntity.getSubscriptionPlan());
            response.setUsername(userEntity.getUsername());
            response.setDateOfBirth(userEntity.getDateOfBirth().toString());
            Set<RoleName> roleNames = userEntity.getRoles().stream()
                    .map(RoleEntity::getName)
                    .collect(Collectors.toSet());
            response.setRoleNames(roleNames);
        }
        return response;
    }

    private UserEntity createUserEntity(UserRequest userRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRequest.getUsername());
        userEntity.setCountry(userRequest.getCountry());
        userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userEntity.setGender(userRequest.getGender());
        userEntity.setDateOfBirth(userRequest.getDateOfBirth());
        userEntity.setSubscriptionPlan((userRequest.getSubscriptionPlan() != null ?
                userRequest.getSubscriptionPlan().name() : SubscriptionPlan.NONE.name()));
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setMobileNumber(userRequest.getMobileNumber());

        RoleEntity roleEntity = roleRepository.findByName(userRequest.getRoleName())
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        userEntity.setRoles(Set.of(roleEntity));

        userEntity.setAuthorities(userEntity.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet()));

        return userEntity;
    }

    @Override
    public UserLoginResponse authenticate(String username, String password) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        String jwtToken = tokenProvider.createToken(userEntity);
        return createUserLoginResponse(jwtToken, userEntity);
    }

    @Override
    public UserLoginResponse getUserDetails(String username) {
        Optional<UserEntity> userResultOptional = userRepository.findByUsername(username);
        if (userResultOptional.isPresent()) {
            UserEntity user = userResultOptional.get();
            UserLoginResponse userResponse = new UserLoginResponse();
            userResponse.setMessage("User Details Fetched Successfully");
            userResponse.setGender(user.getGender());
            userResponse.setCountry(user.getCountry());
            userResponse.setEmail(user.getEmail());
            userResponse.setUsername(user.getUsername());
            userResponse.setMobileNumber(user.getMobileNumber());
            userResponse.setDateOfBirth(user.getDateOfBirth());
            Set<RoleName> roleNames = user.getRoles().stream()
                    .map(RoleEntity::getName)
                    .collect(Collectors.toSet());
            userResponse.setRoleNames(roleNames);
            return userResponse;
        }
        return null;
    }

    @Override
    public UserLoginResponse updateUserDetails(UserRequest userRequest) throws Exception {
        Optional<UserEntity> userOptional = userRepository.findByUsername(userRequest.getUsername());

        if (userOptional.isEmpty()) {
            throw new Exception("Invalid UserName");
        }

        UserEntity user = userOptional.get();

        user.setEmail(userRequest.getEmail() != null ? userRequest.getEmail() : user.getEmail());
        user.setMobileNumber(userRequest.getMobileNumber() != null ? userRequest.getMobileNumber() : user.getMobileNumber());
        user.setCountry(userRequest.getCountry() != null ? userRequest.getCountry() : user.getCountry());
        user.setGender(userRequest.getGender() != null ? userRequest.getGender() : user.getGender());
        user.setDateOfBirth(userRequest.getDateOfBirth() != null ? userRequest.getDateOfBirth() : user.getDateOfBirth());
        user.setSubscriptionPlan(userRequest.getSubscriptionPlan() != null
                ? userRequest.getSubscriptionPlan().toString()
                : user.getSubscriptionPlan());

        PaymentRequest paymentRequest = new PaymentRequest();

        paymentRequest.setAmount(calculatePaymentAmount(userRequest.getSubscriptionPlan().name()));
        paymentRequest.setSubscriptionPlan(userRequest.getSubscriptionPlan().name());
        paymentRequest.setUsername(user.getUsername());
        paymentServiceClient.createPayment(paymentRequest);

        UserEntity userEntity = userRepository.save(user);
        return createUserLoginResponse(null, userEntity);

    }

    @Override
    public FavoriteMovieResponse addFavoriteMovie(FavoriteMovieRequest userFavoriteMovie) throws Exception {

        FavoriteMovieResponse favoriteMovieResponse = new FavoriteMovieResponse();

        if (favoriteMovieRepository.existsByFavoriteMovie(userFavoriteMovie.getMovieTitle())) {
            favoriteMovieResponse.setMessage("Movie Already Added");
            return favoriteMovieResponse;
        }


        ResponseEntity<?> response = movieServiceClient.searchMovieByTitle(userFavoriteMovie.getMovieTitle());
        if (response.getStatusCode().is4xxClientError()) {
            throw new Exception("No Movie Found");
        }


        if (response.getBody() == null) {
            throw new Exception("No Movie Found");
        }

        // Create and save the new favorite movie entity
        FavoriteMovieEntity favoriteMovieEntity = new FavoriteMovieEntity();
        favoriteMovieEntity.setUsername(userFavoriteMovie.getUsername());
        favoriteMovieEntity.setFavoriteMovie(userFavoriteMovie.getMovieTitle());
        favoriteMovieEntity.setFavorited(userFavoriteMovie.isFavorited());

        FavoriteMovieEntity savedEntity = favoriteMovieRepository.save(favoriteMovieEntity);
        if (savedEntity == null) {
            throw new Exception("Failed to save the movie");
        }

        // Prepare and return the response
        favoriteMovieResponse.setUsername(savedEntity.getUsername());
        favoriteMovieResponse.setFavoriteMovies(Collections.singletonList(savedEntity.getFavoriteMovie()));
        favoriteMovieResponse.setMessage("Movie Added Successfully");

        return favoriteMovieResponse;
    }

    @Override
    public ResponseEntity<List<MovieDocument>> getFavoriteMovies(String username) {

        List<String> movieTitles = favoriteMovieRepository.findMovieTitlesByUsername(username);


        List<MovieDocument> movies = movieTitles.stream()
                .map(title -> {
                    ResponseEntity<?> response = movieServiceClient.searchMovieByTitle(title);
                    if (response.getStatusCode().is2xxSuccessful() && response.getBody() instanceof List) {
                        return ((List<MovieDocument>) response.getBody()).get(0);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return ResponseEntity.ok(movies);
    }

    @Override
    public FavoriteMovieResponse removeFavoriteMovie(String username, String title) throws Exception {

        // Check if the movie exists
        ResponseEntity<?> response = movieServiceClient.searchMovieByTitle(title);
        if (response.getStatusCode().is4xxClientError() || response.getBody() == null) {
            throw new Exception("No Movie Found");
        }

        // Find the favorite movie entity
        Optional<FavoriteMovieEntity> favoriteMovieEntityOptional = favoriteMovieRepository.findByUsernameAndFavoriteMovie(username, title);
        if (favoriteMovieEntityOptional.isEmpty()) {
            throw new Exception("Movie not found in user's favorites");
        }

        // Delete the favorite movie entity
        favoriteMovieRepository.delete(favoriteMovieEntityOptional.get());

        // Prepare and return the response
        FavoriteMovieResponse favoriteMovieResponse = new FavoriteMovieResponse();
        favoriteMovieResponse.setUsername(username);
        favoriteMovieResponse.setMessage("Movie Deleted Successfully");
        return favoriteMovieResponse;
    }

    @Override
    public ResponseEntity<FavoriteMovieEntity> getFavoriteMovieByTitle(String username, String title) {
        Optional<FavoriteMovieEntity> favoriteMovieEntityOptional = favoriteMovieRepository.
                findByUsernameAndFavoriteMovie(username, title);
        if (favoriteMovieEntityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        FavoriteMovieEntity favoriteMovieEntity = favoriteMovieEntityOptional.get();

        return ResponseEntity.ok(favoriteMovieEntity);
    }



    private UserLoginResponse createUserLoginResponse(String jwtToken, UserEntity userEntity) {

        UserLoginResponse loginResponse = new UserLoginResponse();

        loginResponse.setUsername(userEntity.getUsername());
        loginResponse.setToken(jwtToken);
        loginResponse.setMobileNumber(userEntity.getMobileNumber());
        loginResponse.setDateOfBirth(userEntity.getDateOfBirth());
        loginResponse.setCountry(userEntity.getCountry());
        loginResponse.setEmail(userEntity.getEmail());
        loginResponse.setGender(userEntity.getGender());
        loginResponse.setMessage("Login Success");
        Set<RoleName> roleNames = userEntity.getRoles().stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());
        loginResponse.setSubscriptionPlan(userEntity.getSubscriptionPlan());
        loginResponse.setRoleNames(roleNames);


        return loginResponse;
    }

    private double calculatePaymentAmount(String subscriptionPlan) {
        return switch (subscriptionPlan) {
            case "PREMIUM" -> 100.0;
            case "BASIC" -> 50.0;
            default -> 00.0;
        };
    }
}
