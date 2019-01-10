import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from './user';

@Injectable()
export class AppService {

  authenticated = false;

  constructor(private http: HttpClient) {}

  authenticate(credentials, callback) {
        const headers = new HttpHeaders(credentials ? {
            authorization : 'Basic ' + btoa(credentials.username + ':' + credentials.password)
        } : {});

        this.http.get<User>('user', {headers: headers}).subscribe(user => {
            if (user) {
                this.authenticated = true;
                localStorage.setItem('occupation', user.occupation)
            } else {
                this.authenticated = false;
            }
            return callback && callback();
        });
    }

    isAuthenticated() {
        return this.authenticated
    }

}