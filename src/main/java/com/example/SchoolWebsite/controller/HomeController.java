package com.example.SchoolWebsite;

import java.util.List;
import com.example.SchoolWebsite.model.Student;
import com.example.SchoolWebsite.service.StudentService;
import com.example.SchoolWebsite.repository.StatsRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final StudentService studentService;
    private final StatsRepository statsRepository;

    public HomeController(StudentService studentService, StatsRepository statsRepository) {
        this.studentService = studentService;
        this.statsRepository = statsRepository;
    }

    @GetMapping("/")
    public String homePage(HttpSession session, Model model) {
        // Aggregate stats for the home page
        model.addAttribute("studentCount", statsRepository.countStudents());
        model.addAttribute("teacherCount", statsRepository.countTeachers());
        model.addAttribute("staffCount", statsRepository.countStaff());
        model.addAttribute("houseCount", statsRepository.countHouses());

        // Session information so navbar and page reflect login state
        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        model.addAttribute("isLoggedIn", userId != null);
        model.addAttribute("loggedInUser", userId);
        model.addAttribute("loggedInRole", role);

        return "index"; // looks for index.html
    }

    @GetMapping("/students")
    public String showStudentDirectory(HttpSession session, Model model) {
        // Protect route: require login as any role
        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || role == null) {
            return "redirect:/login";
        }

        List<Student> students;
        String directoryTitle;

        if ("student".equals(role)) {
            // Only show students of his/her class and section
            Student currentStudent = studentService.findByAdmissionNo(userId);
            if (currentStudent != null && currentStudent.getStandard() != null && currentStudent.getDivision() != null) {
                students = studentService.findByStandardAndDivision(currentStudent.getStandard(), currentStudent.getDivision());
                directoryTitle = "Class " + currentStudent.getStandard() + " - Section " + currentStudent.getDivision() + " Directory";
            } else {
                students = List.of();
                directoryTitle = "Class Directory";
            }
        } else {
            // Teacher, Staff, Admin can view all students
            students = studentService.findAll();
            directoryTitle = "All Students Directory";
        }

        model.addAttribute("studentList", students);
        model.addAttribute("directoryTitle", directoryTitle);
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("loggedInUser", userId);
        model.addAttribute("loggedInRole", role);

        return "students";
    }
}
