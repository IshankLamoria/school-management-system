package com.example.SchoolWebsite.service;

import com.example.SchoolWebsite.model.Student;
import com.example.SchoolWebsite.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findByAdmissionNo(String admissionNo) {
        return studentRepository.findByAdmissionNo(admissionNo);
    }

    public List<Student> findByStandardAndDivision(String standard, String division) {
        return studentRepository.findByStandardAndDivision(standard, division);
    }

    // Full profile with all JOINed data (for the student profile page)
    public Student getFullProfile(String admissionNo) {
        return studentRepository.findFullProfile(admissionNo);
    }
}
