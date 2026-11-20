
package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import model.Student;
import model.Facility;
import model.Reservation;
import service.ReservationManager;

public class MainFrame extends JFrame {

    private ReservationManager manager;
    private DashboardPanel dashboard;

    private DefaultTableModel studentModel;
    private DefaultTableModel facilityModel;
    private DefaultTableModel reservationModel;

    private JTable studentTable;
    private JTable facilityTable;
    private JTable reservationTable;

    public MainFrame(ReservationManager manager) {

        this.manager = manager;

        setTitle("CampuSport - Sports Reservation System");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(
            new Color(245, 247, 250)
        );

        // Main tabs
        JTabbedPane tabs = new JTabbedPane();

        // Updated DashboardPanel connection
        dashboard = new DashboardPanel(manager);

        tabs.addTab("Dashboard", dashboard);
        tabs.addTab("Students", createStudentPanel());
        tabs.addTab("Facilities", createFacilityPanel());
        tabs.addTab("Reservations", createReservationPanel());

        add(tabs);

        refreshTables();
    }

    // Student Panel
    private JPanel createStudentPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        studentModel = new DefaultTableModel(
            new String[]{"Student ID", "Full Name", "Role"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(studentModel);
        studentTable.setRowHeight(28);

        JButton refresh = new JButton("Refresh");

        refresh.addActionListener(e -> {
            refreshStudents();
        });

        panel.add(
            new JScrollPane(studentTable),
            BorderLayout.CENTER
        );

        panel.add(refresh, BorderLayout.SOUTH);

        return panel;
    }

    // Facility Panel
    private JPanel createFacilityPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        facilityModel = new DefaultTableModel(
            new String[]{
                "Facility ID", "Facility Name",
                "Type", "Status"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        facilityTable = new JTable(facilityModel);
        facilityTable.setRowHeight(28);

        JButton refresh = new JButton("Refresh");

        refresh.addActionListener(e -> {
            refreshFacilities();
        });

        panel.add(
            new JScrollPane(facilityTable),
            BorderLayout.CENTER
        );

        panel.add(refresh, BorderLayout.SOUTH);

        return panel;
    }

    // Reservation Panel
    private JPanel createReservationPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        reservationModel = new DefaultTableModel(
            new String[]{
                "Reservation ID",
                "Student ID",
                "Facility ID",
                "Date",
                "Start Time",
                "End Time",
                "Status"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reservationTable = new JTable(reservationModel);
        reservationTable.setRowHeight(28);

        JButton add = new JButton("Make Reservation");
        JButton approve = new JButton("Approve Next");
        JButton cancel = new JButton("Cancel");
        JButton search = new JButton("Search");
        JButton sort = new JButton("Sort by Date");
        JButton refresh = new JButton("Refresh");

        add.addActionListener(e -> addReservation());
        approve.addActionListener(e -> approveReservation());
        cancel.addActionListener(e -> cancelReservation());
        search.addActionListener(e -> searchReservation());

        sort.addActionListener(e -> {
            manager.sortByDate();
            refreshTables();
        });

        refresh.addActionListener(e -> refreshTables());

        JPanel buttons = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );

        buttons.add(add);
        buttons.add(approve);
        buttons.add(cancel);
        buttons.add(search);
        buttons.add(sort);
        buttons.add(refresh);

        panel.add(
            new JScrollPane(reservationTable),
            BorderLayout.CENTER
        );

        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    // Add Reservation
    private void addReservation() {

        try {

            String id = JOptionPane.showInputDialog(
                this, "Reservation ID:"
            );

            if (id == null) return;

            String studentId = JOptionPane.showInputDialog(
                this, "Student ID:"
            );

            if (studentId == null) return;

            String facilityId = JOptionPane.showInputDialog(
                this, "Facility ID:"
            );

            if (facilityId == null) return;

            String date = JOptionPane.showInputDialog(
                this, "Date (YYYY-MM-DD):"
            );

            if (date == null) return;

            String start = JOptionPane.showInputDialog(
                this, "Start Time (HH:MM):"
            );

            if (start == null) return;

            String end = JOptionPane.showInputDialog(
                this, "End Time (HH:MM):"
            );

            if (end == null) return;

            Reservation reservation = new Reservation(
                id,
                studentId,
                facilityId,
                java.time.LocalDate.parse(date),
                java.time.LocalTime.parse(start),
                java.time.LocalTime.parse(end)
            );

            if (manager.addReservation(reservation)) {

                JOptionPane.showMessageDialog(
                    this, "Reservation submitted successfully!"
                );

            } else {

                JOptionPane.showMessageDialog(
                    this, "Reservation could not be submitted."
                );
            }

            refreshTables();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                this, "Invalid reservation details!"
            );
        }
    }

    // Approve Reservation
    private void approveReservation() {

        Reservation reservation =
            manager.approveNextReservation();

        if (reservation == null) {

            JOptionPane.showMessageDialog(
                this, "No reservation was approved."
            );

        } else {

            JOptionPane.showMessageDialog(
                this,
                "Reservation " +
                reservation.getReservationId() +
                " approved successfully!"
            );
        }

        refreshTables();
    }

    // Cancel Reservation
    private void cancelReservation() {

        String id = JOptionPane.showInputDialog(
            this, "Enter Reservation ID:"
        );

        if (id == null || id.trim().isEmpty()) return;

        if (manager.cancelReservation(id)) {

            JOptionPane.showMessageDialog(
                this, "Reservation cancelled successfully!"
            );

        } else {

            JOptionPane.showMessageDialog(
                this, "Reservation not found or already cancelled."
            );
        }

        refreshTables();
    }

    // Search Reservation
    private void searchReservation() {

        String id = JOptionPane.showInputDialog(
            this, "Enter Student ID:"
        );

        if (id == null) return;

        reservationModel.setRowCount(0);

        for (Reservation r : manager.searchByStudent(id)) {
            addReservationRow(r);
        }
    }

    // Refresh All Tables and Dashboard
    private void refreshTables() {

        refreshStudents();
        refreshFacilities();
        refreshReservations();

        dashboard.refreshDashboard();
    }

    // Refresh Students
    private void refreshStudents() {

        studentModel.setRowCount(0);

        for (Student s : manager.getStudents()) {

            studentModel.addRow(new Object[]{
                s.getUserId(),
                s.getFullName(),
                s.getRole()
            });
        }
    }

    // Refresh Facilities
    private void refreshFacilities() {

        facilityModel.setRowCount(0);

        for (Facility f : manager.getFacilities()) {

            facilityModel.addRow(new Object[]{
                f.getFacilityId(),
                f.getFacilityName(),
                f.getFacilityType(),
                f.isActive() ? "Available" : "Inactive"
            });
        }
    }

    // Refresh Reservations
    private void refreshReservations() {

        reservationModel.setRowCount(0);

        for (Reservation r : manager.getReservations()) {
            addReservationRow(r);
        }
    }

    // Add Reservation to Table
    private void addReservationRow(Reservation r) {

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
}
