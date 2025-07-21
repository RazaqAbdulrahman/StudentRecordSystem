package service;

import util.FileHelper;
import model.Student;

import java.util.List;

public class StudentService {
    private List<Student> studentList;

    public StudentService() {
        this.studentList = FileHelper.loadStudentsFromFile();
    }

    public void addStudent(Student student) {
        studentList.add(student);
        FileHelper.saveStudentsToFile(studentList);
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
            return true;
        }
        return false;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void saveChanges() {
        FileHelper.saveStudentsToFile(studentList);
    }
}


