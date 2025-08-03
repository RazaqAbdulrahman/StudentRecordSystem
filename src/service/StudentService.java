package service;
import model.Course;
import model.Student;
import util.FileHelper;
import util.DatabaseHelper;
import java.sql.*;
import java.util.List;

public class StudentService {
    private List<Student> studentList;

    public StudentService() {
        this.studentList = FileHelper.loadStudentsFromFile();
    }

    public void addStudent(Student student) {
        studentList.add(student);
        FileHelper.saveStudentsToFile(studentList);
        saveStudentToDatabase(student);
        System.out.println("Student added successfully.");
    }

    public void viewAllStudents() {
        if (studentList.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        for (Student student : studentList) {
            student.printBioData();
            System.out.println("-----------------------");
        }
    }

    public Student findByMatricNumber(String matricNumber) {
        for (Student student : studentList) {
            if (student.getMatricNumber().equalsIgnoreCase(matricNumber)) {
                return student;
            }
        }
        return null;
    }

    public boolean deleteStudent(String matricNumber) {
        Student found = findByMatricNumber(matricNumber);
        if (found != null) {
            studentList.remove(found);
            FileHelper.saveStudentsToFile(studentList);
            DatabaseHelper.deleteStudentFromDatabase(matricNumber);
            return true;
        }
        return false;
    }

    public void updateGrade(String matric, String courseCode, String grade) {
        Student student = findByMatricNumber(matric);
        if (student != null) {
            student.updateGrade(courseCode, grade);
            FileHelper.saveStudentsToFile(studentList);
            DatabaseHelper.updateCourseGrade(matric, courseCode, grade);
            System.out.println("Grade updated.");
        } else {
            System.out.println("Student not found.");
        }
    }

    public void syncFromDatabase() {
        this.studentList = DatabaseHelper.fetchAllStudents();
        FileHelper.saveStudentsToFile(studentList); // Optional: sync to file
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void saveChanges() {
        FileHelper.saveStudentsToFile(studentList);
        for (Student student : studentList) {
            saveStudentToDatabase(student);
        }
    }

    private void saveStudentToDatabase(Student student) {
        String dbUrl = "jdbc:sqlite:students.db";
        String insertStudent = "INSERT OR REPLACE INTO students (matric, name, department) VALUES (?, ?, ?)";
        String deleteOldCourses = "DELETE FROM courses WHERE matric = ?";
        String insertCourse = "INSERT INTO courses (matric, courseCode, title, unit, grade) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(false);

            try (PreparedStatement psStudent = conn.prepareStatement(insertStudent)) {
                psStudent.setString(1, student.getMatricNumber());
                psStudent.setString(2, student.getName());
                psStudent.setString(3, student.getDepartment());
                psStudent.executeUpdate();
            }

            try (PreparedStatement psDelete = conn.prepareStatement(deleteOldCourses)) {
                psDelete.setString(1, student.getMatricNumber());
                psDelete.executeUpdate();
            }

            for (Course course : student.getCourses()) {
                try (PreparedStatement psCourse = conn.prepareStatement(insertCourse)) {
                    psCourse.setString(1, student.getMatricNumber());
                    psCourse.setString(2, course.getCourseCode());
                    psCourse.setString(3, course.getTitle());
                    psCourse.setInt(4, course.getUnit());
                    psCourse.setString(5, course.getGrade());
                    psCourse.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            System.out.println("DB Save Error: " + e.getMessage());
        }
    }
}




