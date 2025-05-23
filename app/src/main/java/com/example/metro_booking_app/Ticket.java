package com.example.metro_booking_app;

import java.io.Serializable;

public class Ticket implements Serializable {
    private int id;
    private String ticketType;
    private String from;
    private String to;
    private String date;
    private String start;
    private String end;
    private double price;

    // Getter
    public int getId() { return id; }
    public String getTicketType() { return ticketType; }
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getDate() { return date; }
    public String getStart() { return start; }
    public String getEnd() { return end; }
    public double getPrice() { return price; }

    // Setter
    public void setId(int id) { this.id = id; }
    public void setTicketType(String ticketType) { this.ticketType = ticketType; }
    public void setFrom(String from) { this.from = from; }
    public void setTo(String to) { this.to = to; }
    public void setDate(String date) { this.date = date; }
    public void setStart(String start) { this.start = start; }
    public void setEnd(String end) { this.end = end; }
    public void setPrice(double price) { this.price = price; }
}
