import { Play } from "./play";

export class Ticket {
    constructor(
        public id: string,
        public place: number,
        public price: number,
        public date: string,
        public play: Play,
        public status: string
    ){}
}