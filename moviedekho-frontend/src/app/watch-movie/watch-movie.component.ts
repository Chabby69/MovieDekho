import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-watch-movie',
  standalone: true,
  imports: [],
  templateUrl: './watch-movie.component.html',
  styleUrl: './watch-movie.component.css'
})
export class WatchMovieComponent implements OnInit{

  movieDetails: any;
  constructor(private router: Router){
    this.movieDetails = this.router.getCurrentNavigation()?.extras.state;
    console.log(this.movieDetails);
  };

  ngOnInit(): void {
  }
}


