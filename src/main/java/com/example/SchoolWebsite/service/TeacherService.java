package com.example.SchoolWebsite.service;

import com.example.SchoolWebsite.model.ScheduleItem;
import com.example.SchoolWebsite.model.Teacher;
import com.example.SchoolWebsite.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher getFullProfile(String employeeId) {
        return teacherRepository.findFullProfile(employeeId);
    }

    public List<ScheduleItem> getSchedule(String employeeId) {
        return teacherRepository.getSchedule(employeeId);
    }

    public List<String> getTeacherSections(String employeeId) {
        return teacherRepository.getTeacherSections(employeeId);
    }

    public List<com.example.SchoolWebsite.model.Student> getEnrolledStudentsForCourse(String subjectCode, String standard, String division) {
        return teacherRepository.getEnrolledStudentsForCourse(subjectCode, standard, division);
    }

    public String getSubjectName(String subjectCode) {
        return teacherRepository.getSubjectName(subjectCode);
    }

    public List<com.example.SchoolWebsite.model.Payment> getSalaryHistory(String employeeId) {
        return teacherRepository.getSalaryHistory(employeeId);
    }
}
