package ru.politech.theater.model;

import ru.politech.theater.model.datastructures.Occupation;
import ru.politech.theater.model.datastructures.Performance;
import ru.politech.theater.model.datastructures.User;
import ru.politech.theater.model.repositories.PerformanceRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class PerformanceModel {
    private PerformanceRepository performanceRepository = new PerformanceRepository();

    private TicketModel ticketModel = new TicketModel();

    public void addPerformance(String name, String description, User director, long dateMilli, float price) {
        if (name == null || name.trim().equals("")) throw new IllegalStateException("Некорректное название спектакля");
        if (description == null) throw new IllegalStateException("Некорректное описание");
        if (director == null) throw new IllegalStateException("Некорректный режисёр");
        if (dateMilli < LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toEpochSecond()) throw new IllegalStateException("Некорректная дата");
        if (price <= 0) throw new IllegalStateException("Некорректная цена билета");

        Performance newPerformance = new Performance(-1, name, description, dateMilli, director.getId());
        performanceRepository.add(newPerformance);

        ticketModel.createTicketsForPerformance(newPerformance, price, 20);
    }

    public List<Performance> getAllPastPerformances() {
        return performanceRepository.getAllPastPerformances(TimeUtils.getInstance().getTodayMilli());
    }

    public List<Performance> getAllUpcomingPerformancesWithAvailableTickets() {
        return performanceRepository.getAllAfterDateWithAvailableTickets(TimeUtils.getInstance().getTomorrowMilli());
    }

    public List<Performance> getAllUpcomingPerformancesWithAvailableTicketsAtDate(long date) {
        if (date < TimeUtils.getInstance().getTodayMilli()) throw new IllegalStateException("Неверная дата");

        return performanceRepository.getAllWithAvailableTicketsAtDate(date);
    }

    public List<Performance> getAllUpcomingPerformancesForDirector(User director) {
        if (director == null || director.getOccupation() != Occupation.DIRECTOR) throw new IllegalStateException("Не выбран режисёр");

        return performanceRepository
                .getAllAfterDateForDirector(
                        TimeUtils.getInstance().getTomorrowMilli(),
                        director.getId()
                );
    }

}
