package dao;

import database.DBConnection;
import model.Sale;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO {
    
    // CREATE - Make a sale (profit calculated automatically)
    public boolean addSale(Sale sale) {
        String sql = "INSERT INTO sales (customer_id, product_id, quantity, total_price, profit, sale_date) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (sale.getCustomerId() == 0) {
                pstmt.setNull(1, Types.INTEGER);
            } else {
                pstmt.setInt(1, sale.getCustomerId());
            }
            
            pstmt.setInt(2, sale.getProductId());
            pstmt.setInt(3, sale.getQuantity());
            pstmt.setDouble(4, sale.getTotalPrice());
            pstmt.setDouble(5, sale.getProfit());
            pstmt.setString(6, sale.getSaleDate());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                updateStock(sale.getProductId(), sale.getQuantity());
            }
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error adding sale: " + e.getMessage());
            return false;
        }
    }
    
    // READ - Get all sales
    public List<Sale> getAllSales() {
        List<Sale> saleList = new ArrayList<>();
        String sql = "SELECT * FROM sales ORDER BY sale_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Sale sale = new Sale(
                    rs.getInt("id"),
                    rs.getInt("customer_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("total_price"),
                    rs.getDouble("profit"),
                    rs.getString("sale_date")
                );
                saleList.add(sale);
            }
            
        } catch (SQLException e) {
            System.out.println("Error fetching sales: " + e.getMessage());
        }
        
        return saleList;
    }
    
    // READ - Get today's sales
    public List<Sale> getTodaySales(String date) {
        List<Sale> saleList = new ArrayList<>();
        String sql = "SELECT * FROM sales WHERE sale_date = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Sale sale = new Sale(
                    rs.getInt("id"),
                    rs.getInt("customer_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("total_price"),
                    rs.getDouble("profit"),
                    rs.getString("sale_date")
                );
                saleList.add(sale);
            }
            
        } catch (SQLException e) {
            System.out.println("Error fetching today sales: " + e.getMessage());
        }
        
        return saleList;
    }
    
    // UPDATE STOCK - Reduce product stock after sale
    private void updateStock(int productId, int quantitySold) {
        String sql = "UPDATE products SET stock = stock - ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, quantitySold);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Error updating stock: " + e.getMessage());
        }
    }
    
    // REPORT - Get total revenue for a date
    public double getTotalRevenue(String date) {
        String sql = "SELECT SUM(total_price) AS total FROM sales WHERE sale_date = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting revenue: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    // REPORT - Get total profit for a date
    public double getTotalProfit(String date) {
        String sql = "SELECT SUM(profit) AS total FROM sales WHERE sale_date = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting profit: " + e.getMessage());
        }
        
        return 0.0;
    }
}