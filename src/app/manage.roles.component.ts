import { Component } from "@angular/core";
import { AppService } from "./app.service";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from "@angular/router";
import { Ticket } from "./ticket";
import { Role } from './role';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json'
  })
};

@Component({
    templateUrl: './manage.roles.component.html'
})
export class ManageRolesComponent {

    roles: Role[];

    constructor(private app: AppService, private http: HttpClient, private router: Router) {}

    ngOnInit() {
        this.http.get<Role[]>('roles/all').subscribe(data => this.roles = data);
    }

    accept(role: Role) {
        this.roles.splice(this.roles.indexOf(role, 0), 1);
        this.http.post<Boolean>('roles/accept', role, httpOptions).subscribe(data => {
            window.alert("Success");
        },
        error => {
            window.alert(error.error.message);
        })
    }

    decline(role: Role) {
        this.roles.splice(this.roles.indexOf(role, 0), 1);
        this.http.post<Boolean>('roles/decline', role, httpOptions).subscribe(data => {
            window.alert("Success");
        },
        error => {
            window.alert(error.error.message);
        })
    }

}