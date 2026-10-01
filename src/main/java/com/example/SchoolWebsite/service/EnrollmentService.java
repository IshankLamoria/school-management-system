package com.example.SchoolWebsite.service;

import com.example.SchoolWebsite.model.Enrollment;
import com.example.SchoolWebsite.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Enrollment> getEnrolledSubjects(String studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }
}
