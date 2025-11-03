package com.example.loginapp.controller;

import com.example.loginapp.model.User;
import com.example.loginapp.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    // Show login page (GET)
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        logger.info("Loading login page");
        return "login";
    }

    // Handle login (POST)
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        logger.info("Login attempt for username: {}", username);

        User user = userService.validateUser(username, password);
        if (user != null) {
            logger.info("Login successful for username: {}", username);
            model.addAttribute("username", username);
            return "home";
        } else {
            logger.info("Login failed for username: {}", username);
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    // Show signup page
    @GetMapping("/signup")
    public String showSignupPage() {
        logger.info("Loading signup page");
        return "signup";
    }

    // Handle signup
    @PostMapping("/signup")
    public String signup(@RequestParam String username,
                         @RequestParam String password,
                         Model model) {
        logger.info("Signup attempt for username: {}", username);

        boolean exists = userService.existsByUsername(username);
        if (exists) {
            logger.info("Username already exists: {}", username);
            model.addAttribute("message", "Username already exists!");
            return "signup";
        }

        userService.registerUser(username, password);
        logger.info("Signup successful for username: {}", username);
        model.addAttribute("message", "Signup successful! Please login.");
        return "login";
    }
}

