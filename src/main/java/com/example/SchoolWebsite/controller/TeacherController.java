package com.example.SchoolWebsite.controller;

import com.example.SchoolWebsite.model.ScheduleItem;
import com.example.SchoolWebsite.model.Student;
import com.example.SchoolWebsite.model.Teacher;
import com.example.SchoolWebsite.service.StudentService;
import com.example.SchoolWebsite.service.TeacherService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;
    private final StudentService studentService;

    public TeacherController(TeacherService teacherService, StudentService studentService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || !"teacher".equals(role)) {
            return "redirect:/login";
        }

        Teacher teacher = teacherService.getFullProfile(userId);
        model.addAttribute("teacher", teacher);
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", userId);
        model.addAttribute("loggedInRole", role);

        return "teacher/profile";
    }

    @GetMapping("/schedule")
    public String showSchedule(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || !"teacher".equals(role)) {
            return "redirect:/login";
        }

        Teacher teacher = teacherService.getFullProfile(userId);
        List<ScheduleItem> schedule = teacherService.getSchedule(userId);

        model.addAttribute("teacher", teacher);
        model.addAttribute("schedule", schedule);
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", userId);
        model.addAttribute("loggedInRole", role);

        return "teacher/schedule";
    }

    @GetMapping("/students")
    public String showStudents(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || !"teacher".equals(role)) {
            return "redirect:/login";
        }

        Teacher teacher = teacherService.getFullProfile(userId);
        boolean isClassTeacher = teacher.getClassTeacherStandard() != null && teacher.getClassTeacherDivision() != null;

        List<Student> students;
        if (isClassTeacher) {
            // Only show students of his/her class if he/she is the class teacher
            students = studentService.findByStandardAndDivision(
                    teacher.getClassTeacherStandard(),
                    teacher.getClassTeacherDivision()
            );
        } else {
            students = List.of();
        }

        model.addAttribute("teacher", teacher);
        model.addAttribute("isClassTeacher", isClassTeacher);
        model.addAttribute("students", students);
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", userId);
        model.addAttribute("loggedInRole", role);

        return "teacher/students";
    }

    @GetMapping("/course-students")
    public String showCourseStudents(
            @RequestParam String subjectCode,
            @RequestParam String standard,
            @RequestParam String division,
            HttpSession session,
            Model model) {

        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || !"teacher".equals(role)) {
            return "redirect:/login";
        }

        Teacher teacher = teacherService.getFullProfile(userId);
        String subjectName = teacherService.getSubjectName(subjectCode);
        List<Student> enrolledStudents = teacherService.getEnrolledStudentsForCourse(subjectCode, standard, division);

        model.addAttribute("teacher", teacher);
        model.addAttribute("subjectCode", subjectCode);
        model.addAttribute("subjectName", subjectName);
        model.addAttribute("standard", standard);
        model.addAttribute("division", division);
        model.addAttribute("enrolledStudents", enrolledStudents);
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", userId);
        model.addAttribute("loggedInRole", role);

        return "teacher/course-students";
    }

    @GetMapping("/salary")
    public String showSalary(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || !"teacher".equals(role)) {
            return "redirect:/login";
        }

        Teacher teacher = teacherService.getFullProfile(userId);
        List<com.example.SchoolWebsite.model.Payment> salaryPayments = teacherService.getSalaryHistory(userId);

        model.addAttribute("teacher", teacher);
        model.addAttribute("salaryPayments", salaryPayments);
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", userId);
        model.addAttribute("loggedInRole", role);

        return "teacher/salary";
    }
}
