package bgu.spl.net.impl.user;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class Database {
    private static Database singleton;
    private ConcurrentSkipListSet<String> connected_users;
    private ConcurrentHashMap<String, String> students_userNamesPassWords_Table;
    private ConcurrentHashMap<String, String> admin_userNamesPassWords_Table;
    private HashMap<Short, Course> courses;
    private ArrayList<Short> coursesFileOrder;
    private ConcurrentHashMap<Short, ConcurrentSkipListSet<String>> courses_to_students;

    //to prevent user from creating new Database
    private Database() {
        students_userNamesPassWords_Table = new ConcurrentHashMap<>();
        admin_userNamesPassWords_Table = new ConcurrentHashMap<>();
        courses_to_students = new ConcurrentHashMap<>();
        connected_users = new ConcurrentSkipListSet<>();
        courses = new HashMap<>();
        coursesFileOrder = new ArrayList<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Database getInstance() {
        if (singleton == null)
            singleton = new Database();
        return singleton;
    }

    /**
     * loades the courses from the file path specified
     * into the Database, returns true if successful.
     */
    public boolean initialize(String coursesFilePath) {

        try (Scanner reader = new Scanner(Paths.get(coursesFilePath))) {

            while (reader.hasNext()) {
                String str = reader.nextLine();
                Course c = new Course(str);
                courses.put(c.getCourseNumber(), c);
                courses_to_students.put(c.getCourseNumber(), new ConcurrentSkipListSet<>());
                coursesFileOrder.add(c.getCourseNumber());
            }
        } catch (IOException e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }

    private boolean isUsernameExistInDatabase(String Username) {
        return admin_userNamesPassWords_Table.containsKey(Username) |
                students_userNamesPassWords_Table.containsKey(Username);
    }


    public void registerAdmin(String Username, String Password) throws Exception {
        if (!isUsernameExistInDatabase(Username)) {
            admin_userNamesPassWords_Table.put(Username, Password);
            return;
        }
        throw new Exception("User " + Username + " is already registered");
    }


    public void registerStudent(String Username, String Password) throws Exception {
        if (!isUsernameExistInDatabase(Username)) {
            students_userNamesPassWords_Table.put(Username, Password);
            return;
        }
        throw new Exception("User " + Username + " is already registered");
    }

    public Role login(String Username, String Password) throws Exception {
        if (connected_users.contains(Username))
            throw new Exception("Username " + Username + " is logged in currently");
        if (admin_userNamesPassWords_Table.containsKey(Username)) {
            if (admin_userNamesPassWords_Table.get(Username).equals(Password)) {
                connectUser(Username);
                return Role.Admin;
            }
        } else if (students_userNamesPassWords_Table.containsKey(Username))
            if (students_userNamesPassWords_Table.get(Username).equals(Password)) {
                connectUser(Username);
                return Role.Student;
            }
        throw new Exception("Username " + Username + " is not registered yet");
    }

    private void connectUser(String username) {
        connected_users.add(username);
    }

    public void logout(String Username) throws Exception {
        if (connected_users.contains(Username)) {
            connected_users.remove(Username);
            return;
        }
        if (isUsernameExistInDatabase(Username))
            throw new Exception("Username " + Username + " was not logged in");
        throw new Exception("Username " + Username + " is not registered yet");
    }

    public void registerToCourse(String username, short courseNumber) throws Exception {
        if (students_userNamesPassWords_Table.containsKey(username)) {
            if (!courses.containsKey(courseNumber))
                throw new Exception("Failed because there is no such course as " +
                        courseNumber);
            ConcurrentSkipListSet<String> studentNames = courses_to_students.get(courseNumber);
            if (studentNames.contains(username))
                throw new Exception("Failed because " + username + " is already " +
                        "registered to course " + courseNumber);
            Course course = courses.get(courseNumber);
            LinkedList<Short> kdamCourses = new LinkedList<>();
            for (short kdam : course.getKdamCoursesList()) {
                ConcurrentSkipListSet<String> registeredStudentsToCourse = courses_to_students.get(kdam);
                if(registeredStudentsToCourse != null) {
                    if (!registeredStudentsToCourse.contains(username))
                        kdamCourses.push(kdam);
                }else{
                    throw new Exception("Kdam Course " + kdam + " does not exist.");
                }

            }
            if (!kdamCourses.isEmpty()) {
                if (kdamCourses.size() == 1)
                    throw new Exception("not registered to " + kdamCourses.pop() +
                            " , which is a Kdam course for " + courseNumber);
                else
                    throw new Exception("not registered to " + kdamCourses.toString() +
                            " , which are Kdam courses for " + courseNumber);
            }
            if (studentNames.size() == courses.size())
                throw new Exception("Failed because no seats are available" +
                        " in course " + courseNumber);
            studentNames.add(username);
        } else if (admin_userNamesPassWords_Table.containsKey(username))

            throw new Exception("Failed because " + username + " is an admin");
        else
            throw new Exception("Failed because username " + username + " is not" +
                    "registered");

    }

    public String checkKdam(short courseNumber) throws Exception {
        if (courses.containsKey(courseNumber))
            if (courses.get(courseNumber).haveKdams())
                return Arrays.toString(courses.get(courseNumber).getKdamCoursesList());
            else
                return "[]";
        else
            throw new Exception("Course " + courseNumber + " does not exist.");
    }

    public String admin_courseStats(short courseNumber) throws Exception {
        if (!courses.containsKey(courseNumber)) {
            throw new Exception("There are no stats for the course " + courseNumber +
                    " as it does not exist");
        }
        Course c = courses.get(courseNumber);
        int seatsTaken = courses_to_students.get(courseNumber).size();
        String registeredStudents = courses_to_students.get(courseNumber).toString();
        return "Course: (" + courseNumber + ") " + c.getCourseName() + "\n" +
                "Seats Available: " + (c.getNumOfMaxStudents() - seatsTaken) + "/" + c.getNumOfMaxStudents() + "\n" +
                "Students Registered: " + registeredStudents;
    }

    public String admin_studentStats(String username) throws Exception {
        if (admin_userNamesPassWords_Table.containsKey(username))
            throw new Exception("There are no stats for the student " + username +
                    " because it is an admin");
        if (!students_userNamesPassWords_Table.containsKey(username))
            throw new Exception("There are no stats for the student " + username +
                    " as it is not registered");

        return "Student: " + username + "\n" +
                "Courses: " + getStudentCourses(username);
    }

    public boolean isRegistered(String username, short courseNumber) throws Exception {
        if (admin_userNamesPassWords_Table.containsKey(username))
            throw new Exception("The username " + username + " belongs to an admin");
        if (!students_userNamesPassWords_Table.containsKey(username))
            throw new Exception("The user " + username + " is not registered");
        if (!courses_to_students.containsKey(courseNumber))
            throw new Exception("Course " + courseNumber + " does not exist.");
        return courses_to_students.get(courseNumber).contains(username);
    }

    public void unregister(String username, short courseNumber) throws Exception {
        if (isRegistered(username, courseNumber)) {
            courses_to_students.get(courseNumber).remove(username);
        }else {
            throw new Exception("Student is not registered to course " + courseNumber);
        }
    }

    public String getStudentCourses(String username) {
        List<Short> students_courses = new ArrayList<>();
        for (short course : coursesFileOrder) {
            ConcurrentSkipListSet<String> students = courses_to_students.get(course);
            if(students.contains(username))
                students_courses.add(course);
        }
        return students_courses.toString();
    }

    /*public String admin_GetCourseStats() {
        return "Course: ("++ ") "++ "\n" +
                "Seats Available: "++ "/"++ "\n" +
                "Students Registered: "++ "\n" ;
    }*/

}