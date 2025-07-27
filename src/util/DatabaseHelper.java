package util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Course;
import model.Student;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:students.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String createStudentsTable = "CREATE TABLE IF NOT EXISTS students (" +
                    "matric TEXT PRIMARY KEY, " +
                    "name TEXT, " +
                    "department TEXT);";

            String createCoursesTable = "CREATE TABLE IF NOT EXISTS courses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "matric TEXT, " +
                    "courseCode TEXT, " +
                    "title TEXT, " +
                    "unit INTEGER, " +
                    "grade TEXT, " +
                    "FOREIGN KEY(matric) REFERENCES students(matric));";

            stmt.execute(createStudentsTable);
            stmt.execute(createCoursesTable);
        } catch (SQLException e) {
            System.out.println("DB Init Error: " + e.getMessage());
        }
    }

    public static boolean deleteStudentFromDatabase(String matric) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String deleteCourses = "DELETE FROM courses WHERE matric = ?";
            String deleteStudent = "DELETE FROM students WHERE matric = ?";

            try (PreparedStatement ps1 = conn.prepareStatement(deleteCourses);
                 PreparedStatement ps2 = conn.prepareStatement(deleteStudent)) {
                ps1.setString(1, matric);
                ps2.setString(1, matric);
                ps1.executeUpdate();
                ps2.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Delete Error: " + e.getMessage());
            return false;
        }
    }

    public static void updateCourseGrade(String matric, String courseCode, String newGrade) {
        String updateQuery = "UPDATE courses SET grade = ? WHERE matric = ? AND courseCode = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(updateQuery)) {
            ps.setString(1, newGrade);
            ps.setString(2, matric);
            ps.setString(3, courseCode);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Grade Update Error: " + e.getMessage());
        }
    }

    public static List<Student> fetchAllStudents() {
        List<Student> list = new ArrayList<>();
        String studentQuery = "SELECT * FROM students";
        String courseQuery = "SELECT * FROM courses WHERE matric = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(studentQuery)) {

            while (rs.next()) {
                String matric = rs.getString("matric");
                String name = rs.getString("name");
                String dept = rs.getString("department");
                Student student = new Student(name, matric, dept);

                try (PreparedStatement ps = conn.prepareStatement(courseQuery)) {
                    ps.setString(1, matric);
                    ResultSet crs = ps.executeQuery();
                    while (crs.next()) {
                        String code = crs.getString("courseCode");
                        String title = crs.getString("title");
                        int unit = crs.getInt("unit");
                        String grade = crs.getString("grade");
                        student.enrollInCourse(new Course(code, title, unit, grade));
                        student.updateGrade(code, grade);
                    }
                }

                list.add(student);
            }
        } catch (SQLException e) {
            System.out.println("Fetch Error: " + e.getMessage());
        }

        return list;
    }
}

