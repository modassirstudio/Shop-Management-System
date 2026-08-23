package dao;

import database.DBConnection;
import model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    
    // CREATE - Add a new customer
    public boolean addCustomer(Customer customer) {
        String sql = "INSERT INTO customers (name, phone, email) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getPhone());
            pstmt.setString(3, customer.getEmail());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error adding customer: " + e.getMessage());
            return false;
        }
    }
    
    // READ - Get all customers
    public List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email")
                );
                customerList.add(customer);
            }
            
        } catch (SQLException e) {
            System.out.println("Error fetching customers: " + e.getMessage());
        }
        
        return customerList;
    }
    
    // READ - Get customer by phone
    public Customer getCustomerByPhone(String phone) {
        String sql = "SELECT * FROM customers WHERE phone = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, phone);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Customer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email")
                );
            }
            
        } catch (SQLException e) {
            System.out.println("Error fetching customer: " + e.getMessage());
        }
        
        return null;
    }
    
    // UPDATE - Update customer
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET name=?, phone=?, email=? WHERE id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getPhone());
            pstmt.setString(3, customer.getEmail());
            pstmt.setInt(4, customer.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE - Delete customer
    public boolean deleteCustomer(int id) {
        String sql = "DELETE FROM customers WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }
}