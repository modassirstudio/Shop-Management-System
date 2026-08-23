package dao;

import database.DBConnection;
import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    
    // CREATE - Add a new product
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products (name, category, cost_price, sell_price, stock) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getCategory());
            pstmt.setDouble(3, product.getCostPrice());
            pstmt.setDouble(4, product.getSellPrice());
            pstmt.setInt(5, product.getStock());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
            return false;
        }
    }
    
    // READ - Get all products
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM products";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("cost_price"),
                    rs.getDouble("sell_price"),
                    rs.getInt("stock")
                );
                productList.add(product);
            }
            
        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
        
        return productList;
    }
    
    // READ - Get product by ID
    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("cost_price"),
                    rs.getDouble("sell_price"),
                    rs.getInt("stock")
                );
            }
            
        } catch (SQLException e) {
            System.out.println("Error fetching product: " + e.getMessage());
        }
        
        return null;
    }
    
    // UPDATE - Update a product
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name=?, category=?, cost_price=?, sell_price=?, stock=? WHERE id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getCategory());
            pstmt.setDouble(3, product.getCostPrice());
            pstmt.setDouble(4, product.getSellPrice());
            pstmt.setInt(5, product.getStock());
            pstmt.setInt(6, product.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE - Delete a product
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
            return false;
        }
    }
    
    // SEARCH - Search products by name
    public List<Product> searchProducts(String keyword) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("cost_price"),
                    rs.getDouble("sell_price"),
                    rs.getInt("stock")
                );
                productList.add(product);
            }
            
        } catch (SQLException e) {
            System.out.println("Error searching products: " + e.getMessage());
        }
        
        return productList;
    }
}