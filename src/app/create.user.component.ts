import { Component, OnInit } from '@angular/core';
import { AppService } from './app.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

import { User } from './user'

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json'
  })
};

@Component({
  templateUrl: './create.user.component.html'
})
export class CreateUserComponent {

  user = new User("-1", "", "", "", "", "ACTOR");

  constructor(private app: AppService, private http: HttpClient, private router: Router) {}

  ngOnInit() {
    
  }

  create() {
    this.http.post<User>("create", this.user, httpOptions).subscribe(user => user)
  }

}