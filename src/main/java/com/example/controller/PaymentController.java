package com.example.controller;

import com.example.dto.CheckoutRequestDTO;
import com.example.model.Order;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class PaymentController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HomeController homeController; // To access the cart session data trivially

    @GetMapping("/payment")
    public String paymentPage(@RequestParam String orderId, 
                              @RequestParam double amount, 
                              @RequestParam String customerName,
                              Model model) {
        
        model.addAttribute("orderId", orderId);
        model.addAttribute("amount", amount);
        model.addAttribute("customerName", customerName);
        return "payment";
    }

    @PostMapping("/process-payment")
    public String processPayment(@RequestParam String orderId, 
                                 @RequestParam String customerName) {
        
        // Grab cart from HomeController directly for this simple session prototype
        // In full production, this would use Spring Session or a Database Cart table
        if (!homeController.getCart().isEmpty()) {
            orderService.processPaymentAndSaveOrders(homeController.getCart(), customerName, orderId);
            homeController.getCart().clear(); // Clear local session cart
        }
        
        return "redirect:/?success=true";
    }
}
