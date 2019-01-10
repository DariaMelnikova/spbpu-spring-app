import { Component, OnInit } from '@angular/core';
import { AppService } from './app.service';
import { HttpClient } from '@angular/common/http';

import { User } from './user'

@Component({
  selector: 'home-component',
  templateUrl: './home.component.html'
})
export class HomeComponent {

  isActor = false;
  isViewer = false;
  isDirector = false;
  isAdmin = false;

  title = 'Demo';
  users: User[];

  constructor(private app: AppService, private http: HttpClient) {
    const occupation = localStorage.getItem('occupation')
    this.isActor = occupation === "ACTOR";
    this.isViewer = occupation === 'VIEWER';
    this.isDirector = occupation === 'DIRECTOR';
    this.isAdmin = occupation === 'ADMINISTRATOR';
  }

  ngOnInit() {
    this.http.get<User[]>('resource').subscribe(data => this.users = data);
  }

  authenticated() { return this.app.authenticated; }

  delete(user: User) {
    this.users.splice(this.users.indexOf(user, 0), 1)
  }

}
