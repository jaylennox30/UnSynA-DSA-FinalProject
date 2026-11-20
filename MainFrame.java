
package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

import model.*;
import service.ReservationManager;

public class MainFrame extends JFrame {

    private ReservationManager manager;

    private DefaultTableModel studentModel;
    private DefaultTableModel facilityModel;
    private DefaultTableModel reservationModel;

    private JTable studentTable;
    private JTable facilityTable;
    private JTable reservationTable;

    private JLabel studentCount;
    private JLabel facilityCount;
    private JLabel reservationCount;

    public MainFrame(ReservationManager manager) {

        this.manager = manager;

        setTitle("CampuSport - Sports Reservation System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(245, 247, 250));

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Dashboard", createDashboard());
        tabs.addTab("Students", createStudentPanel());
        tabs.addTab("Facilities", createFacilityPanel());
        tabs.addTab("Reservations", createReservationPanel());

        add(tabs);

        refreshTables();
    }

    // Dashboard
    private JPanel createDashboard() {

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("Welcome to CampuSport");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel subtitle = new JLabel(
            "Sports Facility Reservation and Management System"
        );

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(new Color(245, 247, 250));
        header.add(title);
        header.add(subtitle);

        JPanel cards = new JPanel(new GridLayout(1, 3, 15, 15));
        cards.setBackground(new Color(245, 247, 250));

        studentCount = new JLabel("0");
        facilityCount = new JLabel("0");
        reservationCount = new JLabel("0");

        cards.add(createCard("Total Students", studentCount));
        cards.add(createCard("Total Facilities", facilityCount));
        cards.add(createCard("Reservations", reservationCount));

        panel.add(header, BorderLayout.NORTH);
        panel.add(cards, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCard(String title, JLabel count) {

        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(
            new Color(210, 220, 230)
        ));

        JLabel heading = new JLabel(title, SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 16));

        count.setFont(new Font("Arial", Font.BOLD, 30));
        count.setForeground(new Color(25, 120, 90));
        count.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(heading);
        card.add(count);

        return card;
    }

    // Students
    private JPanel createStudentPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        studentModel = new DefaultTableModel(
            new String[]{"Student ID", "Full Name", "Role"}, 0
        );

        studentTable = new JTable(studentModel);

        JButton refresh = new JButton("Refresh");

        refresh.addActionListener(e -> refreshStudents());

        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        panel.add(refresh, BorderLayout.SOUTH);

        return panel;
    }

    // Facilities
    private JPanel createFacilityPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        facilityModel = new DefaultTableModel(
            new String[]{"Facility ID", "Facility Name", "Type", "Status"},
            0
        );

        facilityTable = new JTable(facilityModel);

        JButton refresh = new JButton("Refresh");

        refresh.addActionListener(e -> refreshFacilities());

        panel.add(new JScrollPane(facilityTable), BorderLayout.CENTER);
        panel.add(refresh, BorderLayout.SOUTH);

        return panel;
    }

    // Reservations
    private JPanel createReservationPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        reservationModel = new DefaultTableModel(
            new String[]{
                "Reservation ID", "Student ID", "Facility ID",
                "Date", "Start Time", "End Time", "Status"
            }, 0
        );

        reservationTable = new JTable(reservationModel);

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
            refreshReservations();
        });

        refresh.addActionListener(e -> refreshReservations());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));

        buttons.add(add);
        buttons.add(approve);
        buttons.add(cancel);
        buttons.add(search);
        buttons.add(sort);
        buttons.add(refresh);

        panel.add(new JScrollPane(reservationTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    // Add reservation
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
                LocalDate.parse(date),
                LocalTime.parse(start),
                LocalTime.parse(end)
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
                this, "Invalid reservation details."
            );
        }
    }

    // Approve next reservation
    private void approveReservation() {

        Reservation reservation =
            manager.approveNextReservation();

        if (reservation == null) {
            JOptionPane.showMessageDialog(
                this, "No reservation was approved."
            );
        } else {
            JOptionPane.showMessageDialog(
                this, "Reservation " +
                reservation.getReservationId() +
                " approved successfully!"
            );
        }

        refreshTables();
    }

    // Cancel reservation
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

    // Search reservation
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

    // Refresh all tables
    private void refreshTables() {

        refreshStudents();
        refreshFacilities();
        refreshReservations();

        studentCount.setText(
            String.valueOf(manager.getStudents().size())
        );

        facilityCount.setText(
            String.valueOf(manager.getFacilities().size())
        );

        reservationCount.setText(
            String.valueOf(manager.getReservations().size())
        );
    }

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

    private void refreshReservations() {

        reservationModel.setRowCount(0);

        for (Reservation r : manager.getReservations()) {
            addReservationRow(r);
        }
    }

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
