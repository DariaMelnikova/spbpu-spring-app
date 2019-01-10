import { Play } from "./play";
import { User } from "./user";

export class DirectorPlay {
    constructor(
        public play: Play,
        public actors: User[]
    ){}
}