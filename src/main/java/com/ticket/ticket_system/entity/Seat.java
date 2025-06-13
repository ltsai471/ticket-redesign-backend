package com.ticket.ticket_system.entity;

public class Seat {
    private Long campaignId;
    private String area;
    private int seatRow;
    private int seatColumn;
    private int price;
    private String status;
    private Long id;

    public Seat(Long campaignId, String area, int seatRow, int seatColumn, int price, String status, Long id) {
        this.campaignId = campaignId;
        this.area = area;
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.price = price;
        this.status = status;
        this.id = id;
    }

    public Seat() {

    }

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

    public int getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(int seatRow) {
        this.seatRow = seatRow;
    }

    public int getSeatColumn() {
        return seatColumn;
    }

    public void setSeatColumn(int seatColumn) {
        this.seatColumn = seatColumn;
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
