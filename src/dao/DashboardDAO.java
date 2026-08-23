package dao;

import database.DBConnection;
import java.sql.*;

public class DashboardDAO {
    
    public int getTotalProducts() {
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }
    
    public int getTotalCustomers() {
        String sql = "SELECT COUNT(*) FROM customers";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }
    
    public double getTodayRevenue(String date) {
        String sql = "SELECT SUM(total_price) FROM sales WHERE sale_date = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double val = rs.getDouble(1);
                return rs.wasNull() ? 0.0 : val;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0.0;
    }
    
    public double getTodayProfit(String date) {
        String sql = "SELECT SUM(profit) FROM sales WHERE sale_date = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double val = rs.getDouble(1);
                return rs.wasNull() ? 0.0 : val;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0.0;
    }
    
    public int getTodaySalesCount(String date) {
        String sql = "SELECT COUNT(*) FROM sales WHERE sale_date = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }
}	