export class RegisterResponse {
    message: string; 
    error: string | null; 
    email: string; 
    mobileNumber: string; 
    role: string; 
    username: string; 
    dateOfBirth: string; 
    gender: string; 
    country: string; 
    subscriptionPlan: string; 
  
    constructor(
      message: string,
      error: string | null,
      email: string,
      mobileNumber: string,
      role: string,
      username: string,
      dateOfBirth: string,
      gender: string,
      country: string,
      subscriptionPlan: string
    ) {
      this.message = message;
      this.error = error;
      this.email = email;
      this.mobileNumber = mobileNumber;
      this.role = role;
      this.username = username;
      this.dateOfBirth = dateOfBirth;
      this.gender = gender;
      this.country = country;
      this.subscriptionPlan = subscriptionPlan;
    }
  }
  