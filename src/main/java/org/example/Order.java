package org.example;

public class Order {
    private long id; // id of order
    private double price;
    private char side; // B "Bid" or O "Offer"
    private long size;
    private long timestamp;

    public Order(long id, double price, char side, long size, long timestamp) {
        this.id = id;
        this.price = price;
        this.side = side;
        this.size = size;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public char getSide() {
        return side;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
