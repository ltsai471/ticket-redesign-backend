package com.ticket.ticket_system.entity;

import java.util.Date;

public class Ticket {
    private Long id;
    private Long userId;
    private Long seatId;
    private boolean paid;
    private Date creationDate;
    private Date cancelDate;

    public Ticket(Long id, Long userId, Long seatId, boolean paid, Date creationDate) {
        this.id = id;
        this.userId = userId;
        this.seatId = seatId;
        this.paid = paid;
        this.creationDate = creationDate;
        this.cancelDate = cancelDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
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

    public Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }
}
