package com.example.controller;

import com.example.model.Order;
import com.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Sort;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/admin/orders")
    public String viewOrders(Model model) {
        // Fetch orders, sorting by id or orderTime descending to see newest first
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        
        model.addAttribute("orders", orders);
        return "orders";
    }
}
