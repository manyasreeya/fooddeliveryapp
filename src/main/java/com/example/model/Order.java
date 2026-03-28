package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private double price;
    private int quantity;
    private String imageUrl; // Added for beautiful cart display

    // New Fields for Payment Tracking
    private String customerName;
    private String orderStatus; // e.g., PENDING, PAID
    private String trackingId;
    private java.time.LocalDateTime orderTime;

    public Long getId() { return id; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public String getTrackingId() { return trackingId; }
    public void setTrackingId(String trackingId) { this.trackingId = trackingId; }

    public java.time.LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(java.time.LocalDateTime orderTime) { this.orderTime = orderTime; }
}