package com.example.SchoolWebsite.controller;

import com.example.SchoolWebsite.model.Guardian;
import com.example.SchoolWebsite.model.Student;
import com.example.SchoolWebsite.service.GuardianService;
import com.example.SchoolWebsite.service.StudentService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final GuardianService guardianService;

    public StudentController(StudentService studentService, GuardianService guardianService) {
        this.studentService = studentService;
        this.guardianService = guardianService;
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        // Check if user is logged in as a student
        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || !"student".equals(role)) {
            return "redirect:/login";
        }

        // Fetch full student profile (5-table JOIN)
        Student student = studentService.getFullProfile(userId);
        model.addAttribute("student", student);

        // Fetch guardian info
        List<Guardian> guardians = guardianService.findByStudentId(userId);
        model.addAttribute("guardians", guardians);

        return "student/profile";
    }
}
