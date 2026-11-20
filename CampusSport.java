
package app;

import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;

import model.User;
import model.Student;
import model.Administrator;
import model.Facility;
import model.FacilityType;
import model.Reservation;
import model.ReservationStatus;

public class CampuSport {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Student> students = new ArrayList<>();
    static ArrayList<Facility> facilities = new ArrayList<>();
    static ArrayList<Reservation> reservations = new ArrayList<>();

    static Administrator admin =
        new Administrator("ADMIN-001", "System Administrator");

    public static void main(String[] args) {

        loadSampleData();

        int choice;

        do {
            System.out.println("\n==================================");
            System.out.println("          CAMPUSPORT");
            System.out.println(" SPORTS FACILITY RESERVATION SYSTEM");
            System.out.println("==================================");
            System.out.println("1. Student List");
            System.out.println("2. Administrator");
            System.out.println("3. Sports Facilities");
            System.out.println("4. Reservations");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {

                    case 1:
                        displayStudents();
                        break;

                    case 2:
                        admin.displayAdministrator();
                        break;

                    case 3:
                        displayFacilities();
                        break;

                    case 4:
                        displayReservations();
                        break;

                    case 5:
                        System.out.println(
                            "Thank you for using CampuSport!"
                        );
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (NumberFormatException e) {
                choice = 0;
                System.out.println("Please enter a valid number.");
            }

        } while (choice != 5);

        sc.close();
    }

    public static void loadSampleData() {

        students.add(new Student("2024-001", "Juan Dela Cruz"));
        students.add(new Student("2024-002", "Maria Santos"));
        students.add(new Student("2024-003", "Pedro Reyes"));

        facilities.add(new Facility(
            "F001", "Basketball Court", FacilityType.BASKETBALL
        ));

        facilities.add(new Facility(
            "F002", "Volleyball Court", FacilityType.VOLLEYBALL
        ));

        facilities.add(new Facility(
            "F003", "Badminton Court", FacilityType.BADMINTON
        ));

        reservations.add(new Reservation(
            "R001", "2024-001", "F001",
            LocalDate.now().plusDays(1),
            LocalTime.of(8, 0),
            LocalTime.of(10, 0)
        ));

        reservations.get(0).setStatus(
            ReservationStatus.APPROVED
        );
    }

    public static void displayStudents() {

        System.out.println("\n===== STUDENT LIST =====");

        for (Student student : students) {
            student.displayStudent();
        }
    }

    public static void displayFacilities() {

        System.out.println("\n===== SPORTS FACILITIES =====");

        for (Facility facility : facilities) {
            facility.displayFacility();
        }
    }

    public static void displayReservations() {

        System.out.println("\n===== RESERVATION LIST =====");

        for (Reservation reservation : reservations) {
            reservation.displayReservation();
        }
    }
}
