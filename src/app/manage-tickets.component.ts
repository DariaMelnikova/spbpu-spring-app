import { Component } from "@angular/core";
import { AppService } from "./app.service";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from "@angular/router";
import { Ticket } from "./ticket";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json'
  })
};

@Component({
    templateUrl: './manage-tickets.component.html'
})
export class ManageTicketsComponent {

    tickets: Ticket[];

    constructor(private app: AppService, private http: HttpClient, private router: Router) {}

    ngOnInit() {
        this.http.get<Ticket[]>('tickets/all').subscribe(data => this.tickets = data);
    }

    approveTicket(request: Ticket) {
        this.tickets.splice(this.tickets.indexOf(request, 0), 1);
        this.http.post<Ticket>('tickets/approve', request, httpOptions).subscribe(data => {
            window.alert("Success");
        },
        error => {
            window.alert(error.error.message);
        });
    }

    refuseReturn(request: Ticket) {
        this.tickets.splice(this.tickets.indexOf(request, 0), 1);
        this.http.post<Ticket>('tickets/refuse', request, httpOptions).subscribe(data => {
            window.alert("Success");
        },
        error => {
            window.alert(error.error.message);
        });
    }

}