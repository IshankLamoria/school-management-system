package com.example.SchoolWebsite.model;

import java.util.ArrayList;
import java.util.List;

public class Enrollment {
    private String subjectCode;
    private String subjectName;
    private int periodsPerWeek;
    private String grade;
    private String teacherName;
    private List<String> referenceBooks = new ArrayList<>();

    public Enrollment() {}

    public Enrollment(String subjectCode, String subjectName, int periodsPerWeek, String grade, String teacherName) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.periodsPerWeek = periodsPerWeek;
        this.grade = grade;
        this.teacherName = teacherName;
    }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public int getPeriodsPerWeek() { return periodsPerWeek; }
    public void setPeriodsPerWeek(int periodsPerWeek) { this.periodsPerWeek = periodsPerWeek; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public List<String> getReferenceBooks() { return referenceBooks; }
    public void setReferenceBooks(List<String> referenceBooks) { this.referenceBooks = referenceBooks; }
}
