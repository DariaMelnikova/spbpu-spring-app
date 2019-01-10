package ru.politech.theater.model.datastructures;

public class Role {
    private final int id;

    private final String description;

    private final int performanceId;

    private final boolean accepted;

    private final int actorId;

    public Role(int id, String description, int performanceId, boolean accepted, int actorId) {
        this.id = id;
        this.actorId = actorId;
        this.accepted = accepted;
        this.description = description;
        this.performanceId = performanceId;
    }

    public int getId() {
        return id;
    }

    public int getPerformanceId() {
        return performanceId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public int getActorId() {
        return actorId;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
