
package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {

    private String reservationId;
    private String studentId;
    private String facilityId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private ReservationStatus status;

    public Reservation(String reservationId, String studentId,
                       String facilityId, LocalDate date,
                       LocalTime startTime, LocalTime endTime) {

        if (reservationId == null || reservationId.trim().isEmpty()
                || studentId == null || studentId.trim().isEmpty()
                || facilityId == null || facilityId.trim().isEmpty()
                || date == null || startTime == null || endTime == null
                || !endTime.isAfter(startTime)) {
            throw new IllegalArgumentException(
                "Invalid reservation details."
            );
        }

        this.reservationId = reservationId;
        this.studentId = studentId;
        this.facilityId = facilityId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = ReservationStatus.PENDING;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void displayReservation() {
        System.out.println(
            reservationId + " | " + studentId +
            " | " + facilityId + " | " + date +
            " | " + startTime + "-" + endTime +
            " | " + status
        );
    }
}
