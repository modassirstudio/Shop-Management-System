package ui;

import dao.DashboardDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class DashboardFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    
    private JButton productButton, salesButton, customerButton, reportsButton, logoutButton;
    private JLabel welcomeLabel;
    private DashboardDAO dashboardDAO;
    
    public DashboardFrame() {
        dashboardDAO = new DashboardDAO();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Shop Management System - Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(240, 240, 245));
        
        welcomeLabel = new JLabel("Shop Management System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeLabel.setBounds(200, 20, 400, 40);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel);
        
        String today = LocalDate.now().toString();
        int totalProducts = dashboardDAO.getTotalProducts();
        int totalCustomers = dashboardDAO.getTotalCustomers();
        double todayRevenue = dashboardDAO.getTodayRevenue(today);
        double todayProfit = dashboardDAO.getTodayProfit(today);
        int todaySales = dashboardDAO.getTodaySalesCount(today);
        
        JPanel salesCard = createCard("🛒 Today's Sales", String.valueOf(todaySales), 
            new Color(52, 152, 219), 30, 90, 220, 100);
        add(salesCard);
        
        JPanel revenueCard = createCard("💰 Today's Revenue", "₹" + String.format("%.0f", todayRevenue), 
            new Color(46, 204, 113), 280, 90, 220, 100);
        add(revenueCard);
        
        JPanel profitCard = createCard("📈 Today's Profit", "₹" + String.format("%.0f", todayProfit), 
            new Color(155, 89, 182), 530, 90, 220, 100);
        add(profitCard);
        
        JPanel productsCard = createCard("📦 Products", String.valueOf(totalProducts), 
            new Color(241, 196, 15), 30, 220, 220, 100);
        add(productsCard);
        
        JPanel customersCard = createCard("👥 Customers", String.valueOf(totalCustomers), 
            new Color(230, 126, 34), 280, 220, 220, 100);
        add(customersCard);
        
        productButton = createMenuButton("📦 Manage Products", 150, 370, 200, 50);
        add(productButton);
        
        salesButton = createMenuButton("💰 New Sale", 420, 370, 200, 50);
        add(salesButton);
        
        customerButton = createMenuButton("👥 Manage Customers", 150, 440, 200, 50);
        add(customerButton);
        
        reportsButton = createMenuButton("📊 View Reports", 420, 440, 200, 50);
        add(reportsButton);
        
        logoutButton = new JButton("🚪 Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 13));
        logoutButton.setBounds(320, 510, 120, 35);
        logoutButton.addActionListener(this);
        add(logoutButton);
    }
    
    private JPanel createCard(String title, String value, Color color, int x, int y, int w, int h) {
        JPanel card = new JPanel();
        card.setBounds(x, y, w, h);
        card.setBackground(color);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        
        return card;
    }
    
    private JButton createMenuButton(String text, int x, int y, int w, int h) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBounds(x, y, w, h);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(this);
        return button;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == productButton) {
            this.dispose();
            JFrame frame = new JFrame("Product Management");
            frame.setSize(800, 500);
            frame.setLocationRelativeTo(null);
            frame.add(new ProductPanel());
            frame.setVisible(true);
        } else if (e.getSource() == salesButton) {
            this.dispose();
            JFrame frame = new JFrame("Sales Management");
            frame.setSize(800, 450);
            frame.setLocationRelativeTo(null);
            frame.add(new SalesPanel());
            frame.setVisible(true);
        } else if (e.getSource() == customerButton) {
            this.dispose();
            JFrame frame = new JFrame("Customer Management");
            frame.setSize(800, 450);
            frame.setLocationRelativeTo(null);
            frame.add(new CustomerPanel());
            frame.setVisible(true);
        } else if (e.getSource() == reportsButton) {
            this.dispose();
            JFrame frame = new JFrame("Reports & Analytics");
            frame.setSize(800, 500);
            frame.setLocationRelativeTo(null);
            frame.add(new ReportsPanel());
            frame.setVisible(true);
        } else if (e.getSource() == logoutButton) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrame().setVisible(true);
            }
        }
    }
}