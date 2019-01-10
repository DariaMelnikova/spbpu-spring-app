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

import { UpcomingPlay } from "./upcomingplay";
import { Ticket } from './ticket';

@Component({
    templateUrl: './buyticket.component.html'
})
export class BuyTicketComponent {

    upcomingPlays: UpcomingPlay[];

    selectedPlay: UpcomingPlay;
    selectedTicket: Ticket;

    constructor(private app: AppService, private http: HttpClient, private router: Router) {}

    ngOnInit() {
        this.http.get<UpcomingPlay[]>('/tickets/upcoming').subscribe(data => this.upcomingPlays = data);
    }

    showPlayInfo(upcplay: UpcomingPlay) {
        this.selectedPlay = upcplay;
    }

    showTicketInfo(ticket: Ticket) {
        this.selectedTicket = ticket;
    }

    buyTicket() {
        this.selectedPlay.tickets.splice(this.selectedPlay.tickets.indexOf(this.selectedTicket, 0), 1);
        this.http.post<Ticket>('tickets/buy', this.selectedTicket, httpOptions).subscribe(data => {
            window.alert("Success");
        },
        error => {
            window.alert(error.error.message);
        })
    }

}