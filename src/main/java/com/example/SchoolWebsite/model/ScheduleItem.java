package com.example.SchoolWebsite.model;

public class ScheduleItem {
    private String teacherId;
    private String subjectCode;
    private String subjectName;
    private String standard;
    private String division;
    private String roomNo;
    private int enrolledCount;

    public ScheduleItem() {}

    public ScheduleItem(String teacherId, String subjectCode, String subjectName, String standard, String division, String roomNo, int enrolledCount) {
        this.teacherId = teacherId;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.standard = standard;
        this.division = division;
        this.roomNo = roomNo;
        this.enrolledCount = enrolledCount;
    }

    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getStandard() { return standard; }
    public void setStandard(String standard) { this.standard = standard; }

    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }

    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }

    public int getEnrolledCount() { return enrolledCount; }
    public void setEnrolledCount(int enrolledCount) { this.enrolledCount = enrolledCount; }
}
