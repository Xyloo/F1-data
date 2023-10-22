import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const isAuthenticated = this.authService.isLoggedIn;
    const isAdmin = this.isAdmin();
    if (!isAuthenticated || !isAdmin) {
      this.router.navigateByUrl('/login');  // Redirect to login if not authenticated or not admin
    }
    return isAuthenticated && isAdmin;
  }

  isAdmin(): boolean {
    return this.authService.isAdmin;
  }

}
