import { Component, OnInit } from '@angular/core';
import { Router, Event, NavigationStart, NavigationEnd, NavigationError } from '@angular/router'; 
import { CommonModule } from '@angular/common';
import { TokenService } from '../token.service';
import { Location } from '@angular/common';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { ApiService } from '../_services/api.service';

@Component({
  selector: 'app-header-component',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header-component.component.html',
  styleUrl: './header-component.component.css'
})
export class HeaderComponentComponent implements OnInit{


  loggedIn: boolean = false;
  loggedInAndIsUser: boolean = false;
   user: any;

  constructor(private router: Router, private http: HttpClient, 
    private apiService: ApiService,
    private tokenService: TokenService, private location: Location) {
    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        if(sessionStorage.getItem('userLogin')){
         this.loggedIn = true;
         const userDetails = JSON.parse(sessionStorage.getItem('userDetails'));
         if(userDetails.roleNames.includes("ROLE_USER")){
          this.loggedInAndIsUser = true;
         }
        }
      }
 }
);

  } 
  ngOnInit(): void {
    this.tokenService.logoutIfTokenExpired();
  }
  
  navigateTo(path: string) {  
    this.router.navigateByUrl(path);
  }

  validateSubscribe() {
    if(sessionStorage.getItem('userLogin')){
      this.navigateTo('/subscribe');
    }else{
      this.navigateTo('/login');
    }
    }

    updateProfile() {
      if(sessionStorage.getItem('userLogin')){
        this.navigateTo('/updateProfile');
      }else{
        this.navigateTo('/login');
      }
      }

  logout(){
    sessionStorage.removeItem('userLogin');
    this.loggedIn = false;
    this.router.navigate(['/home']);
  }
  Back() {

    this.location.back();
    
  }

}
