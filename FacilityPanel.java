
package ui;

import model.Facility;
import model.FacilityType;
import service.ReservationManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FacilityPanel extends JPanel {

    private ReservationManager manager;

    private JTable facilityTable;
    private DefaultTableModel tableModel;

    public FacilityPanel(ReservationManager manager) {

        this.manager = manager;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));

        // HEADER
        JLabel title = new JLabel("Facility Management");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        // TABLE
        String[] columns = {
            "Facility ID",
            "Facility Name",
            "Facility Type",
            "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        facilityTable = new JTable(tableModel);
        facilityTable.setRowHeight(25);

        // BUTTONS
        JButton addButton = new JButton("Add Facility");
        JButton searchButton = new JButton("Search");
        JButton activateButton = new JButton("Activate");
        JButton deactivateButton = new JButton("Deactivate");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> addFacility());
        searchButton.addActionListener(e -> searchFacility());
        activateButton.addActionListener(e -> updateFacilityStatus(true));
        deactivateButton.addActionListener(e -> updateFacilityStatus(false));
        deleteButton.addActionListener(e -> deleteFacility());
        refreshButton.addActionListener(e -> refreshFacilities());

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(activateButton);
        buttonPanel.add(deactivateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(facilityTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshFacilities();
    }

    // ================= ADD FACILITY =================

    private void addFacility() {

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();

        JComboBox<FacilityType> typeBox =
            new JComboBox<>(FacilityType.values());

        Object[] fields = {
            "Facility ID:", idField,
            "Facility Name:", nameField,
            "Facility Type:", typeBox
        };

        int result = JOptionPane.showConfirmDialog(
            this,
            fields,
            "Add New Facility",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {

                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                FacilityType type =
                    (FacilityType) typeBox.getSelectedItem();

                if (id.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Please complete all fields."
                    );
                    return;
                }

                Facility facility = new Facility(id, name, type);

                boolean added = manager.addFacility(facility);

                if (added) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Facility added successfully!"
                    );

                } else {

                    JOptionPane.showMessageDialog(
                        this,
                        "Unable to add facility. Check if the ID already exists."
                    );
                }

                refreshFacilities();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                    this,
                    "An error occurred while adding the facility."
                );
            }
        }
    }

    // ================= SEARCH FACILITY =================

    private void searchFacility() {

        String id = JOptionPane.showInputDialog(
            this,
            "Enter Facility ID:"
        );

        if (id == null || id.trim().isEmpty()) {
            return;
        }

        Facility facility = manager.findFacility(id.trim());

        if (facility != null) {

            JOptionPane.showMessageDialog(
                this,
                "Facility ID: " + facility.getFacilityId()
                + "\nFacility Name: " + facility.getFacilityName()
                + "\nFacility Type: " + facility.getFacilityType()
                + "\nStatus: " + (facility.isActive() ? "Active" : "Inactive")
            );

        } else {

            JOptionPane.showMessageDialog(
                this,
                "Facility not found."
            );
        }
    }

    // ================= UPDATE FACILITY STATUS =================

    private void updateFacilityStatus(boolean active) {

        int selectedRow = facilityTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Please select a facility first."
            );

            return;
        }

        String facilityId = tableModel.getValueAt(
            selectedRow, 0
        ).toString();

        boolean updated = manager.updateFacilityStatus(
            facilityId,
            active
        );

        if (updated) {

            JOptionPane.showMessageDialog(
                this,
                active
                    ? "Facility activated successfully!"
                    : "Facility deactivated successfully!"
            );

        } else {

            JOptionPane.showMessageDialog(
                this,
                "Unable to update facility status."
            );
        }

        refreshFacilities();
    }

    // ================= DELETE FACILITY =================

    private void deleteFacility() {

        int selectedRow = facilityTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Please select a facility first."
            );

            return;
        }

        String facilityId = tableModel.getValueAt(
            selectedRow, 0
        ).toString();

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this facility?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            boolean deleted = manager.removeFacility(facilityId);

            if (deleted) {

                JOptionPane.showMessageDialog(
                    this,
                    "Facility deleted successfully!"
                );

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Unable to delete facility."
                );
            }

            refreshFacilities();
        }
    }

    // ================= REFRESH FACILITIES =================

    public void refreshFacilities() {

        tableModel.setRowCount(0);

        for (Facility f : manager.getFacilities()) {

            tableModel.addRow(new Object[]{
                f.getFacilityId(),
                f.getFacilityName(),
                f.getFacilityType(),
                f.isActive() ? "Active" : "Inactive"
            });
        }
    }
}
