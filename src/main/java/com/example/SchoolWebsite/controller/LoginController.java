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

    // Handle login form submission (role detected automatically by backend)
    @PostMapping("/login")
    public String handleLogin(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String password,
            HttpSession session,
            Model model) {

        if (userId == null || userId.trim().isEmpty()) {
            model.addAttribute("error", "Please enter your ID.");
            return "login";
        }

        String rawId = userId.trim();
        String upperId = rawId.toUpperCase();

        // 1. Admin authentication check
        if (ADMIN_ID.equalsIgnoreCase(rawId)) {
            if (password == null || password.trim().isEmpty()) {
                model.addAttribute("error", "Admin password is required.");
                model.addAttribute("userId", rawId);
                model.addAttribute("showPassword", true);
                return "login";
            }
            if (ADMIN_PASSWORD.equals(password)) {
                session.setAttribute("userId", ADMIN_ID);
                session.setAttribute("role", "admin");
                return "redirect:/admin/students";
            } else {
                model.addAttribute("error", "Invalid admin credentials.");
                model.addAttribute("userId", rawId);
                model.addAttribute("showPassword", true);
                return "login";
            }
        }

        // 2. Student check
        String studentId = statsRepository.studentExists(rawId) ? rawId :
                          (statsRepository.studentExists(upperId) ? upperId : null);
        if (studentId != null) {
            session.setAttribute("userId", studentId);
            session.setAttribute("role", "student");
            return "redirect:/student/profile";
        }

        // 3. Teacher check
        String teacherId = statsRepository.teacherExists(rawId) ? rawId :
                          (statsRepository.teacherExists(upperId) ? upperId : null);
        if (teacherId != null) {
            session.setAttribute("userId", teacherId);
            session.setAttribute("role", "teacher");
            return "redirect:/teacher/profile";
        }

        // 4. Staff check
        String staffId = statsRepository.staffExists(rawId) ? rawId :
                        (statsRepository.staffExists(upperId) ? upperId : null);
        if (staffId != null) {
            session.setAttribute("userId", staffId);
            session.setAttribute("role", "staff");
            return "redirect:/staff/profile";
        }

        // ID not found in any table
        model.addAttribute("error", "No account found matching ID: " + rawId);
        model.addAttribute("userId", rawId);
        return "login";
    }

    // Logout — clear session and go home
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
