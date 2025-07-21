package util;

import model.Course;
import model.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
    private static final String FILE_PATH = "students.txt";

    public static void saveStudents(List<Student> students) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            for (Student student : students) {
                writer.write("STUDENT|" + student.getName() + "|" + student.getMatricNumber() + "|" + student.getDepartment() + "\n");
                for (Course course : student.getCourses()) {
                    writer.write("COURSE|" + course.getCourseCode() + "|" + course.getTitle() + "|" + course.getUnit() + "|" + course.getGrade() + "\n");
                }
                writer.write("END\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            Student currentStudent = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("STUDENT|")) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 4) {
                        currentStudent = new Student(parts[1], parts[2], parts[3]);
                        students.add(currentStudent);
                    }
                } else if (line.startsWith("COURSE|") && currentStudent != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 5) {
                        String code = parts[1];
                        String title = parts[2];
                        int unit = Integer.parseInt(parts[3]);
                        String grade = parts[4];
                        currentStudent.enrollInCourse(new Course(code, title, unit, grade));
                    }
                } else if (line.equals("END")) {
                    currentStudent = null;
                }
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
        return students;
    }
}

