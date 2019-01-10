package ru.politech.theater.model.datastructures;

public class Ticket {
    private final int id;

    private final int place;

    private final float price;

    private final long dateMilli;

    private final int performanceId;

    private final TicketStatus ticketStatus;

    public Ticket(int id, int place, float price, long dateMilli, int performanceId, TicketStatus ticketStatus) {
        this.id = id;
        this.place = place;
        this.price = price;
        this.dateMilli = dateMilli;
        this.performanceId = performanceId;
        this.ticketStatus = ticketStatus;
    }

    public int getId() {
        return id;
    }

    public int getPlace() {
        return place;
    }

    public float getPrice() {
        return price;
    }

    public long getDateMilli() {
        return dateMilli;
    }

    public int getPerformanceId() {
        return performanceId;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    @Override
    public String toString() {
        return Integer.toString(place);
    }
}
