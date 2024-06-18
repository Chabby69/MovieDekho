import { Component, OnInit } from '@angular/core';
import { NgFor } from '@angular/common';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { ApiService } from '../_services/api.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NgFor,CommonModule, HttpClientModule ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  movies: any[] = []; 
  movie: any;
  targetedMovie: any;
  errormsg: any;
  
  userLoggedIn: boolean = false;
  constructor(private route:Router,private http: HttpClient, private apiService: ApiService) {}

  ngOnInit(): void {
  
  
    //const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}`).set('Content-Type', 'application/json');
    this.http.get(this.apiService.getAllMovies()).subscribe({
      next:(res:any) =>{
        console.log(res)
        sessionStorage.setItem('movies', JSON.stringify(res));
        this.movies = res;  
      },
      error(err) {
        console.error("Error.....")
      },
    });
    this.playMovie(this.movie.title);
   
  }

   playMovie(title: any) {
    if (sessionStorage.getItem('userLogin')) {

      const userDetails = JSON.parse(sessionStorage.getItem('userDetails'));
    
      if(userDetails.roleNames.includes("ROLE_USER")){
        let a= this.movies.filter((data:any)=> {return data.title == title});
      this.targetedMovie = a[0];
      this.route.navigate(['watch-movie'], {
        state: this.targetedMovie
      });
      }
    } else{
     //this.errormsg = 'you have to login';
      this.route.navigate(['/login']);
    }
  
  }

}


