-- Database Creation
CREATE DATABASE IF NOT EXISTS santhuongmai;
USE santhuongmai;

-- User Management Tables
CREATE TABLE User (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    nickname VARCHAR(100),
    email VARCHAR(100) NOT NULL UNIQUE,
    avatar VARCHAR(255),
    user_role ENUM('Admin', 'Customer', 'Staff') NOT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    password VARCHAR(255) NOT NULL -- Added password field for authentication
);
ALTER TABLE User
MODIFY user_role ENUM('Admin', 'Customer', 'Staff', 'Seller') NOT NULL DEFAULT 'Customer';
CREATE TABLE Customer (
    user_id INT PRIMARY KEY,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Staff (
    user_id INT PRIMARY KEY,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Admin (
    user_id INT PRIMARY KEY,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Seller (
    user_id INT PRIMARY KEY,
    status ENUM('pending', 'approved', 'reject') DEFAULT 'pending',
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

ALTER TABLE Seller
ADD COLUMN account_balance DECIMAL(15, 2) NOT NULL DEFAULT 0;

ALTER TABLE Seller
MODIFY  status ENUM('PENDING', 'APPROVED', 'REJECT') DEFAULT 'PENDING';
-- Shop and Category Tables
CREATE TABLE Shop (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    seller_id INT NOT NULL UNIQUE, -- Enforcing 1:1 with Seller
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (seller_id) REFERENCES Seller(user_id) ON DELETE CASCADE
);

CREATE TABLE Category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- Product Tables
CREATE TABLE Product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    price DECIMAL(15, 2) NOT NULL,
    shop_id INT NOT NULL,
    description TEXT,
    category_id INT NOT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (shop_id) REFERENCES Shop(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES Category(id) ON DELETE RESTRICT
);


ALTER TABLE Product
ADD COLUMN inventory_quantity INT Null default 0;


-- Product Attributes
CREATE TABLE Attribute (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    value VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ProductAttribute (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    attribute_id INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES Product(id) ON DELETE CASCADE,
    FOREIGN KEY (attribute_id) REFERENCES Attribute(id) ON DELETE CASCADE
);
ALTER TABLE ProductAttribute
ADD COLUMN value VARCHAR(255);

UPDATE ProductAttribute pa
JOIN Attribute a ON pa.attribute_id = a.id
SET pa.value = a.value
WHERE pa.id IS NOT NULL;

ALTER TABLE Attribute
DROP COLUMN value;
-- Tag System
CREATE TABLE Tag (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE TagDetail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    tag_id INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES Product(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES Tag(id) ON DELETE CASCADE
);

-- Rating and Review Tables
CREATE TABLE SellerRating (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    seller_id INT NOT NULL,
    rate ENUM('1', '2', '3', '4', '5') NOT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Customer(user_id) ON DELETE CASCADE,
    FOREIGN KEY (seller_id) REFERENCES Seller(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_seller_rating (customer_id, seller_id) -- Ensures unique rating per customer-seller pair
);

CREATE TABLE SellerReview (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    seller_id INT NOT NULL,
    content TEXT NOT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Customer(user_id) ON DELETE CASCADE,
    FOREIGN KEY (seller_id) REFERENCES Seller(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_seller_review (customer_id, seller_id) -- Ensures unique review per customer-seller pair
);

CREATE TABLE ProductRating (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    product_id INT NOT NULL,
    rate ENUM('1', '2', '3', '4', '5') NOT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Customer(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Product(id) ON DELETE CASCADE,
    UNIQUE KEY unique_product_rating (customer_id, product_id) -- Ensures unique rating per customer-product pair
);

CREATE TABLE Comment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    product_id INT NOT NULL,
    content TEXT NOT NULL,
    reply_comment_id INT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Customer(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Product(id) ON DELETE CASCADE,
    FOREIGN KEY (reply_comment_id) REFERENCES Comment(id) ON DELETE SET NULL
);

-- Order and Payment Tables
CREATE TABLE OrderDetail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(15, 2) NOT NULL,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Customer(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Product(id) ON DELETE CASCADE
);

CREATE TABLE Payment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    price DECIMAL(15, 2) NOT NULL,
    payment_method ENUM('COD', 'Paypal', 'Stripe', 'ZaloPay', 'Momo') NOT NULL,
    is_pay BOOLEAN DEFAULT FALSE,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Customer(user_id) ON DELETE CASCADE
);

ALTER TABLE Payment
ADD COLUMN is_pay_for_seller BOOLEAN null DEFAULT FALSE;


CREATE TABLE PaymentDetail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    payment_id INT NOT NULL,
    order_detail_id INT NOT NULL,
    FOREIGN KEY (payment_id) REFERENCES Payment(id) ON DELETE CASCADE,
    FOREIGN KEY (order_detail_id) REFERENCES OrderDetail(id) ON DELETE CASCADE
);

-- Indexes for better performance
CREATE INDEX idx_product_name ON Product(name);
CREATE INDEX idx_product_price ON Product(price);
CREATE INDEX idx_product_category ON Product(category_id);
CREATE INDEX idx_product_shop ON Product(shop_id);
CREATE INDEX idx_user_role ON User(user_role);
CREATE INDEX idx_seller_status ON Seller(status);
CREATE INDEX idx_payment_method ON Payment(payment_method);
CREATE INDEX idx_payment_status ON Payment(is_pay);


-- Bắt đầu transaction để đảm bảo tính toàn vẹn dữ liệu
START TRANSACTION;

-- Bước 1: Thêm cột payment_id vào bảng OrderDetail
ALTER TABLE OrderDetail
ADD COLUMN payment_id INT NULL,
ADD CONSTRAINT fk_payment_id
FOREIGN KEY (payment_id) REFERENCES Payment(id) ON DELETE CASCADE;

-- Bước 2: Chuyển dữ liệu từ PaymentDetail sang OrderDetail
-- Cập nhật payment_id trong OrderDetail dựa trên mối quan hệ trong PaymentDetail
UPDATE OrderDetail od
JOIN PaymentDetail pd ON od.id = pd.order_detail_id
JOIN Payment p ON pd.payment_id = p.id
SET od.payment_id = p.id;

-- Đặt payment_id thành NOT NULL sau khi chuyển dữ liệu (nếu cần)
-- Lưu ý: Chỉ thực hiện bước này nếu tất cả các OrderDetail đều có payment_id hợp lệ
-- ALTER TABLE OrderDetail
-- MODIFY payment_id INT NOT NULL;

-- Kiểm tra dữ liệu để đảm bảo không có hàng nào bị bỏ sót
SELECT od.id AS order_detail_id, od.payment_id, p.id AS payment_id_from_payment
FROM OrderDetail od
LEFT JOIN Payment p ON od.payment_id = p.id
WHERE od.payment_id IS NULL;

-- Nếu không có lỗi, commit transaction
COMMIT;