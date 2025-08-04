package service;

import model.Course;
import model.Student;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsService {

    private List<Student> students;
    public StatisticsService(List<Student> students) {
        this.students = students;
    }
    public void showBasicStatistics() {
        System.out.println("========== SYSTEM STATISTICS ==========");

        System.out.println("Total Students: " + students.size());

        showDepartmentStats();

        showGradeDistribution();

        showCourseStats();
        System.out.println("=======================================");
    }
    private void showDepartmentStats() {
        Map<String, Integer> deptCount = new HashMap<>();
        for (Student student : students) {
            String dept = student.getDepartment();
            deptCount.put(dept, deptCount.getOrDefault(dept, 0) + 1);
        }
        System.out.println("\nStudents per Department:");
        for (Map.Entry<String, Integer> entry : deptCount.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
    }

    private void showGradeDistribution(){
        Map<String,Integer> gradeCount =new HashMap<>();
        for(Student student : students){
            for(Course course : student.getCourses()){
                String grade = course.getGrade();
                gradeCount.put(grade, gradeCount.getOrDefault(grade,0)+1);
            }
        }
        System.out.println("\nGrade Distribution:");
        for(Map.Entry<String,Integer> entry : gradeCount.entrySet()){
            System.out.println("  Grade "+ entry.getKey()+": "+ entry.getValue()+" courses");
        }
    }
    private void showCourseStats(){
        Map<String,Integer> courseCount =new HashMap<>();
        int totalCourses =0;
        for(Student student : students){
            for(Course course : student.getCourses()){
                String courseCode = course.getCourseCode();
                courseCount.put(courseCode, courseCount.getOrDefault(courseCode,0)+1);
                totalCourses++;
            }
        }
        System.out.println("\nCourse Enrollment:");
        System.out.println("  Total course enrollments: "+ totalCourses);
        System.out.println("  Unique courses offered: "+ courseCount.size());

        System.out.println("  Most enrolled courses:");
        courseCount.entrySet().stream()
                .sorted((e1, e2)-> e2.getValue().compareTo(e1.getValue()))
                .limit(3)
                .forEach(entry ->System.out.println("    "+ entry.getKey()+": "+ entry.getValue()+" students"));
    }
}

