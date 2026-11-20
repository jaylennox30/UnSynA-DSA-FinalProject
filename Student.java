public class Student extends User {

    public Student(String userId, String fullName) {
        super(userId, fullName, "Student");
    }

    public void submitReservation(Reservation r, ReservationManager rm) {
        rm.addReservation(r);
    }

    public void cancelReservation(String id, ReservationManager rm) {
        rm.cancelReservation(id);
    }

    public void viewMyReservations(ReservationManager rm) {
        rm.searchByStudent(userId);
    }

    public void checkAvailability(String facilityId, ReservationManager rm) {
        rm.displayFacilities();
    }
}
