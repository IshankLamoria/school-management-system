package com.example.SchoolWebsite;

import java.util.List;
import com.example.SchoolWebsite.model.Student;
import com.example.SchoolWebsite.service.StudentService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// This is supposed to contain all the routes
// Basic routes :
//    User profile (student / teacher or faculty, whichever is logged in)
//        Courses and Grade
//        Fee and payments (fee for student and payment for non student)
//
//    
//    Student directory (list of all students from respective class)
//
//    Faculty directory (list of all students from respective class)
//
//    Transportation etc.
//    
//
//
//    After deciding everyihing we shall add all routes here
//    along with html files templates/ folder

@Controller
public class HomeController {

    private final StudentService studentService;

    public HomeController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("message", "this is added to home page form backend");
        return "index"; // looks for index.html
    }

    @GetMapping("/students")
    public String showStudentDirectory(Model model) {
        // Fetch students from database via Service -> Repository
        List<Student> students = studentService.findAll();

        // Add the list to the model so the webpage can see it
        model.addAttribute("studentList", students);

        // Return the name of the HTML file: src/main/resources/templates/students.html
        return "students";
    }

} // <-- Fixed: closed the class properly with a simple closing brace (no trailing comma)
