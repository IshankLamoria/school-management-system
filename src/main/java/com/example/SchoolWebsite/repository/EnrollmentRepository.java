package com.example.SchoolWebsite.repository;

import com.example.SchoolWebsite.model.Enrollment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnrollmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public EnrollmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Enrollment> findByStudentId(String studentId) {
        String sql = "SELECT e.Subject_ID, s.Subject_Name, s.Periods_Per_Week, e.Grade, " +
                     "CONCAT(t.First_Name, ' ', COALESCE(t.Last_Name, '')) AS teacherName " +
                     "FROM Enrollments e " +
                     "JOIN Subject s ON e.Subject_ID = s.Subject_Code " +
                     "LEFT JOIN Teacher_Subject ts ON s.Subject_Code = ts.Subject_ID " +
                     "LEFT JOIN Teacher t ON ts.Teacher_ID = t.Employee_ID " +
                     "WHERE e.Student_ID = ? " +
                     "ORDER BY s.Subject_Name ASC";

        List<Enrollment> enrollments = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Enrollment e = new Enrollment();
            e.setSubjectCode(rs.getString("Subject_ID"));
            e.setSubjectName(rs.getString("Subject_Name"));
            e.setPeriodsPerWeek(rs.getInt("Periods_Per_Week"));
            e.setGrade(rs.getString("Grade"));
            String teacher = rs.getString("teacherName");
            e.setTeacherName(teacher != null && !teacher.trim().isEmpty() ? teacher : "Not Assigned");
            return e;
        }, studentId);

        // Fetch reference books for each enrolled subject
        for (Enrollment e : enrollments) {
            String bookSql = "SELECT Book_name FROM Reference_Books WHERE Subject_Code = ?";
            List<String> books = jdbcTemplate.queryForList(bookSql, String.class, e.getSubjectCode());
            e.setReferenceBooks(books);
        }

        return enrollments;
    }
}
