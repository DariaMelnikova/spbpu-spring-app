import { Component } from "@angular/core";
import { AppService } from "./app.service";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from "@angular/router";
import { BonusRequest } from "./bonus.request";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json'
  })
};

@Component({
    templateUrl: './give-bonus.component.html'
  })
export class GiveBonusComponent {

    bonusRequests: BonusRequest[];

    constructor(private app: AppService, private http: HttpClient, private router: Router) {}

    ngOnInit() {
        this.http.get<BonusRequest[]>('bonus/all').subscribe(data => this.bonusRequests = data);
    }

    giveBonus(request: BonusRequest) {
        this.bonusRequests.splice(this.bonusRequests.indexOf(request, 0), 1);
        this.http.post<BonusRequest>('bonus/give', request, httpOptions).subscribe(data => {
            window.alert("Success");
        },
        error => {
            window.alert(error.error.message);
        });
    }

    refuseBonus(request: BonusRequest) {
        this.bonusRequests.splice(this.bonusRequests.indexOf(request, 0), 1);
        this.http.post<BonusRequest>('bonus/refuse', request, httpOptions).subscribe(data => {
            window.alert("Success");
        },
        error => {
            window.alert(error.error.message);
        });
    }

}