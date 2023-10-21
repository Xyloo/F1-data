import { Component } from '@angular/core';
import { AuthService} from '../auth.service'
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

    username = '';
    email = '';
    password = '';
    message = '';
    isError = false;
    isSuccess = false;

  constructor(private authService: AuthService, private router: Router) { }

  register() {
    // Creating a user object

    let user = {
      username: this.username,
      email: this.email,
      password: this.password
    };

    this.authService.register(user).subscribe(
      (response: any) => {
        this.message = 'Registration successful!';
        this.isSuccess = true;
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      (error: any) => { // Error callback

        this.message = error;
        this.isError = true;

        console.error(error);
      }
    );
  }
}
