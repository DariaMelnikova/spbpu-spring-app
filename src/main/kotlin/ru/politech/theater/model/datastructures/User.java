package ru.politech.theater.model.datastructures;

public class User {
    private final int id;

    private final String login;

    private final String firstName;

    private final String lastName;

    private final String password;

    private final Occupation occupation;

    public User(int id, String login, String firstName, String lastName, String password, Occupation occupation) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.occupation = occupation;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
