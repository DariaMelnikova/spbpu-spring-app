package ru.politech.theater.model.mappers;


import ru.politech.theater.model.datastructures.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleMapper extends AbstractMapper<Role> {

    @Override
    public void add(Role role) {
        String sql = "INSERT INTO Role(description, performance_id, accepted, user_id) VALUES(?, ?, ?, ?)";

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, role.getDescription());
            preparedStatement.setInt(2, role.getPerformanceId());
            preparedStatement.setInt(3, role.isAccepted() ? 1 : 0);
            preparedStatement.setInt(4, role.getActorId());

            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("Insertion of Role " + role.toString() + " failed!");
            throw new IllegalStateException("Не удалось создать роль");
        }
    }

    @Override
    public Role get(int id) {
        return null;
    }

    @Override
    public List<Role> getAll() {
        return null;
    }

    public List<Role> getAllForPerformance(int performanceId) {
        String sql = "SELECT id, description, performance_id, accepted, user_id FROM Role WHERE performance_id = ?";
        List<Role> list = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, performanceId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(mapRole(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Role> getAllNotAcceptedForActor(int actorId) {
        String sql = "SELECT id, description, performance_id, accepted, user_id FROM Role WHERE user_id = ? AND accepted = 0";
        List<Role> list = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, actorId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(mapRole(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void acceptRole(int roleId) {
        String sql = "UPDATE Role SET accepted = 1 WHERE id = ?";

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, roleId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeRole(int roleId) {
        String sql = "DELETE FROM Role WHERE id = ?";

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, roleId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Role mapRole(ResultSet resultSet) throws SQLException {
        return new Role(
                resultSet.getInt("id"),
                resultSet.getString("description"),
                resultSet.getInt("performance_id"),
                resultSet.getInt("accepted") == 1,
                resultSet.getInt("user_id")
        );
    }
}
