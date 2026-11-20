
package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import model.Student;
import service.ReservationManager;

public class StudentPanel extends JPanel {

    private ReservationManager manager;
    private DefaultTableModel tableModel;
    private JTable studentTable;
    private JLabel totalStudents;

    public StudentPanel(ReservationManager manager) {

        this.manager = manager;

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));

        setBorder(BorderFactory.createEmptyBorder(
            20, 20, 20, 20
        ));

        // Header
        JLabel title = new JLabel("Student Management");
        title.setFont(new Font("Arial", Font.BOLD, 25));

        totalStudents = new JLabel("Total Students: 0");
        totalStudents.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 247, 250));

        header.add(title, BorderLayout.NORTH);
        header.add(totalStudents, BorderLayout.SOUTH);

        // Student Table
        tableModel = new DefaultTableModel(
            new String[]{"Student ID", "Full Name"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(30);
        studentTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane = new JScrollPane(studentTable);

        // Search Field
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(245, 247, 250));

        searchPanel.add(new JLabel("Student ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);

        // Action Buttons
        JButton addButton = new JButton("Add Student");
        JButton deleteButton = new JButton("Delete Student");

        JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        // Top Section
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 247, 250));

        topPanel.add(header, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> addStudent());

        deleteButton.addActionListener(e -> deleteStudent());

        refreshButton.addActionListener(e -> refreshStudents());

        searchButton.addActionListener(e ->
            searchStudent(searchField.getText())
        );

        refreshStudents();
    }

    // Add Student
    private void addStudent() {

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();

        Object[] fields = {
            "Student ID:", idField,
            "Full Name:", nameField
        };

        int result = JOptionPane.showConfirmDialog(
            this,
            fields,
            "Add New Student",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String id = idField.getText().trim();
        String name = nameField.getText().trim();

        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please complete all fields."
            );
            return;
        }

        if (manager.findStudent(id) != null) {
            JOptionPane.showMessageDialog(
                this, "Student ID already exists."
            );
            return;
        }

        Student student = new Student(id, name);

        manager.addStudent(student);

        JOptionPane.showMessageDialog(
            this, "Student added successfully!"
        );

        refreshStudents();
    }

    // Delete Student
    private void deleteStudent() {

        int selectedRow = studentTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select a student."
            );
            return;
        }

        String studentId = tableModel.getValueAt(
            studentTable.convertRowIndexToModel(selectedRow), 0
        ).toString();

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete student " + studentId + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            boolean deleted = manager.removeStudent(studentId);

            if (deleted) {
                JOptionPane.showMessageDialog(
                    this, "Student deleted successfully!"
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Student cannot be deleted. Check existing reservations."
                );
            }

            refreshStudents();
        }
    }

    // Search Student
    private void searchStudent(String studentId) {

        tableModel.setRowCount(0);

        Student student = manager.findStudent(studentId.trim());

        if (student != null) {
            tableModel.addRow(new Object[]{
                student.getUserId(),
                student.getFullName()
            });
        } else {
            JOptionPane.showMessageDialog(
                this, "Student not found."
            );
        }
    }

    // Refresh Student Table
    public void refreshStudents() {

        tableModel.setRowCount(0);

        for (Student student : manager.getStudents()) {

            tableModel.addRow(new Object[]{
                student.getUserId(),
                student.getFullName()
            });
        }

        totalStudents.setText(
            "Total Students: " + manager.getStudents().size()
        );
    }
}
