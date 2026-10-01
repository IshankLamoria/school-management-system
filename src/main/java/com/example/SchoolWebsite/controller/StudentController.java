package com.example.SchoolWebsite.controller;

import com.example.SchoolWebsite.model.Enrollment;
import com.example.SchoolWebsite.model.Guardian;
import com.example.SchoolWebsite.model.Payment;
import com.example.SchoolWebsite.model.Student;
import com.example.SchoolWebsite.service.EnrollmentService;
import com.example.SchoolWebsite.service.GuardianService;
import com.example.SchoolWebsite.service.PaymentService;
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
    private final EnrollmentService enrollmentService;
    private final PaymentService paymentService;

    public StudentController(StudentService studentService,
                             GuardianService guardianService,
                             EnrollmentService enrollmentService,
                             PaymentService paymentService) {
        this.studentService = studentService;
        this.guardianService = guardianService;
        this.enrollmentService = enrollmentService;
        this.paymentService = paymentService;
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

        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", userId);
        model.addAttribute("loggedInRole", role);

        return "student/profile";
    }

    @GetMapping("/subjects")
    public String showSubjects(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || !"student".equals(role)) {
            return "redirect:/login";
        }

        Student student = studentService.findByAdmissionNo(userId);
        List<Enrollment> enrollments = enrollmentService.getEnrolledSubjects(userId);

        model.addAttribute("student", student);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", userId);
        model.addAttribute("loggedInRole", role);

        return "student/subjects";
    }

    @GetMapping("/fees")
    public String showFees(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || !"student".equals(role)) {
            return "redirect:/login";
        }

        Student student = studentService.findByAdmissionNo(userId);
        List<Payment> payments = paymentService.getStudentPayments(userId);

        model.addAttribute("student", student);
        model.addAttribute("payments", payments);
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", userId);
        model.addAttribute("loggedInRole", role);

        return "student/fees";
    }
}
