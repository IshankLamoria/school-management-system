package com.example.SchoolWebsite.controller;

import com.example.SchoolWebsite.model.Payment;
import com.example.SchoolWebsite.model.Staff;
import com.example.SchoolWebsite.model.Student;
import com.example.SchoolWebsite.model.VehicleInfo;
import com.example.SchoolWebsite.service.StaffService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || !"staff".equals(role)) {
            return "redirect:/login";
        }

        Staff staff = staffService.getStaffProfile(userId);
        VehicleInfo vehicle = null;
        if (staff != null && staff.isDriver()) {
            vehicle = staffService.getAssignedVehicle(userId);
        }

        model.addAttribute("staff", staff);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", userId);
        model.addAttribute("loggedInRole", role);

        return "staff/profile";
    }

    @GetMapping("/salary")
    public String showSalary(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || !"staff".equals(role)) {
            return "redirect:/login";
        }

        Staff staff = staffService.getStaffProfile(userId);
        List<Payment> salaryPayments = staffService.getSalaryHistory(userId);

        model.addAttribute("staff", staff);
        model.addAttribute("salaryPayments", salaryPayments);
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", userId);
        model.addAttribute("loggedInRole", role);

        return "staff/salary";
    }

    @GetMapping("/transport")
    public String showTransport(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || !"staff".equals(role)) {
            return "redirect:/login";
        }

        Staff staff = staffService.getStaffProfile(userId);

        // Strict access check: only Drivers can view vehicle student lists
        if (staff == null || !staff.isDriver()) {
            return "redirect:/staff/profile";
        }

        VehicleInfo vehicle = staffService.getAssignedVehicle(userId);
        List<Student> busStudents = List.of();
        if (vehicle != null) {
            busStudents = staffService.getBusStudents(vehicle.getVehicleId());
        }

        model.addAttribute("staff", staff);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("busStudents", busStudents);
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", userId);
        model.addAttribute("loggedInRole", role);

        return "staff/transport";
    }
}
