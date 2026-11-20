
public boolean removeReservation(String reservationId) {

    if (isEmpty()) {
        return false;
    }

    Reservation[] temporary = new Reservation[capacity];
    int count = 0;
    boolean removed = false;

    while (!isEmpty()) {

        Reservation current = dequeue();

        if (!removed && current.getReservationId()
                .equalsIgnoreCase(reservationId)) {
            removed = true;
        } else {
            temporary[count++] = current;
        }
    }

    for (int i = 0; i < count; i++) {
        enqueue(temporary[i]);
    }

    return removed;
}
