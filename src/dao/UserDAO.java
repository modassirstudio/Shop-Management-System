package dao;

import database.DBConnection;
import java.sql.*;

public class UserDAO {
    
    public boolean login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            return rs.next();
            
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
            return false;
        }
    }
}