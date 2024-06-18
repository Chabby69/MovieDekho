import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms'; 
import { MatIconModule } from '@angular/material/icon'; 
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ApiService } from '../_services/api.service';
import { Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-update-profile',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    HttpClientModule
  ],
  templateUrl: './update-profile.component.html',
  styleUrl: './update-profile.component.css'
})
export class UpdateProfileComponent implements OnInit{

  user: any;
  email: any;
  dob:any;
  mobile:any;
  gender: any;
  country: any;
  username: any;

  constructor(
    private fb: FormBuilder,
    private router:Router, 
    private apiService: ApiService,
    private http:HttpClient
  ) {}


  ngOnInit(): void {
    this.user = JSON.parse(sessionStorage.getItem('userDetails'));
    this.email = this.user.email;
    this.dob = this.user.dateOfBirth;
    this.mobile = this.user.mobileNumber;
    this.gender = this.user.gender;
   this.country = this.user.country;
    
  }

  onUpdateProfile() {
    const body = {email: this.email, 
      username: this.user.username, 
      roleNames: this.user.roleNames,
      gender: this.gender,
      dateOfBirth:this.dob,
      country: this.country,
      mobileNumber: this.user.mobileNumber,
      subscriptionPlan:this.user.subscriptionPlan};

      this.http.patch(this.apiService.updateSubscribe(),body).subscribe({
        next:(res:any) => {
          console.log("Updated Profile", res);
          sessionStorage.setItem('userDetails', JSON.stringify(res))
          this.user = res;
          this.router.navigate(['/user-dashboard'])
        }})



    }




}
