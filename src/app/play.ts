import { User } from './user';
import { Role } from './role';

export class Play {
    constructor(
        public id: string,
        public name: string,
        public description: string,
        public date: string,
        public ticketPrice: number,
        public director: User,
        public roles: Role[]
    ) {}
}