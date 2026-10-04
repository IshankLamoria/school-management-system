package com.example.SchoolWebsite.controller;

import com.example.SchoolWebsite.model.Staff;
import com.example.SchoolWebsite.model.Student;
import com.example.SchoolWebsite.model.Teacher;
import com.example.SchoolWebsite.model.Guardian;
import com.example.SchoolWebsite.service.GuardianService;
import com.example.SchoolWebsite.service.StaffService;
import com.example.SchoolWebsite.service.StudentService;
import com.example.SchoolWebsite.service.TeacherService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final StaffService staffService;
    private final GuardianService guardianService;

    public AdminController(StudentService studentService, TeacherService teacherService, StaffService staffService, GuardianService guardianService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.staffService = staffService;
        this.guardianService = guardianService;
    }

    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        String userId = (String) session.getAttribute("userId");
        return "admin".equals(role) && userId != null;
    }

    // ==========================================
    // 1. MANAGE STUDENTS
    // ==========================================

    @GetMapping("/students")
    public String listStudents(
            @RequestParam(required = false) String standard,
            @RequestParam(required = false) String division,
            @RequestParam(required = false) String search,
            HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        List<Student> students = studentService.searchAndFilter(standard, division, search);

        model.addAttribute("students", students);
        model.addAttribute("standard", standard != null ? standard : "");
        model.addAttribute("division", division != null ? division : "");
        model.addAttribute("search", search != null ? search : "");
        model.addAttribute("availableClasses", studentService.getAvailableClasses());
        model.addAttribute("availableDivisions", studentService.getAvailableDivisions());
        model.addAttribute("availableSections", studentService.getAvailableSections());
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", session.getAttribute("userId"));
        model.addAttribute("loggedInRole", "admin");

        return "admin/students";
    }

    @GetMapping("/students/new")
    public String newStudentForm(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Student student = new Student();
        student.setRemainingFees(0.0);
        student.setAge(15);
        student.setDateOfAdmission(java.time.LocalDate.now().toString());

        model.addAttribute("student", student);
        model.addAttribute("mode", "create");
        model.addAttribute("availableClasses", studentService.getAvailableClasses());
        model.addAttribute("availableDivisions", studentService.getAvailableDivisions());
        model.addAttribute("availableSections", studentService.getAvailableSections());
        model.addAttribute("availableHouses", studentService.getAvailableHouses());
        model.addAttribute("availableVehicles", studentService.getAvailableVehicles());
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", session.getAttribute("userId"));
        model.addAttribute("loggedInRole", "admin");

        return "admin/student-form";
    }

    @PostMapping("/students/new")
    public String createStudent(
            @ModelAttribute Student student,
            @RequestParam(required = false) String guardianRelationship,
            @RequestParam(required = false) String guardianFirstName,
            @RequestParam(required = false) String guardianMiddleName,
            @RequestParam(required = false) String guardianLastName,
            @RequestParam(required = false) String guardianOccupation,
            @RequestParam(required = false) String guardianPhone,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        try {
            studentService.saveStudent(student, guardianRelationship, guardianFirstName,
                    guardianMiddleName, guardianLastName, guardianOccupation, guardianPhone);
            redirectAttributes.addFlashAttribute("successMessage", "Student " + student.getAdmissionNo() + " created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create student: " + e.getMessage());
        }

        return "redirect:/admin/students";
    }

    @GetMapping("/students/edit/{admissionNo}")
    public String editStudentForm(@PathVariable String admissionNo, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Student student = studentService.findByAdmissionNo(admissionNo);
        if (student == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Student not found with Admission No: " + admissionNo);
            return "redirect:/admin/students";
        }

        model.addAttribute("student", student);
        model.addAttribute("mode", "edit");

        List<Guardian> guardians = guardianService.findByStudentId(admissionNo);
        model.addAttribute("guardian", guardians.isEmpty() ? null : guardians.get(0));

        model.addAttribute("availableClasses", studentService.getAvailableClasses());
        model.addAttribute("availableDivisions", studentService.getAvailableDivisions());
        model.addAttribute("availableSections", studentService.getAvailableSections());
        model.addAttribute("availableHouses", studentService.getAvailableHouses());
        model.addAttribute("availableVehicles", studentService.getAvailableVehicles());
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", session.getAttribute("userId"));
        model.addAttribute("loggedInRole", "admin");

        return "admin/student-form";
    }

    @PostMapping("/students/edit/{admissionNo}")
    public String updateStudent(
            @PathVariable String admissionNo,
            @ModelAttribute Student student,
            @RequestParam(required = false) String guardianRelationship,
            @RequestParam(required = false) String guardianFirstName,
            @RequestParam(required = false) String guardianMiddleName,
            @RequestParam(required = false) String guardianLastName,
            @RequestParam(required = false) String guardianOccupation,
            @RequestParam(required = false) String guardianPhone,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        student.setAdmissionNo(admissionNo);
        try {
            studentService.updateStudent(student, guardianRelationship, guardianFirstName,
                    guardianMiddleName, guardianLastName, guardianOccupation, guardianPhone);
            redirectAttributes.addFlashAttribute("successMessage", "Student " + admissionNo + " updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update student: " + e.getMessage());
        }

        return "redirect:/admin/students";
    }

    @PostMapping("/students/delete/{admissionNo}")
    public String deleteStudent(@PathVariable String admissionNo, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        try {
            studentService.deleteStudent(admissionNo);
            redirectAttributes.addFlashAttribute("successMessage", "Student " + admissionNo + " deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete student: " + e.getMessage());
        }

        return "redirect:/admin/students";
    }

    // ==========================================
    // 2. MANAGE FACULTY (TEACHERS & STAFF)
    // ==========================================

    @GetMapping("/faculty")
    public String listFaculty(
            @RequestParam(defaultValue = "teachers") String tab,
            HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        List<Teacher> teachers = teacherService.getAllTeachers();
        List<Staff> staffList = staffService.getAllStaff();

        model.addAttribute("teachers", teachers);
        model.addAttribute("staffList", staffList);
        model.addAttribute("activeTab", tab);
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", session.getAttribute("userId"));
        model.addAttribute("loggedInRole", "admin");

        return "admin/faculty";
    }

    // --- Teacher CRUD ---

    @GetMapping("/faculty/teacher/new")
    public String newTeacherForm(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Teacher teacher = new Teacher();
        teacher.setDateOfJoining(java.time.LocalDate.now().toString());

        model.addAttribute("teacher", teacher);
        model.addAttribute("mode", "create");
        model.addAttribute("qualificationsInput", "");
        model.addAttribute("specializationsInput", "");
        model.addAttribute("availableHouses", studentService.getAvailableHouses());
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", session.getAttribute("userId"));
        model.addAttribute("loggedInRole", "admin");

        return "admin/teacher-form";
    }

    @PostMapping("/faculty/teacher/new")
    public String createTeacher(
            @ModelAttribute Teacher teacher,
            @RequestParam(required = false) String qualificationsInput,
            @RequestParam(required = false) String specializationsInput,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        try {
            List<String> quals = parseCommaList(qualificationsInput);
            List<String> specs = parseCommaList(specializationsInput);
            teacherService.saveTeacher(teacher, quals, specs);
            redirectAttributes.addFlashAttribute("successMessage", "Teacher " + teacher.getEmployeeId() + " created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create teacher: " + e.getMessage());
        }

        return "redirect:/admin/faculty?tab=teachers";
    }

    @GetMapping("/faculty/teacher/edit/{employeeId}")
    public String editTeacherForm(@PathVariable String employeeId, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Teacher teacher = teacherService.getFullProfile(employeeId);
        if (teacher == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Teacher not found: " + employeeId);
            return "redirect:/admin/faculty?tab=teachers";
        }

        String quals = teacher.getQualifications() != null ? String.join(", ", teacher.getQualifications()) : "";
        String specs = teacher.getSpecializations() != null ? String.join(", ", teacher.getSpecializations()) : "";

        model.addAttribute("teacher", teacher);
        model.addAttribute("mode", "edit");
        model.addAttribute("qualificationsInput", quals);
        model.addAttribute("specializationsInput", specs);
        model.addAttribute("availableHouses", studentService.getAvailableHouses());
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", session.getAttribute("userId"));
        model.addAttribute("loggedInRole", "admin");

        return "admin/teacher-form";
    }

    @PostMapping("/faculty/teacher/edit/{employeeId}")
    public String updateTeacher(
            @PathVariable String employeeId,
            @ModelAttribute Teacher teacher,
            @RequestParam(required = false) String qualificationsInput,
            @RequestParam(required = false) String specializationsInput,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        teacher.setEmployeeId(employeeId);
        try {
            List<String> quals = parseCommaList(qualificationsInput);
            List<String> specs = parseCommaList(specializationsInput);
            teacherService.updateTeacher(teacher, quals, specs);
            redirectAttributes.addFlashAttribute("successMessage", "Teacher " + employeeId + " updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update teacher: " + e.getMessage());
        }

        return "redirect:/admin/faculty?tab=teachers";
    }

    @PostMapping("/faculty/teacher/delete/{employeeId}")
    public String deleteTeacher(@PathVariable String employeeId, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        try {
            teacherService.deleteTeacher(employeeId);
            redirectAttributes.addFlashAttribute("successMessage", "Teacher " + employeeId + " deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete teacher: " + e.getMessage());
        }

        return "redirect:/admin/faculty?tab=teachers";
    }

    // --- Staff CRUD ---

    @GetMapping("/faculty/staff/new")
    public String newStaffForm(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Staff staff = new Staff();
        staff.setDateOfJoining(java.time.LocalDate.now().toString());

        model.addAttribute("staff", staff);
        model.addAttribute("roles", staffService.getAllRoles());
        model.addAttribute("mode", "create");
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", session.getAttribute("userId"));
        model.addAttribute("loggedInRole", "admin");

        return "admin/staff-form";
    }

    @PostMapping("/faculty/staff/new")
    public String createStaff(@ModelAttribute Staff staff, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        try {
            staffService.saveStaff(staff);
            redirectAttributes.addFlashAttribute("successMessage", "Staff member " + staff.getEmployeeId() + " created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create staff member: " + e.getMessage());
        }

        return "redirect:/admin/faculty?tab=staff";
    }

    @GetMapping("/faculty/staff/edit/{employeeId}")
    public String editStaffForm(@PathVariable String employeeId, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Staff staff = staffService.getStaffProfile(employeeId);
        if (staff == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Staff member not found: " + employeeId);
            return "redirect:/admin/faculty?tab=staff";
        }

        model.addAttribute("staff", staff);
        model.addAttribute("roles", staffService.getAllRoles());
        model.addAttribute("mode", "edit");
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", session.getAttribute("userId"));
        model.addAttribute("loggedInRole", "admin");

        return "admin/staff-form";
    }

    @PostMapping("/faculty/staff/edit/{employeeId}")
    public String updateStaff(@PathVariable String employeeId, @ModelAttribute Staff staff, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        staff.setEmployeeId(employeeId);
        try {
            staffService.updateStaff(staff);
            redirectAttributes.addFlashAttribute("successMessage", "Staff member " + employeeId + " updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update staff member: " + e.getMessage());
        }

        return "redirect:/admin/faculty?tab=staff";
    }

    @PostMapping("/faculty/staff/delete/{employeeId}")
    public String deleteStaff(@PathVariable String employeeId, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        try {
            staffService.deleteStaff(employeeId);
            redirectAttributes.addFlashAttribute("successMessage", "Staff member " + employeeId + " deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete staff member: " + e.getMessage());
        }

        return "redirect:/admin/faculty?tab=staff";
    }

    private List<String> parseCommaList(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
