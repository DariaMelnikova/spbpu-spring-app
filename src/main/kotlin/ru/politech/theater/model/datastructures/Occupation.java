package ru.politech.theater.model.datastructures;

public enum Occupation {
    ACTOR("Актёр"),
    VIEWER("Зритель"),
    DIRECTOR("Режисёр"),
    ADMINISTRATOR("Администратор");

    private final String label;

    Occupation(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
