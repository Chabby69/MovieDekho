
import { AfterViewInit, Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms'; 
import { MatIconModule } from '@angular/material/icon'; 
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MovieResponse } from '../../models/MovieResponse.model';

@Component({
  selector: 'app-add-movie-form',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    FormsModule,
    ReactiveFormsModule, 
  ],
  templateUrl: './add-movie-form.component.html',
  styleUrl: './add-movie-form.component.css'
})
export class AddMovieFormComponent  implements OnInit{
  
  movie: any;
  addMovieForm: any;
  selectedFile: File | null = null;

constructor( private router:Router,
  private fb: FormBuilder,
  private http: HttpClient,
  private authService: AuthService
) { 
  this.addMovieForm = this.fb.group({
    title: ['', Validators.required],
    actors: ['', Validators.required],
    yearOfRelease: ['', Validators.required],
    rating:['',Validators.required],
    genre: ['', Validators.required],
    streamLink: ['', Validators.required],
    moviePoster: ['', Validators.required],
    videoFile: [null],
  });
}
  
  ngOnInit(): void {
   
  }
    token = sessionStorage.getItem('token');

    onFileChange(event: any) {
      const files = event.target.files;
      if (files && files.length > 0) {
        this.selectedFile = files[0]; 
      }
    }

    onSubmit() {
      if (this.addMovieForm.valid && this.selectedFile) {

        const formData = new FormData();
        const yearOfRelease =  parseInt(this.addMovieForm.value.yearOfRelease, 10)

        formData.append("title", this.addMovieForm.value.title);
        formData.append("actors",  this.addMovieForm.value.actors);
        formData.append("genre", this.addMovieForm.value.genre);
        formData.append("yearOfRelease", yearOfRelease.toString());
        formData.append("rating", this.addMovieForm.value.rating);
        formData.append("streamLink", this.addMovieForm.value.streamLink);
        formData.append("moviePoster", this.addMovieForm.value.moviePoster);
        formData.append("videoFile", this.selectedFile);

       // const formData = this.addMovieForm.value; 


        const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}`).set('Content-Type', 'application/json');
       
        this.http
          .post<MovieResponse>('http://localhost:8082/api/movie/addMovieDetails', formData) 
          .subscribe(
            (response: any) => {
              console.log('Registration successful:', response);
              this.router.navigate(['/admin-dashboard'])
            },
            (error: any) => { 
              console.error('Add Movie failed:', error); 
            }
          );
      } else {
        console.error('Form is not valid'); 
      }
    }
}
