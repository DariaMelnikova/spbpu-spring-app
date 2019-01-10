package ru.politech.theater.model.mappers;


import ru.politech.theater.model.datastructures.Performance;
import ru.politech.theater.model.datastructures.TicketStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerformanceMapper extends AbstractMapper<Performance> {
    @Override
    public void add(Performance performance) {
        String sql = "INSERT INTO Performance(name, description, date, user_id) VALUES(?, ?, ?, ?)";

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, performance.getName());
            preparedStatement.setString(2, performance.getDescription());
            preparedStatement.setLong(3, performance.getDateMilli());
            preparedStatement.setInt(4, performance.getDirectorId());

            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                performance.setId(resultSet.getInt(1));
            } else {
                throw new IllegalStateException("Невозможно получить сгенерированный id созданного спектакля");
            }

        } catch (SQLException e) {
            System.out.println("Insertion of Performance " + performance.toString() + " failed!");
            throw new IllegalStateException("Невозможно создать данный спектакль!");
        }
    }

    @Override
    public Performance get(int id) {
        String sql = "SELECT id, name, description, date, user_id FROM Performance WHERE id = ?";
        Performance performance = null;

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                performance = mapPerformance(resultSet);
            } else {
                throw new SQLException("Performance with id: " + id + " not founded!");
            }

        } catch (SQLException e) {
            System.out.println("Insertion of Performance " + performance.toString() + " failed!");
            throw new IllegalStateException("Невозможно создать данный спектакль!");
        }

        return performance;
    }

    @Override
    public List<Performance> getAll() {
        return null;
    }

    public List<Performance> getAllPastPerformances(long date) {
        String sql = "SELECT id, name, description, date, user_id FROM Performance WHERE date < ?";
        List<Performance> list = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setLong(1, date);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(mapPerformance(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Can't fetch performances before date " + date + "!");
            throw new IllegalStateException("Невозможно получить список спектаклей до выбранной даты");
        }

        return list;
    }

    public List<Performance> getAllAfterDateWithAvailableTickets(long date) {
        String sql =
                "SELECT DISTINCT p.id, p.name, p.description, p.date, p.user_id " +
                        "FROM Performance p " +
                        "LEFT JOIN Ticket t ON p.id = t.performance_id " +
                        "WHERE p.date > ? AND t.ticket_status_id = ?";
        List<Performance> list = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setLong(1, date);
            preparedStatement.setInt(2, TicketStatus.AVAILABLE.ordinal());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(mapPerformance(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Can't fetch performances after date " + date + "!");
            throw new IllegalStateException("Невозможно получить список будущих спектаклей");
        }

        return list;
    }

    public List<Performance> getAllWithAvailableTicketsAtDate(long date) {
        String sql =
                "SELECT DISTINCT p.id, p.name, p.description, p.date, p.user_id " +
                "FROM Performance " +
                "LEFT JOIN Ticket t ON p.id = t.performance_id " +
                "WHERE p.date = ? AND t.ticket_status_id = ?";
        List<Performance> list = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setLong(1, date);
            preparedStatement.setInt(2, TicketStatus.AVAILABLE.ordinal());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(mapPerformance(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Can't fetch performances with date " + date + "!");
            throw new IllegalStateException("Невозможно получить список спектаклей для выбранной даты");
        }

        return list;
    }

    public List<Performance> getAllAfterDateForDirector(long date, int directorId) {
        String sql = "SELECT id, name, description, date, user_id FROM Performance WHERE user_id = ? AND date > ?";
        List<Performance> list = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, directorId);
            preparedStatement.setLong(2, date);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(mapPerformance(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Can't fetch performances for director with id " + directorId + "!");
            throw new IllegalStateException("Невозможно получить список спектаклей для выбранного режисёра");
        }

        return list;
    }

    private Performance mapPerformance(ResultSet resultSet) throws SQLException {
        return new Performance(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getLong("date"),
                resultSet.getInt("user_id")
        );
    }
}
