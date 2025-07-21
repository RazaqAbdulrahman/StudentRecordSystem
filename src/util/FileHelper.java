package util;

import model.Course;
import model.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
    private static final String FILE_PATH = "src/resources/students.txt";

    public static void saveStudentsToFile(List<Student> students) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Student student : students) {
                writer.write("STUDENT|" + student.getName() + "|" + student.getMatricNumber() + "|" + student.getDepartment());
                writer.newLine();
                for (Course course : student.getCourses()) {
                    writer.write("COURSE|" + course.getCourseCode() + "|" + course.getTitle() + "|" + course.getUnit() + "|" + course.getGrade());
                    writer.newLine();
                }
                writer.write("END");
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving students to file: " + e.getMessage());
        }
    }

    public static List<Student> loadStudentsFromFile() {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            Student currentStudent = null;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts[0].equals("STUDENT") && parts.length == 4) {
                    currentStudent = new Student(parts[1], parts[2], parts[3]);
                    students.add(currentStudent);
                } else if (parts[0].equals("COURSE") && parts.length == 5 && currentStudent != null) {
                    Course course = new Course(parts[1], parts[2], Integer.parseInt(parts[3]), parts[4]);
                    currentStudent.enrollInCourse(course);
                } else if (parts[0].equals("END")) {
                    currentStudent = null;
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading students from file: " + e.getMessage());
        }

        return students;
    }
}


