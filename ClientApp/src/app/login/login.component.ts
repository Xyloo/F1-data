import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

    username = '';
    email = '';
    password = '';
    message = '';
    isError = false;


  constructor(private authService: AuthService, private router: Router) { }


  login() {
    this.authService.login(this.username, this.password).subscribe((response: any) => {

      setTimeout(() => this.router.navigate(['/']), 1000);
    },
      (error: any) => { // Error callback

        this.message = error;
        this.isError = true;

        console.error(error);
      });

  }
}
