package ru.politech.theater.model.mappers;

import ru.politech.theater.model.datastructures.Bonus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BonusMapper extends AbstractMapper<Bonus> {
    @Override
    public void add(Bonus bonus) {
        String sql = "INSERT INTO Bonus(actor_id, amount) VALUES(?, ?)";

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, bonus.getActorId());
            preparedStatement.setFloat(2, bonus.getAmount());

            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("Insertion of Bonus failed!");
            throw new IllegalStateException("Невозможно создать бонус!");
        }
    }

    @Override
    public Bonus get(int id) {
        return null;
    }

    @Override
    public List<Bonus> getAll() {
        String sql = "SELECT id, actor_id, amount FROM Bonus";
        List<Bonus> list = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(mapBonus(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void remove(Bonus bonus) {
        String sql = "DELETE FROM Bonus WHERE id = ?";

        try (
                Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, bonus.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Deletion of Bonus failed!");
            throw new IllegalStateException("Невозможно удалить бонус!");
        }
    }

    private Bonus mapBonus(ResultSet resultSet) throws SQLException {
        return new Bonus(
                resultSet.getInt("id"),
                resultSet.getInt("actor_id"),
                resultSet.getFloat("amount")
        );
    }

}
