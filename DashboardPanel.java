
package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import model.Reservation;
import model.ReservationStatus;
import service.ReservationManager;

public class DashboardPanel extends JPanel {

    private ReservationManager manager;

    private JLabel studentCount;
    private JLabel facilityCount;
    private JLabel reservationCount;
    private JLabel pendingCount;

    private DefaultTableModel tableModel;

    public DashboardPanel(ReservationManager manager) {

        this.manager = manager;

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));

        setBorder(BorderFactory.createEmptyBorder(
            25, 25, 25, 25
        ));

        // Header
        JLabel title = new JLabel("Welcome to CampuSport");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel subtitle = new JLabel(
            "Sports Facility Reservation Dashboard"
        );

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(new Color(245, 247, 250));

        header.add(title);
        header.add(subtitle);

        // Statistics
        JPanel cards = new JPanel(new GridLayout(1, 4, 15, 15));
        cards.setBackground(new Color(245, 247, 250));

        studentCount = new JLabel("0");
        facilityCount = new JLabel("0");
        reservationCount = new JLabel("0");
        pendingCount = new JLabel("0");

        cards.add(createCard("Total Students", studentCount));
        cards.add(createCard("Facilities", facilityCount));
        cards.add(createCard("Reservations", reservationCount));
        cards.add(createCard("Pending", pendingCount));

        // Recent Reservations
        String[] columns = {
            "Reservation ID",
            "Student ID",
            "Facility ID",
            "Date",
            "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(28);

        JLabel recentTitle = new JLabel("Recent Reservations");
        recentTitle.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel recentPanel = new JPanel(new BorderLayout(10, 10));
        recentPanel.setBackground(Color.WHITE);
        recentPanel.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JButton refresh = new JButton("Refresh Dashboard");
        refresh.addActionListener(e -> refreshDashboard());

        recentPanel.add(recentTitle, BorderLayout.NORTH);
        recentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        recentPanel.add(refresh, BorderLayout.SOUTH);

        // Main Layout
        JPanel top = new JPanel(new BorderLayout(15, 20));
        top.setBackground(new Color(245, 247, 250));

        top.add(header, BorderLayout.NORTH);
        top.add(cards, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        add(recentPanel, BorderLayout.CENTER);

        refreshDashboard();
    }

    // Create statistic card
    private JPanel createCard(String title, JLabel count) {

        JPanel card = new JPanel(new GridLayout(2, 1, 5, 5));

        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(220, 225, 230)
            ),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));

        JLabel heading = new JLabel(title, SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 14));

        count.setFont(new Font("Arial", Font.BOLD, 28));
        count.setForeground(new Color(25, 120, 90));
        count.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(heading);
        card.add(count);

        return card;
    }

    // Refresh dashboard information
    public void refreshDashboard() {

        studentCount.setText(
            String.valueOf(manager.getStudents().size())
        );

        facilityCount.setText(
            String.valueOf(manager.getFacilities().size())
        );

        reservationCount.setText(
            String.valueOf(manager.getReservations().size())
        );

        long pending = manager.getReservations().stream()
            .filter(r -> r.getStatus() == ReservationStatus.PENDING)
            .count();

        pendingCount.setText(String.valueOf(pending));

        tableModel.setRowCount(0);

        for (Reservation r : manager.getReservations()) {

            tableModel.addRow(new Object[]{
                r.getReservationId(),
                r.getStudentId(),
                r.getFacilityId(),
                r.getDate(),
                r.getStatus()
            });
        }
    }
}
