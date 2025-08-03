package model;

public class Course {
    private String courseCode;
    private String title;
    private int unit;
    private String grade;

    public Course(String courseCode,
                  String title,
                  int unit,
                  String grade)
    {
        this.courseCode = courseCode;
        this.title = title;
        this.unit = unit;
        this.grade = grade;
    }

    public String getCourseCode() {
        return courseCode;
    }
    public String getTitle() {
        return title;
    }
    public int getUnit() {
        return unit;
    }
    public String getGrade() {
        return grade;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setUnit(int unit) {
        this.unit = unit;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void printCourseInfo(){
        System.out.println("Course Code: " + courseCode);
        System.out.println("Title: " + title);
        System.out.println("Unit: " + unit);
        System.out.println("grade: " + grade);
    }
}
