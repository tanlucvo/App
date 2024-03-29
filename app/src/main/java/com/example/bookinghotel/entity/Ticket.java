package com.example.bookinghotel.entity;

public class Ticket {
    Long begin;
    Long end;
    Long price;
    String path;
    Boolean status;
    String pathRoom;
    public Ticket() {
    }

    public Ticket(Long begin, Long end, Long price, String path, boolean status, String pathRoom) {
        this.begin = begin;
        this.end = end;
        this.price = price;
        this.path = path;
        this.status = status;
        this.pathRoom = pathRoom;
    }

    public String getPathRoom() {
        return pathRoom;
    }

    public void setPathRoom(String pathRoom) {
        this.pathRoom = pathRoom;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getBegin() {
        return begin;
    }

    public void setBegin(Long begin) {
        this.begin = begin;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "begin=" + begin +
                ", end=" + end +
                ", price=" + price +
                ", path='" + path + '\'' +
                ", status=" + status +
                ", partroom=" + pathRoom +
                '}';
    }
}
