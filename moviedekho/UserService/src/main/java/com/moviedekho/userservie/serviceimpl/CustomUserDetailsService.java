package com.moviedekho.userservie.serviceimpl;

import com.moviedekho.userservie.entity.UserEntity;
import com.moviedekho.userservie.exception.UserAlreadyExistsException;
import com.moviedekho.userservie.model.request.UserRequest;
import com.moviedekho.userservie.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList() // You would add authorities here based on the user's roles
        );
    }

    public UserEntity registerNewUserAccount(UserRequest accountDto) throws UserAlreadyExistsException {
        if (emailExist(accountDto.getEmail())) {
            throw new UserAlreadyExistsException("There is an account with that email address: " + accountDto.getEmail());
        }
        UserEntity user = new UserEntity();

        user.setUsername(accountDto.getUsername());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setGender(accountDto.getGender());


        return userRepository.save(user);
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}

