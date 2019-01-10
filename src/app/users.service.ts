import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { User } from './user'

const httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json',
      'Authorization': 'my-auth-token'
    })
  };

@Injectable()
export class UsersService {

  constructor(private http: HttpClient) {}

  createUser (user: User): Observable<User> {
      return this.http.post<User>("create", user, httpOptions)
  }

}