package ui;

import dao.ProductDAO;
import model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    
    private JLabel titleLabel, nameLabel, categoryLabel, costLabel, sellLabel, stockLabel, searchLabel;
    private JTextField nameField, categoryField, costField, sellField, stockField, searchField;
    private JButton addButton, updateButton, deleteButton, searchButton, clearButton, backButton;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private ProductDAO productDAO;
    
    public ProductPanel() {
        productDAO = new ProductDAO();
        initComponents();
        loadProducts();
    }
    
    private void initComponents() {
        setLayout(null);
        setBackground(new Color(240, 240, 245));
        
        titleLabel = new JLabel("Product Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBounds(300, 10, 250, 30);
        add(titleLabel);
        
        nameLabel = new JLabel("Product Name:");
        nameLabel.setBounds(30, 60, 100, 25);
        add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(140, 60, 180, 25);
        add(nameField);
        
        categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(30, 95, 100, 25);
        add(categoryLabel);
        categoryField = new JTextField();
        categoryField.setBounds(140, 95, 180, 25);
        add(categoryField);
        
        costLabel = new JLabel("Cost Price:");
        costLabel.setBounds(30, 130, 100, 25);
        add(costLabel);
        costField = new JTextField();
        costField.setBounds(140, 130, 180, 25);
        add(costField);
        
        sellLabel = new JLabel("Sell Price:");
        sellLabel.setBounds(30, 165, 100, 25);
        add(sellLabel);
        sellField = new JTextField();
        sellField.setBounds(140, 165, 180, 25);
        add(sellField);
        
        stockLabel = new JLabel("Stock:");
        stockLabel.setBounds(30, 200, 100, 25);
        add(stockLabel);
        stockField = new JTextField();
        stockField.setBounds(140, 200, 180, 25);
        add(stockField);
        
        addButton = new JButton("Add Product");
        addButton.setBounds(30, 250, 130, 35);
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(this);
        add(addButton);
        
        updateButton = new JButton("Update");
        updateButton.setBounds(180, 250, 100, 35);
        updateButton.setBackground(new Color(52, 152, 219));
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(this);
        add(updateButton);
        
        deleteButton = new JButton("Delete");
        deleteButton.setBounds(180, 295, 100, 35);
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(this);
        add(deleteButton);
        
        clearButton = new JButton("Clear");
        clearButton.setBounds(30, 295, 130, 35);
        clearButton.addActionListener(this);
        add(clearButton);
        
        searchLabel = new JLabel("Search:");
        searchLabel.setBounds(380, 60, 60, 25);
        add(searchLabel);
        searchField = new JTextField();
        searchField.setBounds(440, 60, 150, 25);
        add(searchField);
        
        searchButton = new JButton("🔍");
        searchButton.setBounds(600, 60, 60, 25);
        searchButton.addActionListener(this);
        add(searchButton);
        
        backButton = new JButton("← Back to Dashboard");
        backButton.setBounds(30, 350, 180, 35);
        backButton.addActionListener(this);
        add(backButton);
        
        String[] columns = {"ID", "Name", "Category", "Cost Price", "Sell Price", "Stock"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        productTable = new JTable(tableModel);
        productTable.setRowHeight(25);
        productTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBounds(380, 100, 380, 350);
        add(scrollPane);
        
        productTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow >= 0) {
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                categoryField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                costField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                sellField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                stockField.setText(tableModel.getValueAt(selectedRow, 5).toString());
            }
        });
    }
    
    private void loadProducts() {
        tableModel.setRowCount(0);
        List<Product> products = productDAO.getAllProducts();
        for (Product p : products) {
            Object[] row = {p.getId(), p.getName(), p.getCategory(), p.getCostPrice(), p.getSellPrice(), p.getStock()};
            tableModel.addRow(row);
        }
    }
    
    private void clearFields() {
        nameField.setText("");
        categoryField.setText("");
        costField.setText("");
        sellField.setText("");
        stockField.setText("");
        productTable.clearSelection();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) addProduct();
        else if (e.getSource() == updateButton) updateProduct();
        else if (e.getSource() == deleteButton) deleteProduct();
        else if (e.getSource() == searchButton) searchProducts();
        else if (e.getSource() == clearButton) clearFields();
        else if (e.getSource() == backButton) {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            new DashboardFrame().setVisible(true);
        }
    }
    
    private void addProduct() {
        String name = nameField.getText();
        String category = categoryField.getText();
        String costStr = costField.getText();
        String sellStr = sellField.getText();
        String stockStr = stockField.getText();
        
        if (name.isEmpty() || category.isEmpty() || costStr.isEmpty() || sellStr.isEmpty() || stockStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            double costPrice = Double.parseDouble(costStr);
            double sellPrice = Double.parseDouble(sellStr);
            int stock = Integer.parseInt(stockStr);
            
            Product product = new Product(name, category, costPrice, sellPrice, stock);
            boolean success = productDAO.addProduct(product);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Product added successfully!");
                loadProducts();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format for price or stock.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a product to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = nameField.getText();
        String category = categoryField.getText();
        
        try {
            double costPrice = Double.parseDouble(costField.getText());
            double sellPrice = Double.parseDouble(sellField.getText());
            int stock = Integer.parseInt(stockField.getText());
            
            Product product = new Product(id, name, category, costPrice, sellPrice, stock);
            boolean success = productDAO.updateProduct(product);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
                loadProducts();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = tableModel.getValueAt(selectedRow, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete " + name + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = productDAO.deleteProduct(id);
            if (success) {
                JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                loadProducts();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void searchProducts() {
        String keyword = searchField.getText();
        if (keyword.isEmpty()) {
            loadProducts();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Product> products = productDAO.searchProducts(keyword);
        for (Product p : products) {
            Object[] row = {p.getId(), p.getName(), p.getCategory(), p.getCostPrice(), p.getSellPrice(), p.getStock()};
            tableModel.addRow(row);
        }
    }
}