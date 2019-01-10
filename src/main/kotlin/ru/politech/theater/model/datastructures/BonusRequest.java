package ru.politech.theater.model.datastructures;

public class BonusRequest {
    private final User actor;
    private final Bonus bonus;


    public BonusRequest(User actor, Bonus bonus) {
        this.actor = actor;
        this.bonus = bonus;
    }

    public User getActor() {
        return actor;
    }

    public Bonus getBonus() {
        return bonus;
    }

    @Override
    public String toString() {
        return actor.toString() + " сумма: " + bonus.getAmount();
    }
}
