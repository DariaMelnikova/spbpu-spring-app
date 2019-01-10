import { Component, OnInit } from '@angular/core';
import { AppService } from './app.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

import { Play } from "./play";
import { User } from './user';
import { Observable } from 'rxjs';
import { DirectorPlay } from './directorplay';
import { Role } from './role';

const httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
};

@Component({
    templateUrl: './create.role.component.html'
})
export class CreateRoleComponent {

    directorPlays: DirectorPlay[];

    selectedPlay: DirectorPlay;

    createdRole: Role = new Role("-1", "", undefined, undefined);

    constructor(private app: AppService, private http: HttpClient, private router: Router) {}

    ngOnInit() {
        this.http.get<DirectorPlay[]>('director').subscribe(data => this.directorPlays = data);
    }

    onPlaySelected(play: DirectorPlay) {
        this.selectedPlay = play;
        this.createdRole.play = play.play;
        this.createdRole.actor = play.actors[0];
    }

    onActorChange(actor) {
        this.createdRole.actor = actor;
    }

    createRole() {
        this.http.post<DirectorPlay[]>('create/role', this.createdRole, httpOptions).subscribe(data => {
            this.directorPlays = data;
            this.selectedPlay = undefined;
            this.createdRole = new Role("-1", "", undefined, undefined)
            window.alert("Success");
        },
        error => {
            window.alert(error.error.message);
        })
    }

}