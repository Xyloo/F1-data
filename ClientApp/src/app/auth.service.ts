import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, map, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isLoggedIn = false;
  isAdmin = false;

  constructor(private http: HttpClient) { }

  login(username: string, password: string) {
    return this.http.post(`/api/auth/signin`, { username, password })
      .pipe(
        map((response: any) => {
          // save token in local storage if it exists
          if (response && response.token) {
            localStorage.setItem('currentUser', response.token);
          }
          this.isLoggedIn = true;
          if(response.roles.includes("ROLE_ADMIN")) {
            this.isAdmin = true;
          }
          return response;
        }),
        catchError(this.handleError)  // error handling
      );
  }


  register(user: any) {
    return this.http.post('/api/auth/signup', user)
      .pipe(
        catchError(this.handleError)
      );
  }
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Unknown error!';
    if (error.status === 409 || error.status === 401 || error.status === 400) {
      errorMessage = error.error.message
      console.log(error.error);
    }
    // Handle different HTTP error statuses here...
    return throwError(errorMessage);
  }

  logout() {
    localStorage.removeItem('currentUser');
    this.isLoggedIn = false;
  }


  getToken() {
    let token = localStorage.getItem("currentUser");
    return token ? token.replace(/"/g, '') : null;
  }


}
