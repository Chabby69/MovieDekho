
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

declare var Razorpay:any;
@Component({
  selector: 'app-subscribe',
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
  templateUrl: './subscribe.component.html',
  styleUrl: './subscribe.component.css'
})
export class SubscribeComponent implements OnInit{


  subscriptionTypes = ['NONE', 'BASIC', 'PREMIUM'];
  selectedSubscription = 'NONE';
  user: any;
  email: any;
  dob:any;
  mobile:any;
  gender: any;
  country: any;
  username: any;
  subscriptionPlan: any;
  alreadyPremiumMemberError: any;
  alreadyBasicMemberError: any;


  constructor(
    private fb: FormBuilder,
    private router:Router, 
    private apiService: ApiService,
    private http:HttpClient
  ) {
     /* this.email = this.user.email;
     this.dob = this.user.dob;
     this.mobile = this.user.mobileNumber;
     this.gender = this.user.gender;
    this.country = this.user.country;
  this.username = this.user.userName; */
  }

  ngOnInit(): void {
    this.user = JSON.parse(sessionStorage.getItem('userDetails'));
    console.log(this.user);
    this.email = this.user.email;
    this.dob = this.user.dateOfBirth;
    this.mobile = this.user.mobileNumber;
    this.gender = this.user.gender;
   this.country = this.user.country;
    
  }

  onUpdateSubscription() {
    //const formData = this.userForm.value;
    
    const body = {email: this.email, 
    username: this.user.username, 
    roleNames: this.user.roleNames,
    gender: this.gender,
    country: this.country,
    mobileNumber: this.user.mobileNumber,
    subscriptionPlan:this.subscriptionPlan};   

        if(body.roleNames.includes('ROLE_USER')){
        let subscriptionAmount = 0;
     if (body.subscriptionPlan === "PREMIUM" ) {
    if(this.user.subscriptionPlan == "PREMIUM") {
      this.alreadyPremiumMemberError = true;
    }else{
      subscriptionAmount = 100000;
      makePayment(subscriptionAmount, body);
      this.http.patch(this.apiService.updateSubscribe(),body).subscribe({
        next:(res:any) => {
          console.log("Updated Subscription", res);
          sessionStorage.setItem('userDetails', JSON.stringify(res))
          this.user = res;
          this.router.navigate(['/user-dashboard'])
        }})
    }
     
  } else if (body.subscriptionPlan === "BASIC") {
    if(this.user.subscriptionPlan == "BASIC"){
      this.alreadyBasicMemberError = true;
    }else{
      subscriptionAmount = 50000; 
      makePayment(subscriptionAmount, body);
      this.http.patch(this.apiService.updateSubscribe(),body).subscribe({
        next:(res:any) => {
          console.log("Updated Subscription", res);
          sessionStorage.setItem('userDetails', JSON.stringify(res))
          this.user = res;
          this.router.navigate(['/user-dashboard'])
        }})
    }
    
  } 
    }
    
  }
  
}

function makePayment(subscriptionAmount: number, body: any) {
  const RozarpayOptions = {
    description: 'Movie Subscription',
    currency: 'INR',
    amount: subscriptionAmount,
    name: body.username,
    key: 'rzp_test_X0L2E3ySWHjzk6',
    image: 'https://i.imgur.com/FApqk3D.jpeg',
    prefill: {
      name: body.username,
      email: body.email,
      phone: body.mobileNumber
    },
    theme: {
      color: '#6466e3'
    },
    modal: {
      ondismiss:  () => {
        
      },
      onsubmit: () =>{
      }
    }
  }

  const successCallback = (paymentid: any) => {
    console.log(paymentid);
  }


  const failureCallback = (e: any) => {
    console.log(e);
  }

  Razorpay.open(RozarpayOptions,successCallback, failureCallback)
}


