import { Play } from "./play";
import { Ticket } from "./ticket";

export class UpcomingPlay {
    constructor(
        public play: Play,
        public tickets: Ticket[]
    ){}
}