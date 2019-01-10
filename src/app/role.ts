import { User } from "./user";
import { Play } from './play';

export class Role {
    constructor(
        public id: string,
        public description: string,
        public actor: User,
        public play: Play
    ){}
}