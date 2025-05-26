package com.ticket.ticket_system.entity;


public class Seat {
    private String campaignId;
    private String area;
    private int row;
    private int column;
    private int price;
    private String status;
    private String id;

    public Seat(String campaignId, String area, int row, int column, int price, String status, String id) {
        this.campaignId = campaignId;
        this.area = area;
        this.row = row;
        this.column = column;
        this.price = price;
        this.status = status;
        this.id = id;
    }

    // Getters and Setters
    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
