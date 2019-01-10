package ru.politech.theater.model.mappers;

import ru.politech.theater.model.datastructures.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketMapper extends AbstractMapper<Ticket> {

    @Override
    public void add(Ticket ticket) {
        String sql = "INSERT INTO Ticket(place, price, date, performance_id, ticket_status_id) VALUES(?,?,?,?,?)";

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, ticket.getPlace());
            preparedStatement.setFloat(2, ticket.getPrice());
            preparedStatement.setLong(3, ticket.getDateMilli());
            preparedStatement.setInt(4, ticket.getPerformanceId());
            preparedStatement.setInt(5, ticket.getTicketStatus().ordinal());

            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("Insertion of TicketBody " + ticket.toString() + " failed!");
            throw new IllegalStateException("Не удалось создать билет!");
        }
    }

    @Override
    public Ticket get(int id) {
        return null;
    }

    @Override
    public List<Ticket> getAll() {
        return null;
    }

    public void update(Ticket ticket) {
        String sql = "UPDATE Ticket SET place = ?, price = ?, date = ?, performance_id = ?, ticket_status_id = ? WHERE id = ?";

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, ticket.getPlace());
            preparedStatement.setFloat(2, ticket.getPrice());
            preparedStatement.setLong(3, ticket.getDateMilli());
            preparedStatement.setInt(4, ticket.getPerformanceId());
            preparedStatement.setInt(5, ticket.getTicketStatus().ordinal());
            preparedStatement.setInt(6, ticket.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("ERROR: Can't update ticket " + ticket.getId());
            throw new IllegalStateException("Неполучилось обновить данные билета!");
        }
    }

    public List<Ticket> getAvailableTicketsForPerformance(int performanceId) {
        String sql =
                "SELECT id, place, price, date, performance_id, ticket_status_id " +
                        "FROM Ticket " +
                        "WHERE performance_id = ? AND ticket_status_id = ?";
        List<Ticket> tickets = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, performanceId);
            preparedStatement.setInt(2, TicketStatus.AVAILABLE.ordinal());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tickets.add(mapTicket(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Can't fetch performances for performance with id " + performanceId + "!");
            throw new IllegalStateException("Невозможно получить доступные билеты для выбранного представления");
        }

        return tickets;
    }

    public void buyTicket(Ticket ticket, User buyer) {
        update(
                new Ticket(
                        ticket.getId(),
                        ticket.getPlace(),
                        ticket.getPrice(),
                        ticket.getDateMilli(),
                        ticket.getPerformanceId(),
                        TicketStatus.SOLD
                )
        );

        String sql = "INSERT INTO BoughtTickets(user_id, ticket_id) VALUES(?, ?)";
        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, buyer.getId());
            preparedStatement.setInt(2, ticket.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Can't buy ticket " + ticket.getId() + " for user " + buyer.toString());
            throw new IllegalStateException("Неполучилось купить билет!");
        }
    }

    public List<Ticket> getSoldAndReturnRequstedTicketsAfterDate(long date) {
        String sql = "SELECT t.id, t.place, t.price, t.date, t.performance_id, t.ticket_status_id " +
                "FROM Ticket t " +
                "LEFT JOIN BoughtTickets bt ON t.id = bt.ticket_id " +
                "WHERE t.date > ? AND t.ticket_status_id IN (?, ?)";
        List<Ticket> tickets = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setLong(1, date);
            preparedStatement.setInt(2, TicketStatus.SOLD.ordinal());
            preparedStatement.setInt(3, TicketStatus.RETURN_REQUESTED.ordinal());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tickets.add(mapTicket(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Can't fetch tickets with sold and return statuses");
            throw new IllegalStateException("Невозможно получить билеты со статусом SOLD и RETURN_REQUESTED");
        }

        return tickets;
    }

    public List<Ticket> getSoldAndApprovedUserTicketsAfterDate(User buyer, long date) {
        String sql =
                "SELECT t.id, t.place, t.price, t.date, t.performance_id, t.ticket_status_id " +
                        "FROM Ticket t " +
                        "LEFT JOIN BoughtTickets bt ON t.id = bt.ticket_id " +
                        "WHERE bt.user_id = ? AND t.date > ? AND t.ticket_status_id IN (?, ?)";
        List<Ticket> tickets = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, buyer.getId());
            preparedStatement.setLong(2, date);
            preparedStatement.setInt(3, TicketStatus.SOLD.ordinal());
            preparedStatement.setInt(4, TicketStatus.APPROVED.ordinal());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tickets.add(mapTicket(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Can't fetch tickets for user " + buyer.toString() + " after date " + date);
            throw new IllegalStateException("Невозможно получить билеты для выбранного пользователя");
        }

        return tickets;
    }

    private Ticket mapTicket(ResultSet resultSet) throws SQLException {
        return new Ticket(
                resultSet.getInt("id"),
                resultSet.getInt("place"),
                resultSet.getFloat("price"),
                resultSet.getLong("date"),
                resultSet.getInt("performance_id"),
                TicketStatus.values()[resultSet.getInt("ticket_status_id")]
        );
    }
}
