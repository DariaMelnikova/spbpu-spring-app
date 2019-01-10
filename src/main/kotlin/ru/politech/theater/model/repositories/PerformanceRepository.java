package ru.politech.theater.model.repositories;

import ru.politech.theater.model.datastructures.Performance;
import ru.politech.theater.model.mappers.PerformanceMapper;

import java.util.List;

public class PerformanceRepository implements Repository<Performance> {
    private PerformanceMapper performanceMapper = new PerformanceMapper();

    @Override
    public void add(Performance performance) {
        performanceMapper.add(performance);
    }

    @Override
    public void update(Performance item) {}

    @Override
    public void remove(Performance item) {}

    @Override
    public Performance get(int id) {
        return performanceMapper.get(id);
    }

    @Override
    public List<Performance> query() {
        return null;
    }

    public List<Performance> getAllPastPerformances(long date) {
        return performanceMapper.getAllPastPerformances(date);
    }

    public List<Performance> getAllAfterDateWithAvailableTickets(long date) {
        return performanceMapper.getAllAfterDateWithAvailableTickets(date);
    }

    public List<Performance> getAllWithAvailableTicketsAtDate(long date) {
        return performanceMapper.getAllWithAvailableTicketsAtDate(date);
    }

    public List<Performance> getAllAfterDateForDirector(long date, int directorId) {
        return performanceMapper.getAllAfterDateForDirector(date, directorId);
    }
}
