import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Container, Table, Button, Form, Image, Row, Col } from 'react-bootstrap';
import { FaTrash } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import { addToCart, removeFromCart, updateCartQuantity } from '../actions/cartActions';
import { toast } from 'react-toastify';

const Cart = () => {
  const dispatch = useDispatch();
  const cartItems = useSelector((state) => state.cart.items);

  const totalAmount = cartItems.reduce((total, item) => total + item.price * item.quantity, 0);

  const handleQuantityChange = (productId, quantity) => {
    if (quantity < 1) return;
    dispatch(updateCartQuantity(productId, quantity));
  };

  const handleRemoveItem = (productId) => {
    dispatch(removeFromCart(productId));
    toast.success('Đã xóa sản phẩm khỏi giỏ hàng');
  };

  return (
    <>
      <style>
        {`
          @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap');
          
          .cart-container-custom {
            font-family: 'Poppins', sans-serif;
            background-color: #FFFFFF;
            padding: 2rem 0;
            min-height: 100vh;
          }
          
          .cart-title-custom {
            font-size: 2rem;
            font-weight: 600;
            color: #537D5D;
            margin-bottom: 1.5rem;
          }
          
          .cart-table-custom {
            background-color: #FFFFFF;
            border: 1px solid #9EBC8A;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(83, 125, 93, 0.1);
          }
          
          .cart-table-custom th {
            background-color: #537D5D;
            color: #FFFFFF;
            font-weight: 500;
          }
          
          .cart-table-custom td {
            vertical-align: middle;
            color: #537D5D;
          }
          
          .cart-image-custom {
            width: 50px;
            height: 50px;
            object-fit: cover;
            border-radius: 5px;
            border: 1px solid #9EBC8A;
            transition: all 0.3s ease;
          }
          
          .cart-image-custom:hover {
            transform: scale(1.05);
            border-color: #73946B;
          }
          
          .quantity-input-custom {
            width: 80px;
            border: 1px solid #9EBC8A;
            border-radius: 5px;
            background-color: #F9F9F9;
            color: #537D5D;
            transition: all 0.3s ease;
          }
          
          .quantity-input-custom:focus {
            border-color: #73946B;
            box-shadow: 0 0 5px rgba(115, 148, 107, 0.5);
          }
          
          .btn-remove-custom {
            background-color: #9EBC8A;
            border: none;
            color: #FFFFFF;
            
            transition: all 0.3s ease;
          }
          
          .btn-remove-custom:hover {
            background-color: #73946B;
            transform: scale(1.1);
          }
          
          .total-text-custom {
            font-size: 1.2rem;
            font-weight: 500;
            color: #537D5D;
          }
          
          .btn-checkout-custom {
            background-color: #73946B;
            border: none;
            color: #FFFFFF;
            padding: 0.75rem 1.5rem;
            border-radius: 20px;
            transition: all 0.3s ease;
          }
          
          .btn-checkout-custom:hover {
            background-color: #9EBC8A;
            transform: scale(1.05);
          }
          
          .btn-checkout-custom:disabled {
            background-color: #9EBC8A;
            opacity: 0.6;
          }
        `}
      </style>
      <Container className="cart-container-custom">
        <h2 className="cart-title-custom">Giỏ hàng</h2>
        {cartItems.length === 0 ? (
          <p style={{ color: '#537D5D' }}>Giỏ hàng của bạn đang trống.</p>
        ) : (
          <>
            <Table striped bordered hover responsive className="cart-table-custom">
              <thead>
                <tr>
                  <th>Sản phẩm</th>
                  <th>Giá</th>
                  <th>Số lượng</th>
                  <th>Tổng</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {cartItems.map((item) => (
                  <tr key={item.productId}>
                    <td>
                      <Row className="align-items-center">
                        <Col xs={3}>
                          <Image
                            src={item.image}
                            alt={item.name}
                            thumbnail
                            className="cart-image-custom"
                          />
                        </Col>
                        <Col>{item.name}</Col>
                      </Row>
                    </td>
                    <td>{item.price.toLocaleString()} VND</td>
                    <td>
                      <Form.Control
                        type="number"
                        value={item.quantity}
                        onChange={(e) => handleQuantityChange(item.productId, parseInt(e.target.value))}
                        min="1"
                        className="quantity-input-custom"
                      />
                    </td>
                    <td>{(item.price * item.quantity).toLocaleString()} VND</td>
                    <td>
                      <Button
                        variant="danger"
                        size="sm"
                        onClick={() => handleRemoveItem(item.productId)}
                        className="btn-remove-custom"
                      >
                        <FaTrash />
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
            <Row className="mt-3">
              <Col className="text-end">
                <h5 className="total-text-custom">Tổng tiền: {totalAmount.toLocaleString()} VND</h5>
                <Button
                  variant="primary"
                  as={Link}
                  to="/checkout"
                  disabled={cartItems.length === 0}
                  className="btn-checkout-custom"
                >
                  Thanh toán
                </Button>
              </Col>
            </Row>
          </>
        )}
      </Container>
    </>
  );
};

export default Cart;