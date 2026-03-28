package com.example.controller;

import com.example.model.User;
import com.example.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email, 
                               @RequestParam String password, 
                               Model model, 
                               HttpSession session) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            session.setAttribute("loggedInUser", userOptional.get());
            return "redirect:/";
        }
        
        model.addAttribute("error", "Invalid email or password.");
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/";
        }
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@RequestParam String name, 
                                @RequestParam String email, 
                                @RequestParam String password, 
                                Model model,
                                HttpSession session) {
        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "Email already in use.");
            return "signup";
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password); // Note: Simple raw password for simulation, BCrypt would be used in prod
        
        userRepository.save(user);
        
        // Auto login after signup
        session.setAttribute("loggedInUser", user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}
