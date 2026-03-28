package com.example.dto;

public class CheckoutRequestDTO {
    
    private String customerName;
    private double totalAmount;
    
    // Getters and Setters
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
