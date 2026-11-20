
import javax.swing.SwingUtilities;

import model.Student;
import model.Administrator;
import model.Facility;
import model.FacilityType;
import model.Reservation;
import model.ReservationStatus;

import service.ReservationManager;
import ui.MainFrame;

import java.time.LocalDate;
import java.time.LocalTime;

public class CampuSport {

    public static void main(String[] args) {

        ReservationManager manager =
            new ReservationManager(100);

        // Sample students
        manager.addStudent(
            new Student("2024-001", "Juan Dela Cruz")
        );

        manager.addStudent(
            new Student("2024-002", "Maria Santos")
        );

        manager.addStudent(
            new Student("2024-003", "Pedro Reyes")
        );

        // Sample facilities
        manager.addFacility(
            new Facility(
                "F001", "Basketball Court",
                FacilityType.BASKETBALL
            )
        );

        manager.addFacility(
            new Facility(
                "F002", "Volleyball Court",
                FacilityType.VOLLEYBALL
            )
        );

        manager.addFacility(
            new Facility(
                "F003", "Badminton Court",
                FacilityType.BADMINTON
            )
        );

        // Sample reservation
        Reservation sample = new Reservation(
            "R001",
            "2024-001",
            "F001",
            LocalDate.now().plusDays(1),
            LocalTime.of(8, 0),
            LocalTime.of(10, 0)
        );

        manager.addReservation(sample);
        sample.setStatus(ReservationStatus.APPROVED);

        // Launch application
        SwingUtilities.invokeLater(() -> {
            new MainFrame(manager).setVisible(true);
        });
    }
}
