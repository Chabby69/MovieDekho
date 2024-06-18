import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private token: string = '';

  // Store the token
  setToken(token: string): void {
    this.token = token;
  }

  // Retrieve the token
  getToken(): string {
    console.log(this.token);
    return this.token;
  }
  hasRole(role: string): boolean {
    
    const userDetails = JSON.parse(sessionStorage.getItem('userDetails'));
    const userRoles = userDetails.roleNames;
    return userRoles.includes(role);
  }

}
