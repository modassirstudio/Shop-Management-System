package ui;

import dao.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    
    private JLabel titleLabel, usernameLabel, passwordLabel, messageLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, resetButton;
    private UserDAO userDAO;
    
    public LoginFrame() {
        userDAO = new UserDAO();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Shop Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        
        titleLabel = new JLabel("Shop Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(70, 20, 300, 30);
        add(titleLabel);
        
        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 70, 100, 25);
        add(usernameLabel);
        
        usernameField = new JTextField();
        usernameField.setBounds(150, 70, 180, 25);
        add(usernameField);
        
        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 110, 100, 25);
        add(passwordLabel);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 110, 180, 25);
        add(passwordField);
        
        loginButton = new JButton("Login");
        loginButton.setBounds(80, 160, 100, 30);
        loginButton.addActionListener(this);
        add(loginButton);
        
        resetButton = new JButton("Reset");
        resetButton.setBounds(200, 160, 100, 30);
        resetButton.addActionListener(this);
        add(resetButton);
        
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        messageLabel.setBounds(50, 210, 300, 25);
        add(messageLabel);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please fill all fields.");
                return;
            }
            
            boolean success = userDAO.login(username, password);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Welcome to Shop Management System!");
                this.dispose();
                new DashboardFrame().setVisible(true);
            } else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Invalid username or password.");
            }
        } else if (e.getSource() == resetButton) {
            usernameField.setText("");
            passwordField.setText("");
            messageLabel.setText("");
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}