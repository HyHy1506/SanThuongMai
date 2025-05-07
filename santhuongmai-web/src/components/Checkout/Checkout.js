import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Container, Table, Button, Form, Row, Col, Alert } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import MySpinner from '../layouts/MySpinner';
import Apis, { authApis, endpoints } from '../../configs/Apis';
import { reloadCart } from '../../actions/cartActions';
import { PayPalScriptProvider, PayPalButtons } from "@paypal/react-paypal-js";

const Checkout = () => {
  const cartItems = useSelector((state) => state.cart.items);
  const user = useSelector((state) => state.authentication);
  const dispatch = useDispatch();
  const [paymentMethod, setPaymentMethod] = useState('COD');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  // Calculate total amount
  const totalAmount = cartItems.reduce((total, item) => total + item.price * item.quantity, 0);

  // Handle another checkout
  const handleCheckout = async (e) => {
    e.preventDefault();
    if (!user) {
      toast.error('Vui lòng đăng nhập để thanh toán!');
      navigate('/login');
      return;
    }
    if (cartItems.length === 0) {
      toast.error('Giỏ hàng của bạn đang trống!');
      return;
    }

    setLoading(true);
    try {
      const payload = {
        paymentMethod: paymentMethod,
        items: cartItems.map((item) => ({
          productId: item.productId,
          name: item.name,
          price: item.price,
          image: item.image,
          quantity: item.quantity,
        })),
      };

      const response = await authApis().post(endpoints.payments, payload);
      if (response.data.status === 'success') {
        toast.success('Đơn hàng đã được tạo thành công! Mã đơn hàng: ' + response.data.paymentId);
        localStorage.setItem('cart', JSON.stringify([]));
        dispatch(reloadCart());
        navigate('/');
      } else {
        toast.error('Lỗi khi tạo đơn hàng: ' + response.data.error);
      }
    } catch (err) {
      const message = err.response?.data?.error || 'Lỗi khi tạo đơn hàng';
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };
  // Handle paypal checkout
  const handleCheckoutWithPaypal = async () => {
    if (!user) {
      toast.error('Vui lòng đăng nhập để thanh toán!');
      navigate('/login');
      return;
    }
    if (cartItems.length === 0) {
      toast.error('Giỏ hàng của bạn đang trống!');
      return;
    }

    setLoading(true);
    try {
      const payload = {
        paymentMethod: paymentMethod,
        items: cartItems.map((item) => ({
          productId: item.productId,
          name: item.name,
          price: item.price,
          image: item.image,
          quantity: item.quantity,
        })),
      };

      const response = await authApis().post(endpoints.payments, payload);
      if (response.data.status === 'success') {
        toast.success('Đơn hàng đã được tạo thành công! Mã đơn hàng: ' + response.data.paymentId);
        localStorage.setItem('cart', JSON.stringify([]));
        dispatch(reloadCart());
        navigate('/');
      } else {
        toast.error('Lỗi khi tạo đơn hàng: ' + response.data.error);
      }
    } catch (err) {
      const message = err.response?.data?.error || 'Lỗi khi tạo đơn hàng';
      toast.error(message);
    } finally {
      setLoading(false);
    }
  }; 
  // PayPal configuration
  const paypalClientId = process.env.REACT_APP_PAYPAL_CLIENT_ID
  return (
    <Container className="py-4">
      <h2>Thanh Toán</h2>
      {cartItems.length === 0 ? (
        <Alert variant="info">Giỏ hàng của bạn đang trống.</Alert>
      ) : (
        <>
          <Table striped bordered hover responsive>
            <thead>
              <tr>
                <th>Sản phẩm</th>
                <th>Giá</th>
                <th>Số lượng</th>
                <th>Tổng</th>
              </tr>
            </thead>
            <tbody>
              {cartItems.map((item) => (
                <tr key={item.productId}>
                  <td>
                    <Row className="align-items-center">
                      <Col xs={3}>
                        <img src={item.image} alt={item.name} style={{ width: '50px' }} className="rounded" />
                      </Col>
                      <Col>{item.name}</Col>
                    </Row>
                  </td>
                  <td>{item.price.toLocaleString()} VND</td>
                  <td>{item.quantity}</td>
                  <td>{(item.price * item.quantity).toLocaleString()} VND</td>
                </tr>
              ))}
            </tbody>
          </Table>
          <Row className="mt-3">
            <Col md={6}>
              <Form onSubmit={handleCheckout}>
                <Form.Group className="mb-3" controlId="paymentMethod">
                  <Form.Label>Phương thức thanh toán</Form.Label>
                  <Form.Select
                    value={paymentMethod}
                    onChange={(e) => setPaymentMethod(e.target.value)}
                    required
                  >
                    <option value="COD">Tiền mặt khi nhận hàng (COD)</option>
                    <option value="Paypal">Thanh toán bằng Paypal</option>
                    <option value="ZaloPay">Thanh toán bằng ZaloPay</option>
                    <option value="Momo">Thanh toán bằng Momo</option>
                  </Form.Select>
                </Form.Group>
                {paymentMethod === 'Paypal' ? (
                  <PayPalScriptProvider options={{ "client-id": paypalClientId, currency: "USD" }}>
                    <PayPalButtons
                      createOrder={(data, actions) => {
                        return actions.order.create({
                          purchase_units: [
                            {
                              amount: {
                                currency_code: "USD",
                                value: (totalAmount / 26000).toFixed(2), // Convert VND to USD (approximate rate)
                                breakdown: {
                                  item_total: {
                                    currency_code: "USD",
                                    value: (totalAmount / 26000).toFixed(2),
                                  },
                                },
                              },
                              items: cartItems.map((item) => ({
                                name: item.name,
                                unit_amount: {
                                  currency_code: "USD",
                                  value: (item.price / 26000).toFixed(2),
                                },
                                quantity: item.quantity,
                              })),
                            },
                          ],
                        });
                      }}
                      onApprove={async (data, actions) => {
                        try {
                          const details = await actions.order.capture();
                          
                          toast.success(`Thanh toán PayPal thành công! Mã giao dịch: ${details.id}`);
                          handleCheckoutWithPaypal()
                        } catch (err) {
                          toast.error('Lỗi khi xác nhận thanh toán PayPal');
                          console.error(err);
                        }
                      }}
                      onError={(err) => {
                        toast.error('Lỗi khi xử lý thanh toán PayPal');
                        console.error(err);
                      }}
                      onCancel={() => {
                        toast.info('Thanh toán PayPal đã bị hủy');
                      }}
                    />
                  </PayPalScriptProvider>
                ) : (
                  <Button
                    variant="primary"
                    type="submit"
                    disabled={loading}
                  >
                    {loading ? <MySpinner size="sm" /> : 'Xác nhận thanh toán'}
                  </Button>
                )}
              </Form>
            </Col>
            <Col md={6} className="text-end">
              <h5>Tổng tiền: {totalAmount.toLocaleString()} VND</h5>
            </Col>
          </Row>
        </>
      )}
    </Container>
  );
};

export default Checkout;