package ui;

import dao.SaleDAO;
import dao.ProductDAO;
import model.Sale;
import model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class ReportsPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    
    private JLabel titleLabel;
    private JButton todaySalesBtn, todayRevenueBtn, todayProfitBtn, allSalesBtn, topProductsBtn, exportBtn, backButton;
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JTextArea summaryArea;
    private SaleDAO saleDAO;
    private ProductDAO productDAO;
    
    public ReportsPanel() {
        saleDAO = new SaleDAO();
        productDAO = new ProductDAO();
        initComponents();
    }
    
    private void initComponents() {
        setLayout(null);
        setBackground(new Color(240, 240, 245));
        
        titleLabel = new JLabel("Reports & Analytics");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBounds(300, 10, 250, 30);
        add(titleLabel);
        
        todaySalesBtn = new JButton("Today's Sales");
        todaySalesBtn.setBounds(30, 60, 180, 35);
        todaySalesBtn.addActionListener(this);
        add(todaySalesBtn);
        
        todayRevenueBtn = new JButton("Today's Revenue");
        todayRevenueBtn.setBounds(30, 105, 180, 35);
        todayRevenueBtn.setBackground(new Color(46, 204, 113));
        todayRevenueBtn.setForeground(Color.WHITE);
        todayRevenueBtn.addActionListener(this);
        add(todayRevenueBtn);
        
        todayProfitBtn = new JButton("Today's Profit");
        todayProfitBtn.setBounds(30, 150, 180, 35);
        todayProfitBtn.setBackground(new Color(52, 152, 219));
        todayProfitBtn.setForeground(Color.WHITE);
        todayProfitBtn.addActionListener(this);
        add(todayProfitBtn);
        
        allSalesBtn = new JButton("All Sales");
        allSalesBtn.setBounds(30, 195, 180, 35);
        allSalesBtn.addActionListener(this);
        add(allSalesBtn);
        
        topProductsBtn = new JButton("Product Inventory");
        topProductsBtn.setBounds(30, 240, 180, 35);
        topProductsBtn.setBackground(new Color(155, 89, 182));
        topProductsBtn.setForeground(Color.WHITE);
        topProductsBtn.addActionListener(this);
        add(topProductsBtn);
        
        exportBtn = new JButton("📥 Export to Excel");
        exportBtn.setBounds(30, 285, 180, 35);
        exportBtn.setBackground(new Color(241, 196, 15));
        exportBtn.addActionListener(this);
        add(exportBtn);
        
        backButton = new JButton("← Back to Dashboard");
        backButton.setBounds(30, 335, 180, 35);
        backButton.addActionListener(this);
        add(backButton);
        
        summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Arial", Font.PLAIN, 13));
        summaryArea.setBackground(Color.WHITE);
        JScrollPane summaryScroll = new JScrollPane(summaryArea);
        summaryScroll.setBounds(240, 60, 250, 250);
        summaryScroll.setBorder(BorderFactory.createTitledBorder("Summary"));
        add(summaryScroll);
        
        String[] columns = {"ID", "Prod ID", "Cust ID", "Qty", "Total", "Profit", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        reportTable = new JTable(tableModel);
        reportTable.setRowHeight(25);
        reportTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        
        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBounds(500, 60, 270, 350);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Details"));
        add(scrollPane);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == todaySalesBtn) showTodaySales();
        else if (e.getSource() == todayRevenueBtn) showTodayRevenue();
        else if (e.getSource() == todayProfitBtn) showTodayProfit();
        else if (e.getSource() == allSalesBtn) showAllSales();
        else if (e.getSource() == topProductsBtn) showTopProducts();
        else if (e.getSource() == exportBtn) exportToExcel();
        else if (e.getSource() == backButton) {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            new DashboardFrame().setVisible(true);
        }
    }
    
    private void showTodaySales() {
        String today = LocalDate.now().toString();
        List<Sale> sales = saleDAO.getTodaySales(today);
        tableModel.setRowCount(0);
        double totalRevenue = 0;
        double totalProfit = 0;
        
        for (Sale s : sales) {
            Object[] row = {s.getId(), s.getProductId(), s.getCustomerId(), 
                s.getQuantity(), "₹" + s.getTotalPrice(), "₹" + s.getProfit(), s.getSaleDate()};
            tableModel.addRow(row);
            totalRevenue += s.getTotalPrice();
            totalProfit += s.getProfit();
        }
        
        summaryArea.setText("📅 TODAY'S SALES REPORT\n");
        summaryArea.append("Date: " + today + "\n\n");
        summaryArea.append("Total Transactions: " + sales.size() + "\n");
        summaryArea.append("Total Revenue: ₹" + String.format("%.2f", totalRevenue) + "\n");
        summaryArea.append("Total Profit: ₹" + String.format("%.2f", totalProfit) + "\n");
    }
    
    private void showTodayRevenue() {
        String today = LocalDate.now().toString();
        double revenue = saleDAO.getTotalRevenue(today);
        summaryArea.setText("💰 TODAY'S REVENUE\n\n");
        summaryArea.append("Date: " + today + "\n\n");
        summaryArea.append("Total Revenue: ₹" + String.format("%.2f", revenue));
        
        tableModel.setRowCount(0);
        List<Sale> sales = saleDAO.getTodaySales(today);
        for (Sale s : sales) {
            Object[] row = {s.getId(), s.getProductId(), s.getCustomerId(), 
                s.getQuantity(), "₹" + s.getTotalPrice(), "₹" + s.getProfit(), s.getSaleDate()};
            tableModel.addRow(row);
        }
    }
    
    private void showTodayProfit() {
        String today = LocalDate.now().toString();
        double profit = saleDAO.getTotalProfit(today);
        double revenue = saleDAO.getTotalRevenue(today);
        summaryArea.setText("📈 TODAY'S PROFIT\n\n");
        summaryArea.append("Date: " + today + "\n\n");
        summaryArea.append("Total Revenue: ₹" + String.format("%.2f", revenue) + "\n");
        summaryArea.append("Total Profit: ₹" + String.format("%.2f", profit) + "\n");
        
        if (revenue > 0) {
            double margin = (profit / revenue) * 100;
            summaryArea.append("Profit Margin: " + String.format("%.1f", margin) + "%");
        }
        
        tableModel.setRowCount(0);
        List<Sale> sales = saleDAO.getTodaySales(today);
        for (Sale s : sales) {
            Object[] row = {s.getId(), s.getProductId(), s.getCustomerId(), 
                s.getQuantity(), "₹" + s.getTotalPrice(), "₹" + s.getProfit(), s.getSaleDate()};
            tableModel.addRow(row);
        }
    }
    
    private void showAllSales() {
        List<Sale> sales = saleDAO.getAllSales();
        tableModel.setRowCount(0);
        double totalRevenue = 0;
        double totalProfit = 0;
        
        for (Sale s : sales) {
            Object[] row = {s.getId(), s.getProductId(), s.getCustomerId(), 
                s.getQuantity(), "₹" + s.getTotalPrice(), "₹" + s.getProfit(), s.getSaleDate()};
            tableModel.addRow(row);
            totalRevenue += s.getTotalPrice();
            totalProfit += s.getProfit();
        }
        
        summaryArea.setText("📋 ALL SALES REPORT\n\n");
        summaryArea.append("Total Transactions: " + sales.size() + "\n");
        summaryArea.append("Total Revenue: ₹" + String.format("%.2f", totalRevenue) + "\n");
        summaryArea.append("Total Profit: ₹" + String.format("%.2f", totalProfit) + "\n");
    }
    
    private void showTopProducts() {
        List<Product> products = productDAO.getAllProducts();
        tableModel.setRowCount(0);
        
        summaryArea.setText("🏆 PRODUCT INVENTORY\n\n");
        summaryArea.append("Total Products: " + products.size() + "\n\n");
        
        double totalInventoryValue = 0;
        for (Product p : products) {
            totalInventoryValue += (p.getSellPrice() * p.getStock());
        }
        
        summaryArea.append("Total Inventory Value: ₹" + String.format("%.2f", totalInventoryValue));
        
        for (Product p : products) {
            Object[] row = {p.getId(), p.getName(), p.getCategory(), 
                "Stock: " + p.getStock(), "₹" + p.getSellPrice(), "₹" + (p.getCostPrice()), ""};
            tableModel.addRow(row);
        }
    }
    
    private void exportToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Excel File");
        fileChooser.setSelectedFile(new java.io.File("Sales_Report_" + LocalDate.now() + ".csv"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            
            try (java.io.PrintWriter writer = new java.io.PrintWriter(fileToSave)) {
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    writer.print(tableModel.getColumnName(i));
                    if (i < tableModel.getColumnCount() - 1) writer.print(",");
                }
                writer.println();
                
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    for (int col = 0; col < tableModel.getColumnCount(); col++) {
                        writer.print(tableModel.getValueAt(row, col));
                        if (col < tableModel.getColumnCount() - 1) writer.print(",");
                    }
                    writer.println();
                }
                
                JOptionPane.showMessageDialog(this, "Report exported successfully!\n" + fileToSave.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error exporting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}