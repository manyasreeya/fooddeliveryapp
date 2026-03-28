package com.example.service;

import com.example.model.Order;
import com.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void processPaymentAndSaveOrders(List<Order> cart, String customerName, String transactionId) {
        if (!cart.isEmpty() && customerName != null && !customerName.isEmpty()) {
            for (Order order : cart) {
                order.setCustomerName(customerName);
                order.setTrackingId(transactionId);
                order.setOrderStatus("PAID");
                order.setOrderTime(java.time.LocalDateTime.now());
            }
            orderRepository.saveAll(cart);
        }
    }
    
    public double calculateTotal(List<Order> cart) {
        double total = 0;
        for (Order order : cart) {
            total += (order.getPrice() * order.getQuantity());
        }
        return total;
    }
}
