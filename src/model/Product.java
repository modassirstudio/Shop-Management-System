package model;

public class Product {
    
    // Fields (same as database columns)
    private int id;
    private String name;
    private String category;
    private double costPrice;
    private double sellPrice;
    private int stock;
    
    // Constructor 1: Without ID (for adding new product)
    public Product(String name, String category, double costPrice, double sellPrice, int stock) {
        this.name = name;
        this.category = category;
        this.costPrice = costPrice;
        this.sellPrice = sellPrice;
        this.stock = stock;
    }
    
    // Constructor 2: With ID (for existing product from database)
    public Product(int id, String name, String category, double costPrice, double sellPrice, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.costPrice = costPrice;
        this.sellPrice = sellPrice;
        this.stock = stock;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public double getCostPrice() {
        return costPrice;
    }
    
    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }
    
    public double getSellPrice() {
        return sellPrice;
    }
    
    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }
    
    public int getStock() {
        return stock;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    // toString method (for displaying product in console)
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", costPrice=" + costPrice +
                ", sellPrice=" + sellPrice +
                ", stock=" + stock +
                '}';
    }
}