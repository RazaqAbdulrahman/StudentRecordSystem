import model.Course;
import model.Student;
import service.StudentService;
import service.GPAService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentService studentService = new StudentService();
        GPAService gpaService = new GPAService();

        boolean running = true;

        System.out.println("Welcome to the Student Record Management System!");

        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Add a Student");
            System.out.println("2. View All Students");
            System.out.println("3. Find Student by Matric Number");
            System.out.println("4. Delete a Student");
            System.out.println("5. Enroll Student in Course");
            System.out.println("6. Update Course Grade");
            System.out.println("7. Calculate GPA");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter student name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter matric number: ");
                    String matric = scanner.nextLine();

                    System.out.print("Enter department: ");
                    String dept = scanner.nextLine();

                    Student student = new Student(name, matric, dept);
                    studentService.addStudent(student);
                    break;

                case 2:
                    studentService.viewAllStudents();
                    break;

                case 3:
                    System.out.print("Enter matric number to search: ");
                    String searchMatric = scanner.nextLine();
                    Student found = studentService.findByMatricNumber(searchMatric);
                    if (found != null) {
                        found.printBioData();
                        found.printCourses();
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter matric number to delete: ");
                    String deleteMatric = scanner.nextLine();
                    boolean removed = studentService.deleteStudent(deleteMatric);
                    if (removed) {
                        System.out.println("Student deleted.");
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 5:
                    System.out.print("Enter matric number of student to enroll: ");
                    String matricEnroll = scanner.nextLine();
                    Student enrollStudent = studentService.findByMatricNumber(matricEnroll);
                    if (enrollStudent != null) {
                        System.out.print("Enter course code: ");
                        String code = scanner.nextLine();

                        System.out.print("Enter course title: ");
                        String title = scanner.nextLine();

                        System.out.print("Enter course unit: ");
                        int unit = scanner.nextInt();
                        scanner.nextLine();

                        Course course = new Course(code, title, unit, "");
                        enrollStudent.enrollInCourse(course);
                        System.out.println("Course enrolled.");
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 6:
                    System.out.print("Enter matric number: ");
                    String mNumber = scanner.nextLine();
                    Student gradeStudent = studentService.findByMatricNumber(mNumber);

                    if (gradeStudent != null) {
                        System.out.print("Enter course code to update grade: ");
                        String courseCode = scanner.nextLine();

                        System.out.print("Enter grade (A-F): ");
                        String grade = scanner.nextLine();

                        boolean updated = gradeStudent.updateGrade(courseCode, grade);
                        if (updated) {
                            System.out.println("Grade updated.");
                        } else {
                            System.out.println("Course not found.");
                        }
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 7:
                    System.out.print("Enter matric number: ");
                    String gpaMatric = scanner.nextLine();
                    Student gpaStudent = studentService.findByMatricNumber(gpaMatric);
                    if (gpaStudent != null) {
                        double gpa = gpaService.calculateGPA(gpaStudent);
                        String classification = gpaService.classifyGPA(gpa);
                        System.out.println("GPA: " + gpa);
                        System.out.println("Classification: " + classification);
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 8:
                    System.out.println("Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}
