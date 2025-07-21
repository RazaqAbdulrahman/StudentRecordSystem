package model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String name;
    private String matricNumber;
    private String department;
    private List<Course> courses;

    public Student(String name, String matricNumber, String department) {
        this.name = name;
        this.matricNumber = matricNumber;
        this.department = department;
        this.courses = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public String getMatricNumber() {
        return matricNumber;
    }

    public String getDepartment() {
        return department;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void enrollInCourse(Course course) {
        courses.add(course);
    }

    public boolean updateGrade(String courseCode, String grade) {
        for (Course course : courses) {
            if (course.getCourseCode().equalsIgnoreCase(courseCode)) {
                course.setGrade(grade);
                return true;
            }
        }
        return false;
    }

    public void printCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses enrolled.");
        } else {
            for (Course c : courses) {
                System.out.println(c);
            }
        }
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Matric: " + matricNumber + ", Dept: " + department;
    }

    public void printBioData() {
        System.out.println("Name: " + name);
        System.out.println("Matric Number: " + matricNumber);
        System.out.println("Department: " + department);
    }
}

