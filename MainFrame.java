
package ui;

import model.*;
import service.ReservationManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class MainFrame extends JFrame {

    private ReservationManager manager;

    private DashboardPanel dashboard;
    private AdminPanel adminPanel;
    private StudentPanel studentPanel;
    private FacilityPanel facilityPanel;

    private DefaultTableModel reservationModel;
    private JTable reservationTable;

    public MainFrame(ReservationManager manager) {

        this.manager = manager;

        setTitle("CampuSport - Sports Facility Reservation System");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        // DASHBOARD PANEL
        dashboard = new DashboardPanel(manager);
        tabs.addTab("Dashboard", dashboard);

        // ADMIN PANEL
        adminPanel = new AdminPanel(manager);
        tabs.addTab("Admin", adminPanel);

        // STUDENT PANEL
        studentPanel = new StudentPanel(manager);
        tabs.addTab("Students", studentPanel);

        // FACILITY PANEL
        facilityPanel = new FacilityPanel(manager);
        tabs.addTab("Facilities", facilityPanel);

        // RESERVATION PANEL
        tabs.addTab("Reservations", createReservationPanel());

        add(tabs);

        refreshTables();
    }

    // ================= RESERVATION PANEL =================

    private JPanel createReservationPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] columns = {
            "Reservation ID",
            "Student ID",
            "Facility ID",
            "Date",
            "Start Time",
            "End Time",
            "Status"
        };

        reservationModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reservationTable = new JTable(reservationModel);
        reservationTable.setRowHeight(25);

        panel.add(
            new JScrollPane(reservationTable),
            BorderLayout.CENTER
        );

        // BUTTONS
        JButton addButton = new JButton("Make Reservation");
        JButton approveButton = new JButton("Approve Next");
        JButton cancelButton = new JButton("Cancel Reservation");
        JButton searchButton = new JButton("Search by Student");
        JButton sortButton = new JButton("Sort by Date");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> addReservation());
        approveButton.addActionListener(e -> approveNextReservation());
        cancelButton.addActionListener(e -> cancelReservation());
        searchButton.addActionListener(e -> searchReservation());
        sortButton.addActionListener(e -> sortReservations());
        refreshButton.addActionListener(e -> refreshTables());

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(addButton);
        buttonPanel.add(approveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
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
                        "Unable to submit reservation. Please check the details."
                    );
                }

                refreshTables();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                    this,
                    "Invalid input. Please check your date and time format."
                );
            }
        }
    }

    // ================= APPROVE RESERVATION =================

    private void approveNextReservation() {

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

        refreshTables();
    }

    // ================= CANCEL RESERVATION =================

    private void cancelReservation() {

        String reservationId = JOptionPane.showInputDialog(
            this,
            "Enter Reservation ID to cancel:"
        );

        if (reservationId == null || reservationId.trim().isEmpty()) {
            return;
        }

        boolean cancelled = manager.cancelReservation(
            reservationId.trim()
        );

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

        refreshTables();
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

        java.util.List<Reservation> results =
            manager.searchByStudent(studentId.trim());

        reservationModel.setRowCount(0);

        for (Reservation r : results) {

            reservationModel.addRow(new Object[]{
                r.getReservationId(),
                r.getStudentId(),
                r.getFacilityId(),
                r.getDate(),
                r.getStartTime(),
                r.getEndTime(),
                r.getStatus()
            });
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

        refreshTables();

        JOptionPane.showMessageDialog(
            this,
            "Reservations sorted by date successfully!"
        );
    }

    // ================= REFRESH ALL PANELS =================

    public void refreshTables() {

        // STUDENT PANEL
        studentPanel.refreshStudents();

        // FACILITY PANEL
        facilityPanel.refreshFacilities();

        // RESERVATION TABLE
        reservationModel.setRowCount(0);

        for (Reservation r : manager.getReservations()) {

            reservationModel.addRow(new Object[]{
                r.getReservationId(),
                r.getStudentId(),
                r.getFacilityId(),
                r.getDate(),
                r.getStartTime(),
                r.getEndTime(),
                r.getStatus()
            });
        }

        // DASHBOARD PANEL
        dashboard.refreshDashboard();

        // ADMIN PANEL
        adminPanel.refreshPanel();
    }
}
