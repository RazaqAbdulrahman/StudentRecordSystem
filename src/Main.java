import model.Course;
import model.Student;
import service.StudentService;
import service.GPAService;
import util.DatabaseHelper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        StudentService studentService = new StudentService();
        GPAService gpaService = new GPAService();
        DatabaseHelper.initializeDatabase();


        while (true) {
            System.out.println("\n--- Student Record Management System ---");
            System.out.println("1. Add New Student");
            System.out.println("2. View All Students");
            System.out.println("3. Delete Student");
            System.out.println("4. Update Grade");
            System.out.println("5. Calculate GPA");
            System.out.println("6. Enroll In Course");
            System.out.println("7. Sync from Database");
            System.out.println("8. Update Student Grade");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = input.nextLine();
                    System.out.print("Enter matric number: ");
                    String matric = input.nextLine();
                    System.out.print("Enter department: ");
                    String dept = input.nextLine();

                    Student student = new Student(name, matric, dept);
                    studentService.addStudent(student);
                    break;

                case 2:
                    studentService.viewAllStudents();
                    break;

                case 3:
                    System.out.print("Enter matric number to delete: ");
                    String deleteMat = input.nextLine();
                    boolean deleted = studentService.deleteStudent(deleteMat);
                    if (deleted) {
                        System.out.println("Student deleted successfully.");
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter matric number: ");
                    String matUpdate = input.nextLine();
                    Student updateStudent = studentService.findByMatricNumber(matUpdate);

                    if (updateStudent != null) {
                        System.out.print("Enter course code to update: ");
                        String updateCode = input.nextLine();
                        System.out.print("Enter new grade: ");
                        String newGrade = input.nextLine();

                        boolean updated = updateStudent.updateGrade(updateCode, newGrade);
                        if (updated) {
                            studentService.saveChanges();
                            System.out.println("Grade updated successfully.");
                        } else {
                            System.out.println("Course not found.");
                        }
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 5:
                    System.out.print("Enter matric number: ");
                    String gpaMatric = input.nextLine();

                    double gpa = gpaService.calculateGPA(gpaMatric);
                    String classif = gpaService.classifyGPA(gpa);
                    System.out.printf("GPA: %.2f - Classification: %s%n", gpa, classif);
                    break;

                case 6:
                    System.out.print("Enter matric number: ");
                    String matEnroll = input.nextLine();
                    Student enrollStudent = studentService.findByMatricNumber(matEnroll);

                    if (enrollStudent != null) {
                        System.out.print("Enter course code: ");
                        String code = input.nextLine();
                        System.out.print("Enter course title: ");
                        String title = input.nextLine();
                        System.out.print("Enter course unit: ");
                        int unit = input.nextInt();
                        input.nextLine(); // clear newline
                        System.out.print("Enter course grade: ");
                        String grade = input.nextLine();

                        Course course = new Course(code, title, unit, grade);
                        enrollStudent.enrollInCourse(course);
                        studentService.saveChanges();
                        System.out.println("Course added successfully.");
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;
                case 7:
                    studentService.syncFromDatabase();
                    System.out.println("Synced from database.");
                    break;

                case 8:
                    System.out.print("Enter Matric: ");
                    String matricNum = input.nextLine();

                    System.out.print("Course Code: ");
                    String courseCode = input.nextLine();

                    System.out.print("New Grade: ");
                    String newGradeVal = input.nextLine();

                    studentService.updateGrade(matricNum, courseCode, newGradeVal);
                    break;

                case 0:
                    System.out.println("Exiting program. Goodbye!");
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}


