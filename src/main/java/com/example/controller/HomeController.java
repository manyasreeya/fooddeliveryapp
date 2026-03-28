package com.example.controller;

import com.example.model.FoodItem;
import com.example.model.Order;
import com.example.service.FoodService;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.example.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private OrderService orderService;

    private List<Order> cart = new ArrayList<>();

    // Expose cart for PaymentController
    public List<Order> getCart() {
        return cart;
    }

    @GetMapping("/")
    public String home(@RequestParam(required = false) String success, Model model, HttpSession session) {
        // Robust Fallback Pre-loader in case database is empty
        foodService.seedDatabaseIfEmpty();

        model.addAttribute("items", foodService.getAllFoodItems());
        model.addAttribute("cartCount", cart.size());
        
        if (session.getAttribute("loggedInUser") != null) {
            model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        }
        
        if (success != null) {
            model.addAttribute("successMessage", "Payment Successful! Your order is being prepared.");
        }
        
        return "index";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam String name,
                           @RequestParam double price,
                           @RequestParam(required = false) String imageUrl) {

        Order order = new Order();
        order.setItemName(name);
        order.setPrice(price);
        order.setQuantity(1);
        order.setImageUrl(imageUrl); // Track image for beautiful cart

        cart.add(order);
        return "redirect:/";
    }

    @GetMapping("/cart")
    public String cart(Model model, HttpSession session) {
        double totalAmount = orderService.calculateTotal(cart);
        
        model.addAttribute("cart", cart);
        model.addAttribute("total", totalAmount);
        model.addAttribute("cartCount", cart.size());
        
        if (session.getAttribute("loggedInUser") != null) {
            model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        }
        
        return "cart";
    }

    @PostMapping("/place-order")
    public String placeOrder(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
    
        if (!cart.isEmpty()) {
            double totalAmount = orderService.calculateTotal(cart);
            String orderId = UUID.randomUUID().toString();
            
            // Redirect to the simulated Payment Gateway
            return "redirect:/payment?amount=" + totalAmount + "&orderId=" + orderId + "&customerName=" + user.getName();
        }
        return "redirect:/cart";
    }
}