
package model;

public class Student extends User {

    public Student(String studentId, String fullName) {
        super(studentId, fullName, "Student");
    }

    public void displayStudent() {
        System.out.println(
            getUserId() + " - " + getFullName()
        );
    }
}
