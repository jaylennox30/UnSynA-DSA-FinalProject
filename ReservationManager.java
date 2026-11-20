public void addReservation(Reservation r) {
    reservations.add(r);
    pendingQueue.enqueue(r);
}

public void cancelReservation(String id) {
    // Find and cancel reservation by ID
}

public void searchByStudent(String studentId) {
    // Display reservations belonging to student
}

public void displayFacilities() {
    // Display available sports facilities
}
