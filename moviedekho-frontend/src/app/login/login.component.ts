import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { ApiService } from '../_services/api.service';
import { CommonModule } from '@angular/common';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [HttpClientModule, ReactiveFormsModule, FormsModule, CommonModule, MatInputModule, MatFormFieldModule],
  standalone: true,
})
export class LoginComponent {
  loginForm: FormGroup;
// password: any;
  constructor(private router: Router, 
    private fb: FormBuilder, 
    private http: HttpClient,
    private authService: AuthService, 
    private apiService: ApiService) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.pattern('^[a-zA-Z0-9]*$')]], // Alphanumeric usernames without special characters
      password: ['', [
        Validators.required,
        Validators.minLength(5),
        Validators.pattern('(?=.*[A-Z])(?=.*[!@#$&*]).+') // At least one uppercase and one symbol
      ]]});
  }

  userName:string = "";
  password:any = "";
  userNameError:boolean = false;
  passwordError:boolean = false;
  passwordPatternError:boolean = false;


  onSubmit(){
    console.log(this.password.length > 4 ,
      "2", !this.password.includes(" ") ,"3", 
      /\d/.test(this.password) ,"4",  
      this.password.toUpperCase() !== this.password ,"5",
      this.password.toLowerCase() !== this.password)
      
    if(this.userName == ""){
      this.userNameError = true;
    }
    if(this.password.length == 0){
      this.passwordError = true;
    }
    if(this.password.length <= 4 || this.password.includes(" ") ||! /\d/.test(this.password) ||  this.password.toUpperCase() == this.password || this.password.toLowerCase() == this.password){
      this.passwordPatternError = true;
    }
    if(!this.userNameError && !this.passwordError && !this.passwordPatternError){
      this.userNameError = false;
      this.passwordError = false;
      this.passwordPatternError = false;
      const password = this.password
      console.log(this.passwordError,this.passwordPatternError,";klaeg");
      this.http.post(this.apiService.login(), { userName: this.userName, password }).subscribe({
        next:(res:any) =>{
          const token= res.token;
          sessionStorage.setItem('userDetails', JSON.stringify(res));
          sessionStorage.setItem('token', token);
     
      console.log('Login successful:', res);
      sessionStorage.setItem('userLogin', 'true');
      if (res && Array.isArray(res.roleNames)) {
        if (res.roleNames.includes('ROLE_USER')) { 
          console.log('In Role_user ::: ', res.roleNames);
          this.router.navigateByUrl('/user-dashboard');
        } else if (res.roleNames.includes('ROLE_ADMIN')) {
          this.router.navigate(['/admin-dashboard']); 
        }
      }
        },
        error(err) {
          console.error('Login failed:', err);
        },
      })
    }
  }

  // onSubmit() {
  //   console.log(this.loginForm)
  //   if (this.loginForm.valid) {
  //     const { username, password } = this.loginForm.value;

  //     this.http.post(this.apiService.login(), { userName: username, password }).subscribe({
  //       next:(res:any) =>{
  //         const token= res.token;
  //         sessionStorage.setItem('userDetails', JSON.stringify(res));
  //         sessionStorage.setItem('token', token);
     
  //     console.log('Login successful:', res);
  //     sessionStorage.setItem('userLogin', 'true');
  //     if (res && Array.isArray(res.roleNames)) {
  //       if (res.roleNames.includes('ROLE_USER')) { 
  //         console.log('In Role_user ::: ', res.roleNames);
  //         this.router.navigateByUrl('/user-dashboard');
  //       } else if (res.roleNames.includes('ROLE_ADMIN')) {
  //         this.router.navigate(['/admin-dashboard']); 
  //       }
  //     }
  //       },
  //       error(err) {
  //         console.error('Login failed:', err);
  //       },
  //     })
  //   }
  // }

}
