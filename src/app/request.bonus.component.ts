import { Component } from "@angular/core";
import { AppService } from "./app.service";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from "@angular/router";
import { Ticket } from "./ticket";
import { DirectorPlay } from './directorplay';
import { User } from './user';
import { Play } from './play';
import { Bonus } from './bonus';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json'
  })
};

@Component({
    templateUrl: './request.bonus.component.html'
})
export class RequestBonusComponent {

    previousPlays: DirectorPlay[];

    selectedPlay: DirectorPlay;
    selectedActor: User;
    selectedPrice: number = 0;

    constructor(private app: AppService, private http: HttpClient, private router: Router) {}

    ngOnInit() {
        this.http.get<DirectorPlay[]>('plays/previous').subscribe(data => {
            this.previousPlays = data;
        });
    }

    onActorChange(actor) {
        this.selectedActor = actor;
    }

    onPlaySelected(play: DirectorPlay) {
        this.selectedPlay = play;
        this.selectedActor = play.actors[0];
        this.selectedPrice = 0;
    }

    requestBonus() {
        this.http.post<Boolean>('bonus/request', new Bonus("", this.selectedActor.id, this.selectedPrice), httpOptions).subscribe(data => {
            this.selectedActor = undefined;
            this.selectedPlay = undefined;
            window.alert("Success");
        },
        error => {
            window.alert(error.error.message);
        })
    }

}