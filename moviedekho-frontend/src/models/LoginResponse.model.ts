export class LoginResponse {
    message: string = ''; // Add default values to avoid errors
    token: string = '';
    email: string = '';
    mobileNumber: string = '';
    roleNames: string[] = []; // Explicitly define it as an array of strings
    country?: string;
    dateOfBirth?: string;
    gender?: string;
    username: string = ''; 
  }
  