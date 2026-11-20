
package service;

import java.util.ArrayList;

import model.Student;
import model.Facility;
import model.Reservation;
import model.ReservationStatus;
import datastructures.ReservationQueue;

public class ReservationManager {

    private ArrayList<Student> students;
    private ArrayList<Facility> facilities;
    private ArrayList<Reservation> reservations;

    private ReservationQueue pendingQueue;

    public ReservationManager(int queueCapacity) {
        students = new ArrayList<>();
        facilities = new ArrayList<>();
        reservations = new ArrayList<>();
        pendingQueue = new ReservationQueue(queueCapacity);
    }

    // Add student
    public void addStudent(Student student) {
        if (student != null && findStudent(student.getUserId()) == null) {
            students.add(student);
        }
    }

    // Add facility
    public void addFacility(Facility facility) {
        if (facility != null && findFacility(facility.getFacilityId()) == null) {
            facilities.add(facility);
        }
    }

    // Find student
    public Student findStudent(String studentId) {
        for (Student student : students) {
            if (student.getUserId().equalsIgnoreCase(studentId)) {
                return student;
            }
        }
        return null;
    }

    // Find facility
    public Facility findFacility(String facilityId) {
        for (Facility facility : facilities) {
            if (facility.getFacilityId().equalsIgnoreCase(facilityId)) {
                return facility;
            }
        }
        return null;
    }

    // Submit reservation
    public boolean addReservation(Reservation reservation) {

        if (reservation == null
                || reservation.getStatus() != ReservationStatus.PENDING
                || findReservation(reservation.getReservationId()) != null
                || findStudent(reservation.getStudentId()) == null
                || findFacility(reservation.getFacilityId()) == null
                || !isFacilityActive(reservation.getFacilityId())
                || pendingQueue.isFull()
                || checkScheduleConflict(reservation)) {
            return false;
        }

        reservations.add(reservation);
        pendingQueue.enqueue(reservation);

        return true;
    }

    // Find reservation by ID
    public Reservation findReservation(String reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId()
                    .equalsIgnoreCase(reservationId)) {
                return reservation;
            }
        }
        return null;
    }

    // Search reservations by student
    public ArrayList<Reservation> searchByStudent(String studentId) {
        ArrayList<Reservation> results = new ArrayList<>();

        for (Reservation r : reservations) {
            if (r.getStudentId().equalsIgnoreCase(studentId)) {
                results.add(r);
            }
        }

        return results;
    }

    // Check facility activity
    private boolean isFacilityActive(String facilityId) {
        Facility facility = findFacility(facilityId);
        return facility != null && facility.isActive();
    }

    // Check schedule conflicts against approved reservations
    public boolean checkScheduleConflict(Reservation request) {

        for (Reservation existing : reservations) {

            if (existing.getStatus() == ReservationStatus.APPROVED
                    && existing.getFacilityId().equalsIgnoreCase(
                        request.getFacilityId())
                    && existing.getDate().equals(request.getDate())
                    && request.getStartTime().isBefore(existing.getEndTime())
                    && request.getEndTime().isAfter(existing.getStartTime())) {

                return true;
            }
        }

        return false;
    }

    // Approve the first pending reservation (FIFO)
    public Reservation approveNextReservation() {

        Reservation next = pendingQueue.peek();

        if (next == null) {
            return null;
        }

        pendingQueue.dequeue();

        if (next.getStatus() != ReservationStatus.PENDING) {
            return approveNextReservation();
        }

        if (checkScheduleConflict(next)
                || !isFacilityActive(next.getFacilityId())) {
            return null;
        }

        next.setStatus(ReservationStatus.APPROVED);
        return next;
    }

    // Cancel reservation
    public boolean cancelReservation(String reservationId) {

        Reservation reservation = findReservation(reservationId);

        if (reservation == null
                || reservation.getStatus() == ReservationStatus.CANCELLED) {
            return false;
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        return true;
    }

    // Update reservation schedule
    public boolean updateReservation(
            String reservationId,
            java.time.LocalDate date,
            java.time.LocalTime start,
            java.time.LocalTime end) {

        Reservation reservation = findReservation(reservationId);

        if (reservation == null
                || reservation.getStatus() == ReservationStatus.CANCELLED
                || date == null || start == null || end == null
                || !end.isAfter(start)) {
            return false;
        }

        Reservation temporary = new Reservation(
            reservation.getReservationId(),
            reservation.getStudentId(),
            reservation.getFacilityId(),
            date, start, end
        );

        // Ignore the reservation being updated
        for (Reservation existing : reservations) {

            if (existing != reservation
                    && existing.getStatus() == ReservationStatus.APPROVED
                    && existing.getFacilityId().equalsIgnoreCase(
                        reservation.getFacilityId())
                    && existing.getDate().equals(date)
                    && start.isBefore(existing.getEndTime())
                    && end.isAfter(existing.getStartTime())) {

                return false;
            }
        }

        reservation.setSchedule(date, start, end);
        return true;
    }

    // Sort reservations by date and time using insertion sort
    public void sortByDate() {

        for (int i = 1; i < reservations.size(); i++) {

            Reservation key = reservations.get(i);
            int j = i - 1;

            while (j >= 0 &&
                    compareReservations(reservations.get(j), key) > 0) {

                reservations.set(j + 1, reservations.get(j));
                j--;
            }

            reservations.set(j + 1, key);
        }
    }

    private int compareReservations(Reservation a, Reservation b) {

        int dateResult = a.getDate().compareTo(b.getDate());

        if (dateResult != 0) {
            return dateResult;
        }

        return a.getStartTime().compareTo(b.getStartTime());
    }

    // Display students
    public void displayStudents() {
        for (Student student : students) {
            student.displayStudent();
        }
    }

    // Display facilities
    public void displayFacilities() {
        for (Facility facility : facilities) {
            facility.displayFacility();
        }
    }

    // Display reservations
    public void displayReservations() {
        for (Reservation reservation : reservations) {
            reservation.displayReservation();
        }
    }

    // Display pending queue
    public void displayPendingReservations() {
        pendingQueue.displayQueue();
    }

    // Get stored records
    public ArrayList<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public ArrayList<Facility> getFacilities() {
        return new ArrayList<>(facilities);
    }

    public ArrayList<Reservation> getReservations() {
        return new ArrayList<>(reservations);
    }
}
