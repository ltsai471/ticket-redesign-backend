package com.ticket.ticket_system.entity;

public class Seat {
    private Long campaignId;
    private String area;
    private int seat_row;
    private int seat_column;
    private int price;
    private String status;
    private Long id;

    public Seat(Long campaignId, String area, int seat_row, int seat_column, int price, String status, Long id) {
        this.campaignId = campaignId;
        this.area = area;
        this.seat_row = seat_row;
        this.seat_column = seat_column;
        this.price = price;
        this.status = status;
        this.id = id;
    }

    // Getters and Setters
    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getSeat_row() {
        return seat_row;
    }

    public void setSeat_row(int seat_row) {
        this.seat_row = seat_row;
    }

    public int getSeat_column() {
        return seat_column;
    }

    public void setSeat_column(int seat_column) {
        this.seat_column = seat_column;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
