import { Component, OnInit } from '@angular/core';
import { AppService } from './app.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

import { Play } from "./play";
import { User } from './user';
import { Observable } from 'rxjs';

const httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
};

@Component({
    templateUrl: './create.play.component.html'
})
export class CreatePlayComponent {
    play = new Play("", "", "", "", -1, undefined, undefined);

    directors: User[] = [];

    constructor(private app: AppService, private http: HttpClient, private router: Router) {}

    ngOnInit() {
        this.http.get<User[]>('directors').subscribe(data => {
            this.play.director = data[0];
            this.directors = data;
        });
    }

    create() {
        this.http.post<Play>("create/play", this.play, httpOptions).subscribe(play => {
            window.alert("Created");
        },
        error => window.alert(error.error.message));
    }

    onChange(user) {
        this.play.director = user
    }

}