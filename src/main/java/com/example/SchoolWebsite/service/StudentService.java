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

    public List<Student> searchAndFilter(String standard, String division, String search) {
        return studentRepository.searchAndFilter(standard, division, search);
    }

    public void saveStudent(Student s, String guardianRel, String gFirst, String gMiddle, String gLast, String gOcc, String gPhone) {
        studentRepository.save(s);
        if (guardianRel != null && !guardianRel.trim().isEmpty() && gFirst != null && !gFirst.trim().isEmpty()) {
            studentRepository.saveGuardian(s.getAdmissionNo(), guardianRel.trim(), gFirst.trim(), gMiddle, gLast, gOcc != null ? gOcc.trim() : "", gPhone != null ? gPhone.trim() : "");
        }
    }

    public void updateStudent(Student s, String guardianRel, String gFirst, String gMiddle, String gLast, String gOcc, String gPhone) {
        studentRepository.update(s);
        if (guardianRel != null && !guardianRel.trim().isEmpty() && gFirst != null && !gFirst.trim().isEmpty()) {
            studentRepository.saveGuardian(s.getAdmissionNo(), guardianRel.trim(), gFirst.trim(), gMiddle, gLast, gOcc != null ? gOcc.trim() : "", gPhone != null ? gPhone.trim() : "");
        }
    }

    public void deleteStudent(String admissionNo) {
        studentRepository.delete(admissionNo);
    }

    public List<String> getAvailableClasses() {
        return studentRepository.getAvailableClasses();
    }

    public List<String> getAvailableDivisions() {
        return studentRepository.getAvailableDivisions();
    }

    public List<java.util.Map<String, Object>> getAvailableSections() {
        return studentRepository.getAvailableSections();
    }

    public List<java.util.Map<String, Object>> getAvailableHouses() {
        return studentRepository.getAvailableHouses();
    }

    public List<java.util.Map<String, Object>> getAvailableVehicles() {
        return studentRepository.getAvailableVehicles();
    }
}
