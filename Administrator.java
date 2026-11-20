
package model;

public class Administrator extends User {

    public Administrator(String adminId, String fullName) {
        super(adminId, fullName, "Administrator");
    }

    public void displayAdministrator() {
        System.out.println(
            getUserId() + " - " + getFullName()
        );
    }
}
