package bgu.spl.net.impl.user;

public class Course {
    private short courseNumber;
    private String courseName;
    private short[] kdamCoursesList;
    private int numOfMaxStudents;

    public Course(String line) {
        String[] args = line.split("\\|");
        courseNumber = Short.parseShort(args[0]);
        courseName = args[1];

        String kdamCourses_str = args[2];
        kdamCourses_str = kdamCourses_str.substring(1, kdamCourses_str.length() - 2);
        if (!kdamCourses_str.trim().isEmpty()) {
            String[] kdamCourses_strArray = kdamCourses_str.split(",");
            kdamCoursesList = new short[kdamCourses_strArray.length];
            for (int i = 0; i < kdamCourses_strArray.length; i++) {
                kdamCoursesList[i] = Short.parseShort(kdamCourses_strArray[i]);
            }
        } else
            kdamCoursesList = new short[0];
        numOfMaxStudents = Integer.parseInt(args[3]);
    }

    public short getCourseNumber() {
        return courseNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public short[] getKdamCoursesList() {
        return kdamCoursesList;
    }

    public int getNumOfMaxStudents() {
        return numOfMaxStudents;
    }

    public boolean canRegister(short[] studentCourses) {
        for (short kdam : kdamCoursesList) {
            boolean containsKdam = false;
            for (short studentCourse : studentCourses) {
                if (studentCourse == kdam) {
                    containsKdam = true;
                    break;
                }
            }
            if (!containsKdam) {
                return false;
            }
        }
        return true;
    }
}
