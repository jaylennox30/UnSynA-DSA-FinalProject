
package ui;

import model.*;
import service.ReservationManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminPanel extends JPanel {

    private ReservationManager manager;

    private JLabel studentCountLabel;
    private JLabel facilityCountLabel;
    private JLabel reservationCountLabel;
    private JLabel pendingCountLabel;

    private DefaultTableModel reservationModel;
    private JTable reservationTable;

    public AdminPanel(ReservationManager manager) {

        this.manager = manager;

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));

        // HEADER
        JLabel title = new JLabel("Administrator Panel");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        JLabel subtitle = new JLabel("Manage and monitor CampuSport activities");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(new Color(245, 247, 250));
        header.add(title);
        header.add(subtitle);

        add(header, BorderLayout.NORTH);

        // MAIN CONTENT
        JPanel content = new JPanel(new BorderLayout(10, 15));
        content.setBackground(new Color(245, 247, 250));
        content.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        // STATISTICS
        JPanel statistics = new JPanel(new GridLayout(1, 4, 10, 10));
        statistics.setBackground(new Color(245, 247, 250));

        studentCountLabel = createStatCard(
            statistics, "Total Students"
        );

        facilityCountLabel = createStatCard(
            statistics, "Total Facilities"
        );

        reservationCountLabel = createStatCard(
            statistics, "Total Reservations"
        );

        pendingCountLabel = createStatCard(
            statistics, "Pending Reservations"
        );

        content.add(statistics, BorderLayout.NORTH);

        // RESERVATION TABLE
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

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(
            BorderFactory.createTitledBorder("All Reservations")
        );

        tablePanel.add(
            new JScrollPane(reservationTable),
            BorderLayout.CENTER
        );

        content.add(tablePanel, BorderLayout.CENTER);

        // BUTTONS
        JButton approveButton = new JButton("Approve Next");
        JButton refreshButton = new JButton("Refresh");

        approveButton.addActionListener(e -> approveReservation());
        refreshButton.addActionListener(e -> refreshPanel());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(approveButton);
        buttons.add(refreshButton);

        content.add(buttons, BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);

        refreshPanel();
    }

    // CREATE STAT CARD
    private JLabel createStatCard(JPanel parent, String title) {

        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
            )
        );

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel valueLabel = new JLabel("0", SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(new Color(30, 90, 160));

        card.add(titleLabel);
        card.add(valueLabel);

        parent.add(card);

        return valueLabel;
    }

    // APPROVE RESERVATION
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

        refreshPanel();
    }

    // REFRESH ADMIN PANEL
    public void refreshPanel() {

        studentCountLabel.setText(
            String.valueOf(manager.getStudents().size())
        );

        facilityCountLabel.setText(
            String.valueOf(manager.getFacilities().size())
        );

        reservationCountLabel.setText(
            String.valueOf(manager.getReservations().size())
        );

        int pending = 0;

        for (Reservation r : manager.getReservations()) {
            if (r.getStatus() == ReservationStatus.PENDING) {
                pending++;
            }
        }

        pendingCountLabel.setText(String.valueOf(pending));

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
    }
}
