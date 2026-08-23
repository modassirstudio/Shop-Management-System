-- Shop Management System Database
CREATE DATABASE IF NOT EXISTS shop_management;
USE shop_management;

-- Users table (Admin login)
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'admin'
);

-- Products table
CREATE TABLE IF NOT EXISTS products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    cost_price DECIMAL(10,2) NOT NULL,
    sell_price DECIMAL(10,2) NOT NULL,
    stock INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Customers table
CREATE TABLE IF NOT EXISTS customers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    email VARCHAR(100)
);

-- Sales table
CREATE TABLE IF NOT EXISTS sales (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    profit DECIMAL(10,2) NOT NULL,
    sale_date DATE NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE SET NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Insert default admin user
INSERT INTO users (username, password, role) 
VALUES ('admin', 'admin123', 'admin');

-- Insert sample products (using your real shop products)
INSERT INTO products (name, category, cost_price, sell_price, stock) VALUES
('3/4 inch Pipe', 'Hardware', 167.00, 180.00, 50),
('3 inch Pipe', 'Hardware', 303.00, 325.00, 30),
('Toilet Seat', 'Sanitary', 860.00, 980.00, 20),
('5x4 Syphan', 'Hardware', 290.00, 320.00, 15),
('Solvent 50ml', 'Chemicals', 101.00, 110.00, 100),
('Aree Blade', 'Tools', 9.20, 10.00, 200),
('Tank 1000 Ltr', 'Storage', 8200.00, 8500.00, 5);