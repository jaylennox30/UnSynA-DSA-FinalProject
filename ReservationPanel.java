
package ui;

import model.Reservation;
import service.ReservationManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservationPanel extends JPanel {

    private ReservationManager manager;

    private JTable reservationTable;
    private DefaultTableModel tableModel;

    public ReservationPanel(ReservationManager manager) {

        this.manager = manager;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));

        // HEADER
        JLabel title = new JLabel("Reservation Management");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 5, 15)
        );

        // TABLE
        String[] columns = {
            "Reservation ID",
            "Student ID",
            "Facility ID",
            "Date",
            "Start Time",
            "End Time",
            "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reservationTable = new JTable(tableModel);
        reservationTable.setRowHeight(25);

        // BUTTONS
        JButton addButton = new JButton("Make Reservation");
        JButton approveButton = new JButton("Approve Next");
        JButton cancelButton = new JButton("Cancel Reservation");
        JButton searchButton = new JButton("Search by Student");
        JButton sortButton = new JButton("Sort by Date");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> addReservation());
        approveButton.addActionListener(e -> approveReservation());
        cancelButton.addActionListener(e -> cancelReservation());
        searchButton.addActionListener(e -> searchReservation());
        sortButton.addActionListener(e -> sortReservations());
        refreshButton.addActionListener(e -> refreshReservations());

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(addButton);
        buttonPanel.add(approveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(refreshButton);

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(reservationTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshReservations();
    }

    // ================= ADD RESERVATION =================

    private void addReservation() {

        JTextField idField = new JTextField();
        JTextField studentField = new JTextField();
        JTextField facilityField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField startField = new JTextField();
        JTextField endField = new JTextField();

        Object[] fields = {
            "Reservation ID:", idField,
            "Student ID:", studentField,
            "Facility ID:", facilityField,
            "Date (YYYY-MM-DD):", dateField,
            "Start Time (HH:MM):", startField,
            "End Time (HH:MM):", endField
        };

        int result = JOptionPane.showConfirmDialog(
            this,
            fields,
            "Make Reservation",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {

                Reservation reservation = new Reservation(
                    idField.getText().trim(),
                    studentField.getText().trim(),
                    facilityField.getText().trim(),
                    LocalDate.parse(dateField.getText().trim()),
                    LocalTime.parse(startField.getText().trim()),
                    LocalTime.parse(endField.getText().trim())
                );

                boolean added = manager.addReservation(reservation);

                if (added) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Reservation submitted successfully!"
                    );

                } else {

                    JOptionPane.showMessageDialog(
                        this,
                        "Unable to submit reservation. Check the details."
                    );
                }

                refreshReservations();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                    this,
                    "Invalid input. Use YYYY-MM-DD for date and HH:MM for time."
                );
            }
        }
    }

    // ================= APPROVE RESERVATION =================

    private void approveReservation() {

        Reservation approved = manager.approveNextReservation();

        if (approved != null) {

            JOptionPane.showMessageDialog(
                this,
                "Reservation approved successfully!"
            );

        } else {

            JOptionPane.showMessageDialog(
                this,
                "No reservation was approved."
            );
        }

        refreshReservations();
    }

    // ================= CANCEL RESERVATION =================

    private void cancelReservation() {

        String id = JOptionPane.showInputDialog(
            this,
            "Enter Reservation ID to cancel:"
        );

        if (id == null || id.trim().isEmpty()) {
            return;
        }

        boolean cancelled = manager.cancelReservation(id.trim());

        if (cancelled) {

            JOptionPane.showMessageDialog(
                this,
                "Reservation cancelled successfully!"
            );

        } else {

            JOptionPane.showMessageDialog(
                this,
                "Reservation not found or cannot be cancelled."
            );
        }

        refreshReservations();
    }

    // ================= SEARCH RESERVATION =================

    private void searchReservation() {

        String studentId = JOptionPane.showInputDialog(
            this,
            "Enter Student ID:"
        );

        if (studentId == null || studentId.trim().isEmpty()) {
            return;
        }

        List<Reservation> results =
            manager.searchByStudent(studentId.trim());

        tableModel.setRowCount(0);

        for (Reservation r : results) {

            addReservationRow(r);
        }

        if (results.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "No reservations found for this student."
            );
        }
    }

    // ================= SORT RESERVATIONS =================

    private void sortReservations() {

        manager.sortByDate();

        refreshReservations();

        JOptionPane.showMessageDialog(
            this,
            "Reservations sorted by date successfully!"
        );
    }

    // ================= REFRESH RESERVATIONS =================

    public void refreshReservations() {

        tableModel.setRowCount(0);

        for (Reservation r : manager.getReservations()) {

            addReservationRow(r);
        }
    }

    // ================= ADD TABLE ROW =================

    private void addReservationRow(Reservation r) {

        tableModel.addRow(new Object[]{
            r.getReservationId(),
            r.getStudentId(),
            r.getFacilityId(),
            r.getDate(),
            r.getStartTime(),
            r.getEndTime(),
            r.getStatus()
        });
    }
}
