package ru.politech.theater.model.datastructures;

public class Bonus {
    private final int id;

    private final int actorId;

    private final float amount;

    public Bonus(int id, int actorId, float amount) {
        this.id = id;
        this.actorId = actorId;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public int getActorId() {
        return actorId;
    }

    public float getAmount() {
        return amount;
    }
}
