package ru.politech.theater.model.datastructures;

public class BoughtTicket {
    private final Ticket ticket;
    private final Performance performance;


    public BoughtTicket(Ticket ticket, Performance performance) {
        this.ticket = ticket;
        this.performance = performance;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public Performance getPerformance() {
        return performance;
    }

    @Override
    public String toString() {
        return performance.getName() + " место: " + ticket.getPlace() + " статус: " + ticket.getTicketStatus();
    }
}
