
USE santhuongmai;
-- 1. Tạo 1 user là seller
INSERT INTO User (username, nickname, email, avatar, user_role, password)
VALUES ('nguyenvana', 'Nguyễn Văn A', 'vana@example.com', NULL, 'Customer', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO');

-- 2. Gán user này làm seller
INSERT INTO Seller (user_id, status)
VALUES ((SELECT id FROM User WHERE username = 'nguyenvana'), 'approved');

-- 3. Tạo shop cho seller này
INSERT INTO Shop (name, seller_id)
VALUES ('Cửa Hàng Điện Tử A', (SELECT user_id FROM Seller WHERE user_id = (SELECT id FROM User WHERE username = 'nguyenvana')));

-- 4. Thêm 5 danh mục sản phẩm
INSERT INTO Category (name, description)
VALUES 
('Điện thoại', 'Các loại điện thoại thông minh, điện thoại phổ thông'),
('Laptop', 'Máy tính xách tay phục vụ học tập và làm việc'),
('Phụ kiện', 'Ốp lưng, sạc, tai nghe và các phụ kiện khác'),
('Gia dụng', 'Đồ gia dụng như quạt, máy xay, nồi cơm điện'),
('Thời trang', 'Quần áo, giày dép, phụ kiện thời trang');

-- 5. Thêm 20 sản phẩm thuộc 5 danh mục khác nhau (mỗi danh mục ~4 sản phẩm)
INSERT INTO Product (name, image, price, shop_id, description, category_id)
VALUES
-- Điện thoại
('iPhone 14 Pro Max', NULL, 29990000, 1, 'Điện thoại cao cấp của Apple', 1),
('Samsung Galaxy S23', NULL, 21990000, 1, 'Flagship mới nhất của Samsung', 1),
('Xiaomi Redmi Note 12', NULL, 5490000, 1, 'Điện thoại tầm trung giá tốt', 1),
('OPPO A78', NULL, 4990000, 1, 'Điện thoại giá rẻ nhiều tính năng', 1),

-- Laptop
('MacBook Air M2', NULL, 26990000, 1, 'Laptop mỏng nhẹ, hiệu năng cao', 2),
('Asus VivoBook 15', NULL, 13990000, 1, 'Laptop học sinh sinh viên', 2),
('HP Pavilion 14', NULL, 15490000, 1, 'Thiết kế đẹp, cấu hình ổn', 2),
('Lenovo IdeaPad 3', NULL, 11900000, 1, 'Giá hợp lý, hiệu suất ổn định', 2),

-- Phụ kiện
('Ốp lưng iPhone 14', NULL, 150000, 1, 'Ốp lưng chống sốc cao cấp', 3),
('Tai nghe Bluetooth Baseus', NULL, 490000, 1, 'Âm thanh sống động, kết nối ổn định', 3),
('Sạc nhanh Anker 20W', NULL, 320000, 1, 'Sạc nhanh hỗ trợ nhiều thiết bị', 3),
('Giá đỡ điện thoại', NULL, 90000, 1, 'Tiện lợi khi xem video hoặc gọi video', 3),

-- Gia dụng
('Quạt đứng Mitsubishi', NULL, 1290000, 1, 'Quạt đứng, gió mạnh và bền', 4),
('Máy xay sinh tố Philips', NULL, 990000, 1, 'Xay mịn, dễ vệ sinh', 4),
('Nồi cơm điện Sharp 1.8L', NULL, 1150000, 1, 'Nấu cơm ngon, giữ ấm lâu', 4),
('Bình đun siêu tốc Kangaroo', NULL, 490000, 1, 'Dung tích lớn, đun nhanh', 4),

-- Thời trang
('Áo thun nam cổ tròn', NULL, 129000, 1, 'Chất vải cotton thoáng mát', 5),
('Quần jean nữ lưng cao', NULL, 299000, 1, 'Tôn dáng, thời trang', 5),
('Giày sneaker trắng', NULL, 499000, 1, 'Thiết kế trẻ trung, dễ phối đồ', 5),
('Túi đeo chéo mini', NULL, 229000, 1, 'Phù hợp đi chơi, dạo phố', 5);

INSERT INTO Attribute (name, is_active, create_at, update_at) VALUES
('RAM', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dung lượng lưu trữ', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Kích thước màn hình', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Độ phân giải', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bộ vi xử lý', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Card đồ họa', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dung lượng pin', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Hệ điều hành', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Trọng lượng', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Kích thước', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Độ phân giải camera', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Công suất tiêu thụ', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dung tích', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Chất liệu', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Màu sắc', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Điện áp', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tần số', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Hệ thống làm mát', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Loại màn hình', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tần số quét', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Kết nối', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Số cổng kết nối', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bảo hành', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Hiệu suất năng lượng', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Độ ồn', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Chế độ nấu', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Phạm vi nhiệt độ', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Khả năng chống nước', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Công suất động cơ', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Loại bộ lọc', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);