package ru.politech.theater.model.repositories;

import ru.politech.theater.model.TimeUtils;
import ru.politech.theater.model.datastructures.Ticket;
import ru.politech.theater.model.datastructures.User;
import ru.politech.theater.model.mappers.TicketMapper;

import java.util.List;

public class TicketRepository implements Repository<Ticket> {
    private TicketMapper ticketMapper = new TicketMapper();

    @Override
    public void add(Ticket item) {
        ticketMapper.add(item);
    }

    @Override
    public void update(Ticket item) {
        ticketMapper.update(item);
    }

    @Override
    public void remove(Ticket item) {

    }

    @Override
    public Ticket get(int id) {
        return ticketMapper.get(id);
    }

    @Override
    public List<Ticket> query() {
        return null;
    }

    public List<Ticket> getAvailableTicketsForPerformance(int performanceId) {
        return ticketMapper.getAvailableTicketsForPerformance(performanceId);
    }

    public void buyTicket(Ticket ticket, User buyer) {
        ticketMapper.buyTicket(ticket, buyer);
    }

    public List<Ticket> getPendingTickets() {
        return ticketMapper.getSoldAndReturnRequstedTicketsAfterDate(TimeUtils.getInstance().getTodayMilli());
    }

    public List<Ticket> getBoughtTicketsForUser(User buyer) {
        return ticketMapper.getSoldAndApprovedUserTicketsAfterDate(buyer, TimeUtils.getInstance().getTomorrowMilli());
    }

}
