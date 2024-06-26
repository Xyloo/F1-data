import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';

import { AppComponent } from './app.component';
import { NavMenuComponent } from './nav-menu/nav-menu.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { LoadingAnimationComponent } from './loading-animation/loading-animation.component';
import { AuthGuard } from './auth-guard.service';
import { TokenInterceptor } from './interceptors/token-interceptor';
import { CircuitListComponent } from './circuit-list/circuit-list.component';
import { CircuitDetailsComponent } from './circuit-details/circuit-details.component';
import { ConstructorsComponent } from './constructors/constructors.component';
import { ConstructorDetailsComponent } from './constructor-details/constructor-details.component';

@NgModule({
  declarations: [

      AppComponent,
      NavMenuComponent,
      HomeComponent,
      LoginComponent,
      RegisterComponent,
      LoadingAnimationComponent,
      CircuitListComponent,
      CircuitDetailsComponent,
      ConstructorsComponent,
      ConstructorDetailsComponent,

  ],
  imports: [
    BrowserModule.withServerTransition({ appId: 'ng-cli-universal' }),
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule,
    CommonModule,
    RouterModule.forRoot([
        { path: '', component: HomeComponent, pathMatch: 'full' },
        { path: 'login', component: LoginComponent },
        { path: 'register', component: RegisterComponent },
        { path: 'circuits', component: CircuitListComponent },
        { path: 'circuits/:id', component: CircuitDetailsComponent },
        { path: 'constructors', component: ConstructorsComponent },
        { path: 'constructors/:id', component: ConstructorDetailsComponent },
    ])
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
