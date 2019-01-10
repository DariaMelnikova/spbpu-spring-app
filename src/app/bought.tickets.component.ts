import { Component, OnInit } from '@angular/core';
import { AppService } from './app.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

import { Play } from "./play";
import { User } from './user';
import { Observable } from 'rxjs';
import { Ticket } from './ticket';

const httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
};

@Component({
    templateUrl: './bought.tickets.component.html'
})
export class BoughtTicketsComponent {

    tickets: Ticket[];

    constructor(private app: AppService, private http: HttpClient, private router: Router) {}

    ngOnInit() {
        this.http.get<Ticket[]>('tickets/bought').subscribe(data => this.tickets = data);
    }

    requestReturn(ticket: Ticket) {
        this.tickets.splice(this.tickets.indexOf(ticket, 0), 1);
        this.http.post<Ticket>('tickets/return', ticket, httpOptions).subscribe(data => {
            window.alert("Success");
        },
        error => {
            window.alert(error.error.message);
        })
    }

}