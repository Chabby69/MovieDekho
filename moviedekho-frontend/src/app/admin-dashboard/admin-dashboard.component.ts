import {  Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Movie } from '../../models/movie.model';
import { ApiService } from '../_services/api.service';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit{
movies: any[] = []; 
selectedMovie: Movie | null = null; 

dashboardData: any;
filteredMovies: any;
modelData: Movie | null = null;
searchGenre: string = '';
searchTitle: string = '';
searchActors: string = '';
searchRating: number | null = null;
searchYear: number | null = null;
movie: any;

constructor(
  private router: Router,
  private http: HttpClient,
  private apiService: ApiService, 
  private authService: AuthService ) {}

  token = sessionStorage.getItem('token');


  ngOnInit(): void {
   
    if(!sessionStorage.getItem('userLogin') && !this.authService.hasRole('ROLE_ADMIN')){
      this.router.navigate(['/login']);
    }
    this.getAllMovies();
   this.deleteMovie(this.movie.title);
  }

  

  deleteMovie(title: any) {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}`);
    this.http.delete(this.apiService.deleteMovie(title), { headers }).subscribe({
      next: (res: any) => {
        console.log("Movie Deleted");
  
        this.router.navigate(['/admin-dashboard']);
      //  this.modalService.open(this.successModal, { centered: true });
      },
      error: (err: any) => {
        console.error("Error while deleting the movie.");
      }
    });
  }

  
  getAllMovies(): any{
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}`).set('Content-Type', 'application/json');
    this.http.get(this.apiService.getAllMovies(), {headers}).subscribe({
      next:(res:any) =>{
        console.log(res)
        this.movies = res;  
        
      },
      error(err) {
        console.error("Error.....")
      },
    });
  }
  getModalData(data: Movie): void {
    this.modelData = data; 
  }

 

    updateMovieDetails(movie: any): void {
      
      sessionStorage.setItem("updateMovie",JSON.stringify(movie));
      this.router.navigate(['/update-movie-form'], {
        queryParams: {
          title: movie.title,
          actors: movie.actors, 
          genre: movie.genre,
          yearOfRelease: movie.yearOfRelease,
          rating: movie.rating,
          streamLink: movie.streamLink,
          moviePoster: movie.moviePoster,
        },
      });
    }

    openAddMovieForm() {
      this.router.navigate(['/add-movie-form']);
      }
}
