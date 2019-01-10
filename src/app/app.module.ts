import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';
import { AppService } from './app.service';
import { AppComponent } from './app.component';
import { HomeComponent } from './home.component';
import { LoginComponent } from './login.component';
import { CreateUserComponent } from './create.user.component';
import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HTTP_INTERCEPTORS
} from '@angular/common/http';

import { RoleGuardService } from './role-guard.service';

import { Observable } from 'rxjs/Observable';
import { GiveBonusComponent } from './give-bonus.component';
import { CreatePlayComponent } from './create.play.component';
import { ManageTicketsComponent } from './manage-tickets.component';
import { BuyTicketComponent } from './buyticket.component';
import { BoughtTicketsComponent } from './bought.tickets.component';
import { CreateRoleComponent } from './create.role.component';
import { RequestBonusComponent } from './request.bonus.component';
import { ManageRolesComponent } from './manage.roles.component';

@Injectable()
export class XhrInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const xhr = req.clone({
      headers: req.headers.set('X-Requested-With', 'XMLHttpRequest')
    });
    return next.handle(xhr);
  }
}

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'home' },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'create', component: CreateUserComponent},
  { 
    path: 'givebonus', 
    component: GiveBonusComponent, 
    canActivate: [RoleGuardService], 
    data: { expectedRole: "ADMINISTRATOR" } 
  },
  { 
    path: 'createplay', 
    component: CreatePlayComponent, 
    canActivate: [RoleGuardService], 
    data: { expectedRole: "ADMINISTRATOR" } 
  },
  { 
    path: 'managetickets', 
    component: ManageTicketsComponent, 
    canActivate: [RoleGuardService], 
    data: { expectedRole: "ADMINISTRATOR" } 
  },
  { 
    path: 'buyticket', 
    component: BuyTicketComponent, 
    canActivate: [RoleGuardService], 
    data: { expectedRole: "VIEWER" } 
  },
  { 
    path: 'boughttickets', 
    component: BoughtTicketsComponent, 
    canActivate: [RoleGuardService], 
    data: { expectedRole: "VIEWER" } 
  },
  { 
    path: 'createrole', 
    component: CreateRoleComponent, 
    canActivate: [RoleGuardService], 
    data: { expectedRole: "DIRECTOR" } 
  },
  { 
    path: 'requestbonus', 
    component: RequestBonusComponent, 
    canActivate: [RoleGuardService], 
    data: { expectedRole: "DIRECTOR" } 
  },
  { 
    path: 'manageroles', 
    component: ManageRolesComponent, 
    canActivate: [RoleGuardService], 
    data: { expectedRole: "ACTOR" } 
  }
];

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    CreatePlayComponent,
    CreateUserComponent,
    GiveBonusComponent,
    ManageTicketsComponent,
    BuyTicketComponent,
    BoughtTicketsComponent,
    CreateRoleComponent,
    RequestBonusComponent,
    ManageRolesComponent
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [AppService, RoleGuardService, { provide: HTTP_INTERCEPTORS, useClass: XhrInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }