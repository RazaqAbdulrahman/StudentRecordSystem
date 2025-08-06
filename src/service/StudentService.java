package service;
import model.Course;
import model.Student;
import util.FileHelper;
import util.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentService {
    private List<Student> studentList;

    public StudentService() {
        this.studentList = FileHelper.loadStudentsFromFile();
    }

    private List<Course> getDefaultCoursesForDepartment(String department) {
        List<Course> defaultCourses = new ArrayList<>();

        for (Student s : studentList) {
            for (Course c : s.getCourses()) {

                if (!s.getDepartment().equalsIgnoreCase(department)) continue;


                boolean alreadyExists = defaultCourses.stream()
                        .anyMatch(dc -> dc.getCourseCode().equalsIgnoreCase(c.getCourseCode()));

                if (!alreadyExists) {
                    defaultCourses.add(new Course(
                            c.getCourseCode(),
                            c.getTitle(),
                            c.getUnit(),
                            "N/A"
                    ));
                }
            }
        }

        return defaultCourses;
    }


    /*
    private String generateNextMatricNumber() {
        int maxMatric = 0;

        for (Student student : studentList) {
            try {
                int current = Integer.parseInt(student.getMatricNumber());
                if (current > maxMatric) {
                    maxMatric = current;
                }
            } catch (NumberFormatException ignored) {

            }
        }

     */
    private String generateNextMatricNumber() {
        int maxMatric = 0;

        for (Student student : studentList) {
            try {
                int current = Integer.parseInt(student.getMatricNumber());
                if (current > maxMatric) {
                    maxMatric = current;
                }
            } catch (NumberFormatException ignored) {

            }
        }

        return String.format("%03d", maxMatric + 1); // e.g., "001", "002", "080"
    }



    /*
     old codeee ---
     public void addStudent(Student student) {
        studentList.add(student);
        FileHelper.saveStudentsToFile(studentList);
        saveStudentToDatabase(student);
        System.out.println("Student added successfully.");
    }
    */
   public void addStudent(Student student) {

       for (Student s : studentList) {
           if (s.getName().equalsIgnoreCase(student.getName()) && s.getDepartment().equalsIgnoreCase(student.getDepartment())) {
               System.out.println("Error: Student with this name already exists in the same department.");
               return;
           }
       }


       List<Course> defaultCourses = getDefaultCoursesForDepartment(student.getDepartment());

       if (defaultCourses.isEmpty()) {
           System.out.println(" Warning: No course catalog found for department '" + student.getDepartment() + "'.");
       } else {
           for (Course course : defaultCourses) {
               student.enrollInCourse(course);
           }
       }



       String newMatric = generateNextMatricNumber();
       student.setMatricNumber(newMatric);

       studentList.add(student);
       FileHelper.saveStudentsToFile(studentList);
       saveStudentToDatabase(student);

       System.out.println("Student added successfully with matric number: " + newMatric);
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

    public boolean updateStudentGradesInteractively(String matricNumber, Scanner input) {
        Student student = findByMatricNumber(matricNumber);
        if (student == null) {
            System.out.println("Student with matric number " + matricNumber + " not found.");
            return false;
        }

        List<Course> courses = student.getCourses();
        if (courses.isEmpty()) {
            System.out.println("⚠ This student is not enrolled in any courses.");
            return false;
        }

        System.out.println("\n📋 Current Courses for " + student.getName() + ":\n");
        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            System.out.printf("%d. %s - %s (%d units) | Grade: %s%n", i + 1, c.getCourseCode(), c.getTitle(), c.getUnit(), c.getGrade());
        }

        System.out.println("\n📝 Enter new grades or press Enter to skip:");

        for (Course course : courses) {
            System.out.print("New grade for " + course.getCourseCode() + " (" + course.getTitle() + "): ");
            String grade = input.nextLine().trim().toUpperCase();
            if (!grade.isEmpty()) {
                course.setGrade(grade);
            }
        }

        FileHelper.saveStudentsToFile(studentList);
        for (Course course : courses) {
            DatabaseHelper.updateCourseGrade(matricNumber, course.getCourseCode(), course.getGrade());
        }

        System.out.println("Grades updated successfully for " + student.getName());
        return true;
    }


   /*
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
    */

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

    public void searchStudentsByName(String searchName){
        List<Student> found =new ArrayList<>();
        for(Student student : studentList){
            if(student.getName().toLowerCase().contains(searchName.toLowerCase())){
                found.add(student);
            }
        }
        if(found.isEmpty()){
            System.out.println("No students found with name containing: "+ searchName);
        }else{
            System.out.println("Found "+ found.size()+" student(s):");
            for(Student student : found){
                student.printBioData();
                System.out.println("-----------------------");
            }
        }
    }
    public void searchStudentsByDepartment(String department){
        List<Student> found =new ArrayList<> ();
        for(Student student : studentList){
            if(student.getDepartment().toLowerCase().contains(department.toLowerCase())){
                found.add(student);
            }
        }
        if(found.isEmpty()){
            System.out.println("No students found in department: "+ department);
        }else{
            System.out.println("Found "+ found.size()+" student(s) in "+ department +":");
            for(Student student : found){
                student.printBioData();
                System.out.println("-----------------------");
            }
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




