import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  constructor(private router: Router) {}

  // Function to get the token expiration timestamp from the token
  getTokenExpiration(token: string): number {
    const payload = token.split('.')[1]; 
    const decodedPayload = atob(payload); 
    const { exp } = JSON.parse(decodedPayload); 
    return exp * 1000;
  }

  isTokenExpired(token: string): boolean {
    const expirationTime = this.getTokenExpiration(token);
    const currentTime = new Date().getTime();
    return currentTime >= expirationTime;
  }

  logoutIfTokenExpired(): void {
    const token = sessionStorage.getItem('token');
    console.log(token)
    if (token && this.isTokenExpired(token)) {
      sessionStorage.removeItem('userLogin'); 
      this.router.navigate(['/home']);
    }
  }
}
