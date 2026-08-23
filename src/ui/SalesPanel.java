package ui;

import dao.ProductDAO;
import dao.SaleDAO;
import model.Product;
import model.Sale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class SalesPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    
    private JLabel titleLabel, productLabel, quantityLabel, totalLabel, profitLabel;
    private JComboBox<String> productCombo;
    private JTextField quantityField, totalField, profitField;
    private JButton sellButton, clearButton, backButton;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private ProductDAO productDAO;
    private SaleDAO saleDAO;
    private List<Product> productList;
    
    public SalesPanel() {
        productDAO = new ProductDAO();
        saleDAO = new SaleDAO();
        initComponents();
        loadProducts();
    }
    
    private void initComponents() {
        setLayout(null);
        setBackground(new Color(240, 240, 245));
        
        titleLabel = new JLabel("Make a Sale");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBounds(300, 10, 200, 30);
        add(titleLabel);
        
        productLabel = new JLabel("Select Product:");
        productLabel.setBounds(30, 60, 100, 25);
        add(productLabel);
        productCombo = new JComboBox<>();
        productCombo.setBounds(140, 60, 200, 25);
        productCombo.addActionListener(this);
        add(productCombo);
        
        quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(30, 100, 100, 25);
        add(quantityLabel);
        quantityField = new JTextField();
        quantityField.setBounds(140, 100, 100, 25);
        add(quantityField);
        
        quantityField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { calculateTotal(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { calculateTotal(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { calculateTotal(); }
        });
        
        totalLabel = new JLabel("Total Price:");
        totalLabel.setBounds(30, 140, 100, 25);
        add(totalLabel);
        totalField = new JTextField();
        totalField.setBounds(140, 140, 150, 25);
        totalField.setEditable(false);
        add(totalField);
        
        profitLabel = new JLabel("Profit:");
        profitLabel.setBounds(30, 180, 100, 25);
        add(profitLabel);
        profitField = new JTextField();
        profitField.setBounds(140, 180, 150, 25);
        profitField.setEditable(false);
        add(profitField);
        
        sellButton = new JButton("💵 Complete Sale");
        sellButton.setBounds(30, 230, 180, 40);
        sellButton.setBackground(new Color(46, 204, 113));
        sellButton.setForeground(Color.WHITE);
        sellButton.setFont(new Font("Arial", Font.BOLD, 14));
        sellButton.addActionListener(this);
        add(sellButton);
        
        clearButton = new JButton("Clear");
        clearButton.setBounds(220, 230, 100, 40);
        clearButton.addActionListener(this);
        add(clearButton);
        
        backButton = new JButton("← Back to Dashboard");
        backButton.setBounds(30, 290, 200, 35);
        backButton.addActionListener(this);
        add(backButton);
        
        String[] columns = {"ID", "Product", "Price", "Stock"};
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
        scrollPane.setBounds(380, 60, 380, 300);
        add(scrollPane);
    }
    
    private void loadProducts() {
        productList = productDAO.getAllProducts();
        productCombo.removeAllItems();
        tableModel.setRowCount(0);
        
        for (Product p : productList) {
            productCombo.addItem(p.getId() + " - " + p.getName() + " (Stock: " + p.getStock() + ")");
            Object[] row = {p.getId(), p.getName(), "₹" + p.getSellPrice(), p.getStock()};
            tableModel.addRow(row);
        }
    }
    
    private void calculateTotal() {
        int selectedIndex = productCombo.getSelectedIndex();
        if (selectedIndex < 0) return;
        
        Product selectedProduct = productList.get(selectedIndex);
        String qtyText = quantityField.getText();
        
        if (!qtyText.isEmpty()) {
            try {
                int quantity = Integer.parseInt(qtyText);
                double totalPrice = selectedProduct.getSellPrice() * quantity;
                double profit = (selectedProduct.getSellPrice() - selectedProduct.getCostPrice()) * quantity;
                
                totalField.setText("₹" + String.format("%.2f", totalPrice));
                profitField.setText("₹" + String.format("%.2f", profit));
            } catch (NumberFormatException ex) {
                totalField.setText("");
                profitField.setText("");
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == productCombo) calculateTotal();
        else if (e.getSource() == sellButton) makeSale();
        else if (e.getSource() == clearButton) {
            quantityField.setText("");
            totalField.setText("");
            profitField.setText("");
            productCombo.setSelectedIndex(0);
        }
        else if (e.getSource() == backButton) {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            new DashboardFrame().setVisible(true);
        }
    }
    
    private void makeSale() {
        int selectedIndex = productCombo.getSelectedIndex();
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(this, "Select a product.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Product selectedProduct = productList.get(selectedIndex);
        String qtyText = quantityField.getText();
        
        if (qtyText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int quantity = Integer.parseInt(qtyText);
            
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (quantity > selectedProduct.getStock()) {
                JOptionPane.showMessageDialog(this, "Not enough stock! Available: " + selectedProduct.getStock(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double totalPrice = selectedProduct.getSellPrice() * quantity;
            double profit = (selectedProduct.getSellPrice() - selectedProduct.getCostPrice()) * quantity;
            String today = LocalDate.now().toString();
            
            Sale sale = new Sale(0, selectedProduct.getId(), quantity, totalPrice, profit, today);
            boolean success = saleDAO.addSale(sale);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Sale Complete!\n\nProduct: " + selectedProduct.getName() +
                    "\nQuantity: " + quantity +
                    "\nTotal: ₹" + String.format("%.2f", totalPrice) +
                    "\nProfit: ₹" + String.format("%.2f", profit));
                
                loadProducts();
                quantityField.setText("");
                totalField.setText("");
                profitField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Sale failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}