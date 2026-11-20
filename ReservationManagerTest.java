
package test;

import model.*;
import service.ReservationManager;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationManagerTest {

    private static ReservationManager manager;

    public static void main(String[] args) {

        manager = new ReservationManager(10);

        System.out.println("==================================");
        System.out.println("   RESERVATION MANAGER TEST");
        System.out.println("==================================");

        testAddStudent();
        testAddFacility();
        testAddReservation();
        testFindReservation();
        testSearchByStudent();
        testApproveReservation();
        testCancelReservation();
        testSortByDate();

        System.out.println("==================================");
        System.out.println("       TESTING COMPLETED");
        System.out.println("==================================");
    }

    // ================= ADD STUDENT =================

    private static void testAddStudent() {

        manager.addStudent(
            new Student("S001", "Juan Dela Cruz")
        );

        if (manager.findStudent("S001") != null) {
            System.out.println("[PASSED] Add Student");
        } else {
            System.out.println("[FAILED] Add Student");
        }
    }

    // ================= ADD FACILITY =================

    private static void testAddFacility() {

        manager.addFacility(
            new Facility(
                "F001",
                "Basketball Court",
                FacilityType.BASKETBALL
            )
        );

        if (manager.findFacility("F001") != null) {
            System.out.println("[PASSED] Add Facility");
        } else {
            System.out.println("[FAILED] Add Facility");
        }
    }

    // ================= ADD RESERVATION =================

    private static void testAddReservation() {

        Reservation reservation = new Reservation(
            "R001",
            "S001",
            "F001",
            LocalDate.now().plusDays(1),
            LocalTime.of(8, 0),
            LocalTime.of(10, 0)
        );

        reservation.setStatus(ReservationStatus.PENDING);

        if (manager.addReservation(reservation)) {
            System.out.println("[PASSED] Add Reservation");
        } else {
            System.out.println("[FAILED] Add Reservation");
        }
    }

    // ================= FIND RESERVATION =================

    private static void testFindReservation() {

        if (manager.findReservation("R001") != null) {
            System.out.println("[PASSED] Find Reservation");
        } else {
            System.out.println("[FAILED] Find Reservation");
        }
    }

    // ================= SEARCH BY STUDENT =================

    private static void testSearchByStudent() {

        if (manager.searchByStudent("S001").size() == 1) {
            System.out.println("[PASSED] Search By Student");
        } else {
            System.out.println("[FAILED] Search By Student");
        }
    }

    // ================= APPROVE RESERVATION =================

    private static void testApproveReservation() {

        Reservation approved = manager.approveNextReservation();

        if (approved != null &&
            approved.getStatus() == ReservationStatus.APPROVED) {

            System.out.println("[PASSED] Approve Reservation");

        } else {

            System.out.println("[FAILED] Approve Reservation");
        }
    }

    // ================= CANCEL RESERVATION =================

    private static void testCancelReservation() {

        Reservation reservation = new Reservation(
            "R002",
            "S001",
            "F001",
            LocalDate.now().plusDays(2),
            LocalTime.of(13, 0),
            LocalTime.of(15, 0)
        );

        reservation.setStatus(ReservationStatus.PENDING);

        manager.addReservation(reservation);

        if (manager.cancelReservation("R002") &&
            manager.findReservation("R002").getStatus()
                == ReservationStatus.CANCELLED) {

            System.out.println("[PASSED] Cancel Reservation");

        } else {

            System.out.println("[FAILED] Cancel Reservation");
        }
    }

    // ================= SORT BY DATE =================

    private static void testSortByDate() {

        manager.sortByDate();

        java.util.List<Reservation> reservations =
            manager.getReservations();

        boolean sorted = true;

        for (int i = 0; i < reservations.size() - 1; i++) {

            Reservation first = reservations.get(i);
            Reservation second = reservations.get(i + 1);

            int comparison = first.getDate().compareTo(second.getDate());

            if (comparison > 0 ||
                (comparison == 0 &&
                 first.getStartTime().isAfter(second.getStartTime()))) {

                sorted = false;
                break;
            }
        }

        if (sorted) {
            System.out.println("[PASSED] Sort By Date");
        } else {
            System.out.println("[FAILED] Sort By Date");
        }
    }
}
