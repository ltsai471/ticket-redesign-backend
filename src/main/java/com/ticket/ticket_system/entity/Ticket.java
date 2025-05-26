package com.ticket.ticket_system.entity;

import java.util.Date;

public class Ticket {
    private String id;
    private String userId;
    private String seatId;
    private boolean paid;
    private Date creationDate;

    public Ticket(String id, String userId, String seatId, boolean paid, Date creationDate) {
        this.id = id;
        this.userId = userId;
        this.seatId = seatId;
        this.paid = paid;
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
