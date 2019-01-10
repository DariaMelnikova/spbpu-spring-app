package ru.politech.theater.model.datastructures;

import java.util.Objects;

public class Performance {
    private int id;

    private final String name;

    private final String description;

    private final long dateMilli;

    private final int directorId;

    public Performance(int id, String name, String description, long dateMilli, int directorId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateMilli = dateMilli;
        this.directorId = directorId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getDateMilli() {
        return dateMilli;
    }

    public int getDirectorId() {
        return directorId;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Performance that = (Performance) o;
        return id == that.id &&
                dateMilli == that.dateMilli &&
                directorId == that.directorId &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, dateMilli, directorId);
    }

    @Override
    public String toString() {
        return name;
    }
}
