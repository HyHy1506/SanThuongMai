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

  // Tính tổng tiền
  const totalAmount = cartItems.reduce((total, item) => total + item.price * item.quantity, 0);

  // Xử lý thay đổi số lượng
  const handleQuantityChange = (productId, quantity) => {
    if (quantity < 1) return;
    dispatch(updateCartQuantity(productId, quantity));
  };

  // Xử lý xóa sản phẩm
  const handleRemoveItem = (productId) => {
    dispatch(removeFromCart(productId));
    toast.success('Đã xóa sản phẩm khỏi giỏ hàng');
  };

  return (
    <Container className="py-4">
      <h2>Giỏ hàng</h2>
      {cartItems.length === 0 ? (
        <p>Giỏ hàng của bạn đang trống.</p>
      ) : (
        <>
          <Table striped bordered hover responsive>
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
                        <Image src={item.image} alt={item.name} thumbnail style={{ width: '50px' }} />
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
                      style={{ width: '80px' }}
                    />
                  </td>
                  <td>{(item.price * item.quantity).toLocaleString()} VND</td>
                  <td>
                    <Button variant="danger" size="sm" onClick={() => handleRemoveItem(item.productId)}>
                      <FaTrash />
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
          <Row className="mt-3">
            <Col className="text-end">
              <h5>Tổng tiền: {totalAmount.toLocaleString()} VND</h5>
              <Button
                variant="primary"
                as={Link}
                to="/checkout"
                disabled={cartItems.length === 0}
              >
                Thanh toán
              </Button>
            </Col>
          </Row>
        </>
      )}
    </Container>
  );
};

export default Cart;