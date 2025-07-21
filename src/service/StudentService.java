package service;

import model.Student;
import util.FileHelper;
import java.util.List;

public class StudentService {

    private List<Student> studentList;

    public StudentService() {
        studentList = FileHelper.loadStudents();
    }

    public void addStudent(Student student) {
        studentList.add(student); // add to list
        FileHelper.saveStudents(studentList); // save updated list to file
        System.out.println("Student added successfully.");
    }

    public void viewAllStudents() {
        if (studentList.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        for (Student student : studentList) {
            student.printBioData();
            student.printCourses();
            System.out.println("----------------------------");
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
        Student foundStudent = findByMatricNumber(matricNumber);

        if (foundStudent != null) {
            studentList.remove(foundStudent); // remove from list
            FileHelper.saveStudents(studentList); // save updated list
            return true;
        }
        return false;
    }

    public List<Student> getStudentList() {
        return studentList;
    }
}

