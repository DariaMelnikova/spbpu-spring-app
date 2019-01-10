package ru.politech.theater.model;

import ru.politech.theater.model.datastructures.Occupation;
import ru.politech.theater.model.datastructures.Performance;
import ru.politech.theater.model.datastructures.User;
import ru.politech.theater.model.repositories.UserRepository;

import java.util.List;

public class UserModel {
    private UserRepository userRepository = new UserRepository();

    public void addUser(
            String login,
            String firstName,
            String lastName,
            String password,
            Occupation occupation
    ) throws IllegalStateException {
        if (login == null || login.trim().equals("")) throw new IllegalStateException("Некорректный логин");
        if (firstName == null || firstName.trim().equals("")) throw new IllegalStateException("Некорректное имя");
        if (lastName == null || lastName.trim().equals("")) throw new IllegalStateException("Некорректная фамилия");
        if (password == null || password.trim().equals("")) throw new IllegalStateException("Некорректный пароль");
        if (occupation == null) throw new IllegalStateException("Некорректный род деятельности");

        userRepository.add(new User(-1, login, firstName, lastName, password, occupation));
    }

    public User getUser(int id) {
        return userRepository.get(id);
    }

    public User getUser(String login) {
        if (login == null || login.trim().equals("")) throw new IllegalStateException("Некорректный логин");

      return userRepository.getByLogin(login);
    }

    public List<User> getAllUsersWithOccupation(Occupation occupation) {
        return userRepository.getAll(occupation);
    }

    public List<User> getActorsOnPerformance(Performance performance) {
        if (performance == null) throw new IllegalStateException("Не выбран спектакль");

        return userRepository.getAllActorsInPerformance(performance.getId());
    }

    public List<User> getAvailableActorsForPerformance(Performance performance) {
        if (performance == null) throw new IllegalStateException("Не выбран спектакль");

        return userRepository.getAllActorsNotInPerformance(performance.getId());
    }
}
