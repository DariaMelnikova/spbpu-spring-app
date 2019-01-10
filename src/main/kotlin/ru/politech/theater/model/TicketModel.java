package ru.politech.theater.model;

import ru.politech.theater.model.datastructures.*;
import ru.politech.theater.model.repositories.PerformanceRepository;
import ru.politech.theater.model.repositories.TicketRepository;

import java.util.ArrayList;
import java.util.List;

public class TicketModel {
    private TicketRepository ticketRepository = new TicketRepository();
    private PerformanceRepository performanceRepository = new PerformanceRepository();

    public List<Ticket> getAvailableSitsForPerformance(Performance performance) {
        return ticketRepository.getAvailableTicketsForPerformance(performance.getId());
    }

    public void buyTicket(Ticket ticket, User buyer) {
        if (ticket == null) throw new IllegalStateException("Выбран некорректный билет");

        ticketRepository.buyTicket(ticket, buyer);
    }

    public void approveBuying(BoughtTicket boughtTicket) {
        updateTicketStatus(boughtTicket, TicketStatus.APPROVED);
    }

    public void requestReturn(BoughtTicket boughtTicket) {
        updateTicketStatus(boughtTicket, TicketStatus.RETURN_REQUESTED);
    }

    public void refuseReturn(BoughtTicket boughtTicket) {
        updateTicketStatus(boughtTicket, TicketStatus.SOLD);
    }

    public List<BoughtTicket> getBoughtTicketsForUser(User user) {
        List<Ticket> userTickets = ticketRepository.getBoughtTicketsForUser(user);
        List<BoughtTicket> ticketsAndPerformance = new ArrayList<>();
        userTickets.forEach(ticket -> {
            ticketsAndPerformance.add(new BoughtTicket(ticket, performanceRepository.get(ticket.getPerformanceId())));
        });

        return ticketsAndPerformance;
    }

    // Tickets with status SOLD or RETURN_REQUESTED
    public List<BoughtTicket> getPendingBoughtTickets() {
        List<Ticket> userTickets = ticketRepository.getPendingTickets();
        List<BoughtTicket> ticketsAndPerformance = new ArrayList<>();
        userTickets.forEach(ticket -> {
            ticketsAndPerformance.add(new BoughtTicket(ticket, performanceRepository.get(ticket.getPerformanceId())));
        });

        return ticketsAndPerformance;
    }

    void createTicketsForPerformance(Performance performance, float price, int amount) {
        for (int i = 1; i <= amount; ++i) {
            ticketRepository.add(
                    new Ticket(0, i, price, performance.getDateMilli(), performance.getId(), TicketStatus.AVAILABLE)
            );
        }
    }

    private void updateTicketStatus(BoughtTicket boughtTicket, TicketStatus ticketStatus) {
        if (boughtTicket == null) throw new IllegalStateException("Выбран некорректный билет");

        final Ticket ticket = boughtTicket.getTicket();
        ticketRepository.update(
                new Ticket(
                        ticket.getId(),
                        ticket.getPlace(),
                        ticket.getPrice(),
                        ticket.getDateMilli(),
                        ticket.getPerformanceId(),
                        ticketStatus
                )
        );
    }
}
