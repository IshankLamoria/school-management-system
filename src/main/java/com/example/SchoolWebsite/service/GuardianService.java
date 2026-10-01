package com.example.SchoolWebsite.service;

import com.example.SchoolWebsite.model.Guardian;
import com.example.SchoolWebsite.repository.GuardianRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuardianService {

    private final GuardianRepository guardianRepository;

    public GuardianService(GuardianRepository guardianRepository) {
        this.guardianRepository = guardianRepository;
    }

    public List<Guardian> findByStudentId(String studentId) {
        return guardianRepository.findByStudentId(studentId);
    }
}
