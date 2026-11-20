
package datastructures;

import model.Reservation;

public class ReservationQueue {

    private Reservation[] queue;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public ReservationQueue(int capacity) {

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                "Capacity must be greater than zero."
            );
        }

        this.capacity = capacity;
        queue = new Reservation[capacity];

        front = 0;
        rear = -1;
        size = 0;
    }

    // Add reservation to the queue
    public boolean enqueue(Reservation reservation) {

        if (isFull() || reservation == null) {
            return false;
        }

        rear = (rear + 1) % capacity;
        queue[rear] = reservation;
        size++;

        return true;
    }

    // Remove the first reservation
    public Reservation dequeue() {

        if (isEmpty()) {
            return null;
        }

        Reservation removed = queue[front];

        queue[front] = null;
        front = (front + 1) % capacity;
        size--;

        if (isEmpty()) {
            front = 0;
            rear = -1;
        }

        return removed;
    }

    // View the first reservation
    public Reservation peek() {

        if (isEmpty()) {
            return null;
        }

        return queue[front];
    }

    // Check if queue is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Check if queue is full
    public boolean isFull() {
        return size == capacity;
    }

    // Get current queue size
    public int getSize() {
        return size;
    }

    // Display all pending reservations
    public void displayQueue() {

        if (isEmpty()) {
            System.out.println("No pending reservations.");
            return;
        }

        System.out.println("\n=== PENDING RESERVATIONS ===");

        for (int i = 0; i < size; i++) {

            int index = (front + i) % capacity;

            queue[index].displayReservation();
        }
    }
}
