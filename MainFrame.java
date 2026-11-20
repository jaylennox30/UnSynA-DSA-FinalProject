
package ui;

import service.ReservationManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private ReservationManager manager;

    private DashboardPanel dashboard;
    private AdminPanel adminPanel;
    private StudentPanel studentPanel;
    private FacilityPanel facilityPanel;
    private ReservationPanel reservationPanel;

    public MainFrame(ReservationManager manager) {

        this.manager = manager;

        setTitle("CampuSport - Sports Facility Reservation System");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // MAIN TAB PANEL
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
        reservationPanel = new ReservationPanel(manager);
        tabs.addTab("Reservations", reservationPanel);

        // ADD TABS TO FRAME
        add(tabs, BorderLayout.CENTER);

        // REFRESH ALL PANELS
        refreshTables();
    }

    // ================= REFRESH ALL PANELS =================

    public void refreshTables() {

        // Refresh Student Panel
        studentPanel.refreshStudents();

        // Refresh Facility Panel
        facilityPanel.refreshFacilities();

        // Refresh Reservation Panel
        reservationPanel.refreshReservations();

        // Refresh Dashboard Panel
        dashboard.refreshDashboard();

        // Refresh Admin Panel
        adminPanel.refreshPanel();
    }
}
