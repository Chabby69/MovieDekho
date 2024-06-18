import { AfterViewInit, Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms'; 
import { MatIconModule } from '@angular/material/icon'; 
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MovieResponse } from '../../models/MovieResponse.model';

@Component({
  selector: 'app-update-movie',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    FormsModule,
    ReactiveFormsModule, 
  ],
  templateUrl: './update-movie.component.html',
  styleUrl: './update-movie.component.css'
})
export class UpdateMovieComponent implements OnInit{

  movie: any  = JSON.parse(sessionStorage.getItem("updateMovie"));
  title:any;
  updateMovieForm: any;
  queryParams:any;
  selectedFile: File | null = null;

constructor( private router:Router,
  private fb: FormBuilder,
  private http: HttpClient,
  private authService: AuthService
) { 
  console.log(this.movie);
  this.updateMovieForm = this.fb.group({
    title: [this.movie.title],
    actors: [this.movie.actors],
    yearOfRelease: [this.movie.yearOfRelease],
    rating:[this.movie.rating],
    genre: [this.movie.genre],
    streamLink: [this.movie.streamLink],
    moviePoster: [this.movie.moviePoster],
    videoFile: [null],
  });
}

ngOnInit(): void {


  this.updateMovieForm = this.fb.group({
    
    title: [this.movie.title],
    actors: [this.movie.actors],
    yearOfRelease: [this.movie.yearOfRelease],
    rating:[this.movie.rating],
    genre: [this.movie.genre],
    streamLink: [this.movie.streamLink],
    moviePoster: [this.movie.moviePoster],
    videoFile: [null],
    
  });
  
}

token = this.authService.getToken();

onSubmit() {

  console.log(this.updateMovieForm)

  if (this.updateMovieForm.valid) {
    const formData = new FormData();
    const yearOfRelease =  parseInt(this.updateMovieForm.value.yearOfRelease, 10)

    formData.append("title", this.movie.title);
    formData.append("actors",  this.updateMovieForm.value.actors);
    formData.append("genre", this.updateMovieForm.value.genre);
    formData.append("yearOfRelease", yearOfRelease.toString());
    formData.append("rating", this.updateMovieForm.value.rating);
    formData.append("streamLink", this.updateMovieForm.value.streamLink);
    formData.append("moviePoster", this.updateMovieForm.value.moviePoster);
    formData.append("videoFile", this.selectedFile);

    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}`).set('Content-Type', 'application/json');
    
    this.http
      .patch<MovieResponse>('http://localhost:8082/api/movie/updateMovieDetails', formData) 
      .subscribe(
        (response: any) => {
          console.log('Update successful:', response);
          this.router.navigate(['/admin-dashboard'])
        },
        (error: any) => { 
          console.error('Update Movie failed:', error); 
        }
      );
  } else {
    console.error('Form is not valid'); 
  }
  
  }

  onFileChange(event: any) {
    const files = event.target.files;
    if (files && files.length > 0) {
      this.selectedFile = files[0]; 
    }
  }

}
