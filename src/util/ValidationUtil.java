package util;

public class ValidationUtil {

        public static boolean isValidName(String name) {
            return name.matches("[a-zA-Z\\s]{2,}");
        }

        public static boolean isValidGrade(String grade) {
            return grade.matches("[A-Fa-f]") || grade.equalsIgnoreCase("N/A");
        }

        public static boolean isValidCourseCode(String code) {
            return code.matches("[A-Z]{3}\\s\\d{3}");
        }

        public static boolean isValidDepartment(String dept) {
            return dept.matches("[a-zA-Z\\s]+");
        }
    }


