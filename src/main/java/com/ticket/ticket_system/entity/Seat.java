package com.ticket.ticket_system.entity;

public class Seat {
    private Long campaignId;
    private String area;
    private int row;
    private int column;
    private int price;
    private String status;
    private Long id;

    public Seat(Long campaignId, String area, int row, int column, int price, String status, Long id) {
        this.campaignId = campaignId;
        this.area = area;
        this.row = row;
        this.column = column;
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

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
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
