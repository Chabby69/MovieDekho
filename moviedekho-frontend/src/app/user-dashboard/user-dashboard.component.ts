import { Component, OnInit, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Movie } from '../../models/movie.model';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../auth.service';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { ApiService } from '../_services/api.service';
import * as bootstrap from 'bootstrap';
import { Router } from '@angular/router';


@Component({
  selector: 'app-user-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './user-dashboard.component.html',
  styleUrl: './user-dashboard.component.css'
})
export class UserDashboardComponent implements OnInit, AfterViewInit{

movies: any[] = []; 
selectedMovie: Movie | null = null; 

dashboardData: any;
username: any;
title:any;
filteredMovies: any;
modelData: Movie | null = null;
searchGenre: string = '';
searchTitle: string = '';
searchActors: string = '';
searchRating: number | null = null;
searchYear: number | null = null;
movie: any;
targetedMovie: any;
favMovie: any;
errormsg: any;
isFavoriteMovies: boolean = true;
favoriteMovies: any;
favMovieTitles: any[] = [];

  constructor(private route: Router,private http: HttpClient,private apiService: ApiService, private authService: AuthService) {}
  ngAfterViewInit(): void {
    
  }


  ngOnInit(): void {
    this.searchMovies();
    this.getAllMovies();
    sessionStorage.setItem('addFavorite', 'True');
  }
   token = sessionStorage.getItem('token');
   user = JSON.parse(sessionStorage.getItem('userDetails'));
  getAllMovies(): any{
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}`).set('Content-Type', 'application/json');
    this.http.get(this.apiService.getAllMovies(), {headers}).subscribe({
      next:(res:any) =>{
        console.log(res)
        this.movies = res;  
        
      this.http.get(this.apiService.getFavoriteMovies(this.user.username)).subscribe({
        next:(res:any) => {

        this.isFavoriteMovies = false;
        console.log(res)
        this.favMovieTitles = res.map((data: any) => data.title)
        for(let i of this.movies){
          if(this.favMovieTitles.includes(i.title)){
              i.favoriteMovie = true
          }
          else{
              i.favoriteMovie = false
          }
      }
      console.log("Test",this.movies)
      }
    });
      },
      error(err) {
        console.error("Error.....")
      },
    });
  }

  searchMovies(): any {
    // Define query parameters
    const queryParams = {
      title: this.searchTitle,
      genre: this.searchGenre,
      actors: this.searchActors,
      minRating: this.searchRating,
      releaseYear: this.searchYear,
    };

    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}`).set('Content-Type', 'application/json');
    this.http.get(this.apiService.searchMovies(queryParams), {headers}).subscribe({
      next:(res:any) => {
        console.log(res)
        this.movies = res; 
      }
    })
  }

  getModalData(data: Movie): void {
    this.modelData = data; 
  }

  playMovie(title: any) {
    if (sessionStorage.getItem('userLogin')) {
    
      let a= this.movies.filter((data:any)=> {return data.title == title});
      this.targetedMovie = a[0];
      this.route.navigate(['watch-movie'], {
        state: this.targetedMovie
      });

    } else{
     //this.errormsg = 'you have to login';
      this.route.navigate(['/login']);
    }
  
  }

  getUserFavMovies(){
    this.http.get(this.apiService.getFavoriteMovies(this.user.username)).subscribe({
      next:(res:any) => {
        this.isFavoriteMovies = false;
        console.log(res)
        this.movies = res;
      }
    });

}

selectFav(title: any, task: any, event: Event){
  event.stopPropagation();
  if(task === 'add'){
    this.addFavorite(title);
  } 

  if(task === 'remove'){
    this.removeFavorite(title);
  } 

}

toggleFavorite(movie: any, event: Event) {
  event.stopPropagation();

  if (!movie.favorited) {
    this.addFavorite(movie.title);
  } else {
    this.removeFavorite(movie.title);
  }
}

removeFavorite(title: any) {
  this.http
    .delete(this.apiService.removeFavoriteMovie(this.user.username, title)).subscribe({
      next:(res:any) => {
        console.log(res);
        this.getAllMovies();
        sessionStorage.setItem('addFavorite', 'False');
      },
      error(err){
        console.error('Error....')
      }
    });
}

addFavorite(title: any){
  
  this.http.post(this.apiService.addFavoriteMovie(), {username: this.user.username,
    movieTitle: title}).subscribe({
    next:(res:any) =>{
      console.log(res)
      this.getAllMovies();
      sessionStorage.setItem('addFavorite', 'True');
    },
    error(err) {
      console.error("Error.....")
      
    },
  });

}
  }

