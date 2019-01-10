package ru.politech.theater.model.mappers;

import ru.politech.theater.model.datastructures.Occupation;
import ru.politech.theater.model.datastructures.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserMapper extends AbstractMapper<User> {

    @Override
    public void add(User user) {
        String sql = "INSERT INTO User(login, first_name, last_name, password, occupation_id) VALUES(?, ?, ?, ?, ?)";

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setInt(5, user.getOccupation().ordinal());

            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("Insertion of User " + user.toString() + " failed!");
            throw new IllegalStateException("Логин уже используется!");
        }
    }

    @Override
    public User get(int id) {
        String sql = "SELECT id, login, first_name, last_name, password, occupation_id FROM User WHERE id = ?";

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            return mapUser(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT id, login, first_name, last_name, password, occupation_id FROM User";
        List<User> list = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(mapUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public User get(String login, String password) {
        String sql = "SELECT id, login, first_name, last_name, password, occupation_id FROM User WHERE login = ? AND password = ?";

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            return mapUser(resultSet);

        } catch (SQLException e) {
            System.out.println("ERROR: User with login " + login + " and password " + password + " not founded!");
            throw new IllegalStateException("Пара логин/пароль не найдена!");
        }
    }

  public User get(String login) {
    String sql = "SELECT id, login, first_name, last_name, password, occupation_id FROM User WHERE login = ?";

    try (
      Connection connection = DriverManager.getConnection(CONNECTION_URL);
      PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setString(1, login);

      ResultSet resultSet = preparedStatement.executeQuery();

      return mapUser(resultSet);

    } catch (SQLException e) {
      System.out.println("ERROR: User with login " + login + " not founded!");
      throw new IllegalStateException("Пользователь с данным паролем не найден!");
    }
  }

    public List<User> getAll(Occupation occupation) {
        String sql = "SELECT id, login, first_name, last_name, password, occupation_id FROM User WHERE occupation_id = ?";
        List<User> list = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, occupation.ordinal());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(mapUser(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Can't fetch users with occupation " + occupation.name() + "!");
            throw new IllegalStateException("Невозможно получить список пользователей с занятостью: " + occupation.toString());
        }

        return list;
    }

    public List<User> getAllActorsNotInPerformance(int performanceId) {
        String sql =
                "SELECT user.id, login, first_name, last_name, password, occupation_id " +
                        "FROM User user " +
                        "LEFT JOIN Role role ON user.id = role.user_id " +
                        "WHERE (user.occupation_id = ? AND (role.performance_id IS NULL OR NOT role.performance_id = ?))";
        List<User> list = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, Occupation.ACTOR.ordinal());
            preparedStatement.setInt(2, performanceId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(mapUser(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Can't fetch actors not in performance with id: " + performanceId + "!");
            throw new IllegalStateException("Невозможно получить список актёров не занятых в выбранном спектакле");
        }

        return list;
    }

    public List<User> getAllActorsInPerformance(int performanceId) {
        String sql =
                "SELECT user.id, login, first_name, last_name, password, occupation_id " +
                        "FROM User user " +
                        "LEFT JOIN Role role ON user.id = role.user_id " +
                        "WHERE (user.occupation_id = ? AND role.performance_id = ?)";
        List<User> list = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, Occupation.ACTOR.ordinal());
            preparedStatement.setInt(2, performanceId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(mapUser(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Can't fetch actors in performance with id: " + performanceId + "!");
            throw new IllegalStateException("Невозможно получить список актёров занятых в выбранном спектакле");
        }

        return list;
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("login"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("password"),
                Occupation.values()[resultSet.getInt("occupation_id")]
        );
    }

}
