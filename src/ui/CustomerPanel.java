package ui;

import dao.CustomerDAO;
import model.Customer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CustomerPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    
    private JLabel titleLabel, nameLabel, phoneLabel, emailLabel;
    private JTextField nameField, phoneField, emailField;
    private JButton addButton, updateButton, deleteButton, clearButton, backButton;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private CustomerDAO customerDAO;
    
    public CustomerPanel() {
        customerDAO = new CustomerDAO();
        initComponents();
        loadCustomers();
    }
    
    private void initComponents() {
        setLayout(null);
        setBackground(new Color(240, 240, 245));
        
        titleLabel = new JLabel("Customer Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBounds(300, 10, 250, 30);
        add(titleLabel);
        
        nameLabel = new JLabel("Name:");
        nameLabel.setBounds(30, 60, 100, 25);
        add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(140, 60, 180, 25);
        add(nameField);
        
        phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(30, 100, 100, 25);
        add(phoneLabel);
        phoneField = new JTextField();
        phoneField.setBounds(140, 100, 180, 25);
        add(phoneField);
        
        emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 140, 100, 25);
        add(emailLabel);
        emailField = new JTextField();
        emailField.setBounds(140, 140, 180, 25);
        add(emailField);
        
        addButton = new JButton("Add Customer");
        addButton.setBounds(30, 190, 130, 35);
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(this);
        add(addButton);
        
        updateButton = new JButton("Update");
        updateButton.setBounds(180, 190, 100, 35);
        updateButton.setBackground(new Color(52, 152, 219));
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(this);
        add(updateButton);
        
        deleteButton = new JButton("Delete");
        deleteButton.setBounds(180, 235, 100, 35);
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(this);
        add(deleteButton);
        
        clearButton = new JButton("Clear");
        clearButton.setBounds(30, 235, 130, 35);
        clearButton.addActionListener(this);
        add(clearButton);
        
        backButton = new JButton("← Back to Dashboard");
        backButton.setBounds(30, 290, 180, 35);
        backButton.addActionListener(this);
        add(backButton);
        
        String[] columns = {"ID", "Name", "Phone", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        customerTable = new JTable(tableModel);
        customerTable.setRowHeight(25);
        customerTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBounds(380, 60, 380, 300);
        add(scrollPane);
        
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow >= 0) {
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                phoneField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                emailField.setText(tableModel.getValueAt(selectedRow, 3) != null ? 
                    tableModel.getValueAt(selectedRow, 3).toString() : "");
            }
        });
    }
    
    private void loadCustomers() {
        tableModel.setRowCount(0);
        List<Customer> customers = customerDAO.getAllCustomers();
        for (Customer c : customers) {
            Object[] row = {c.getId(), c.getName(), c.getPhone(), c.getEmail()};
            tableModel.addRow(row);
        }
    }
    
    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        customerTable.clearSelection();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) addCustomer();
        else if (e.getSource() == updateButton) updateCustomer();
        else if (e.getSource() == deleteButton) deleteCustomer();
        else if (e.getSource() == clearButton) clearFields();
        else if (e.getSource() == backButton) {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            new DashboardFrame().setVisible(true);
        }
    }
    
    private void addCustomer() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        
        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Phone are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Customer customer = new Customer(name, phone, email);
        boolean success = customerDAO.addCustomer(customer);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Customer added!");
            loadCustomers();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add customer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a customer first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        
        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Phone are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Customer customer = new Customer(id, name, phone, email);
        boolean success = customerDAO.updateCustomer(customer);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Customer updated!");
            loadCustomers();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update customer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a customer first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = tableModel.getValueAt(selectedRow, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, "Delete " + name + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            customerDAO.deleteCustomer(id);
            JOptionPane.showMessageDialog(this, "Customer deleted!");
            loadCustomers();
            clearFields();
        }
    }
}