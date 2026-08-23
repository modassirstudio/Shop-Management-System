package model;

public class Sale {
    
    private int id;
    private int customerId;
    private int productId;
    private int quantity;
    private double totalPrice;
    private double profit;
    private String saleDate;
    
    // Constructor 1: Without ID (for adding new sale)
    public Sale(int customerId, int productId, int quantity, double totalPrice, double profit, String saleDate) {
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.profit = profit;
        this.saleDate = saleDate;
    }
    
    // Constructor 2: With ID (for existing sale from database)
    public Sale(int id, int customerId, int productId, int quantity, double totalPrice, double profit, String saleDate) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.profit = profit;
        this.saleDate = saleDate;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public double getProfit() {
        return profit;
    }
    
    public void setProfit(double profit) {
        this.profit = profit;
    }
    
    public String getSaleDate() {
        return saleDate;
    }
    
    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }
    
    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", profit=" + profit +
                ", saleDate='" + saleDate + '\'' +
                '}';
    }
}