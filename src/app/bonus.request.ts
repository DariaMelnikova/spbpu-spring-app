import { User } from "./user";
import { Bonus } from "./bonus";

export class BonusRequest {
    constructor(
        public actor: User,
        public bonus: Bonus
    ) {}
}