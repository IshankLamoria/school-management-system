package com.example.SchoolWebsite.controller;

import com.example.SchoolWebsite.repository.StatsRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final StatsRepository statsRepository;

    // Hard-coded admin credentials (simple approach for DBMS project)
    private static final String ADMIN_ID = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    public LoginController(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    // Show the login page (or redirect if already logged in)
    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role != null) {
            switch (role) {
                case "student":
                    return "redirect:/student/profile";
                case "teacher":
                    return "redirect:/teacher/profile";
                case "staff":
                    return "redirect:/staff/profile";
                case "admin":
                    return "redirect:/admin/students";
            }
        }
        return "login";
    }

    // Handle login form submission
    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String role,
            @RequestParam String userId,
            @RequestParam(required = false) String password,
            HttpSession session,
            Model model) {

        switch (role) {
            case "student":
                if (statsRepository.studentExists(userId)) {
                    session.setAttribute("userId", userId);
                    session.setAttribute("role", "student");
                    return "redirect:/student/profile";
                }
                model.addAttribute("error", "No student found with Admission No: " + userId);
                break;

            case "teacher":
                if (statsRepository.teacherExists(userId)) {
                    session.setAttribute("userId", userId);
                    session.setAttribute("role", "teacher");
                    return "redirect:/teacher/profile";
                }
                model.addAttribute("error", "No teacher found with Employee ID: " + userId);
                break;

            case "staff":
                if (statsRepository.staffExists(userId)) {
                    session.setAttribute("userId", userId);
                    session.setAttribute("role", "staff");
                    return "redirect:/staff/profile";
                }
                model.addAttribute("error", "No staff found with Employee ID: " + userId);
                break;

            case "admin":
                if (ADMIN_ID.equals(userId) && ADMIN_PASSWORD.equals(password)) {
                    session.setAttribute("userId", userId);
                    session.setAttribute("role", "admin");
                    return "redirect:/admin/students";
                }
                model.addAttribute("error", "Invalid admin credentials.");
                break;

            default:
                model.addAttribute("error", "Please select a role.");
        }

        model.addAttribute("selectedRole", role);
        return "login";
    }

    // Logout — clear session and go home
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
