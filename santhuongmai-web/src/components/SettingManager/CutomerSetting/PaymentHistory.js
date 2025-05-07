import React, { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import { Container, Table, Button, Alert, Modal } from 'react-bootstrap';
import { useNavigate, Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import MySpinner from '../../layouts/MySpinner';
import Apis, { authApis, endpoints } from '../../../configs/Apis';

const PaymentHistory = () => {
  const user = useSelector((state) => state.authentication);
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [selectedPayment, setSelectedPayment] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (!user) {
      toast.error('Vui lòng đăng nhập để xem lịch sử thanh toán!');
      navigate('/login');
      return;
    }

    const fetchPaymentHistory = async () => {
      setLoading(true);
      try {
        const response = await authApis().get(endpoints.paymentHistory);
        if (response.data.status === 'success') {
          setPayments(response.data.payment);
        } else {
          toast.error('Lỗi khi tải lịch sử thanh toán: ' + response.data.message);
        }
      } catch (err) {
        const message = err.response?.data?.message || 'Lỗi khi tải lịch sử thanh toán';
        toast.error(message);
      } finally {
        setLoading(false);
      }
    };

    fetchPaymentHistory();
  }, [user, navigate]);

  // Hàm mở modal và truyền dữ liệu
  const handleShowModal = (payment) => {
    setSelectedPayment(payment);
    setShowModal(true);
  };

  // Hàm đóng modal
  const handleCloseModal = () => {
    setShowModal(false);
    setSelectedPayment(null);
  };

  return (
    <Container className="py-4">
      <h2>Lịch Sử Thanh Toán</h2>
      {loading ? (
        <MySpinner />
      ) : payments.length === 0 ? (
        <Alert variant="info">Bạn chưa có đơn hàng nào.</Alert>
      ) : (
        <Table striped bordered hover responsive>
          <thead>
            <tr>
              <th>Mã đơn hàng</th>
              <th>Ngày đặt</th>
              <th>Tổng tiền</th>
              <th>Phương thức</th>
              <th>Trạng thái</th>
              <th>Chi tiết</th>
            </tr>
          </thead>
          <tbody>
            {payments.map((payment) => (
              <tr key={payment.id}>
                <td>{payment.id}</td>
                <td>{new Date(payment.createAt).toLocaleDateString('vi-VN')}</td>
                <td>{payment.amount.toLocaleString()} VND</td>
                <td>{payment.paymentMethod}</td>
                {payment.isPay === true ? (
                  <td className="text-success">Đã thanh toán</td>
                ) : (
                  <td className="text-secondary">Chưa thanh toán</td>
                )}
                <td>
                  <Button
                    variant="outline-primary"
                    size="sm"
                    onClick={() => handleShowModal(payment)}
                  >
                    Xem chi tiết
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}

      {/* Modal chi tiết hóa đơn */}
      <Modal show={showModal} onHide={handleCloseModal} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>Chi tiết hóa đơn - Mã đơn: {selectedPayment?.id}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {selectedPayment && (
            <>
              <p><strong>Ngày đặt:</strong> {new Date(selectedPayment.createAt).toLocaleDateString('vi-VN')}</p>
              <p><strong>Tổng tiền:</strong> {selectedPayment.amount.toLocaleString()} VND</p>
              <p><strong>Phương thức thanh toán:</strong> {selectedPayment.paymentMethod}</p>
              <p><strong>Trạng thái:</strong> {selectedPayment.isPay ? 'Đã thanh toán' : 'Chưa thanh toán'}</p>
              <h5>Danh sách sản phẩm:</h5>
              <Table striped bordered hover responsive>
                <thead>
                  <tr>
                    <th>Hình ảnh</th>
                    <th>Tên sản phẩm</th>
                    <th>Giá</th>
                    <th>Số lượng</th>
                    <th>Thành tiền</th>
                  </tr>
                </thead>
                <tbody>
                  {selectedPayment.orderDetails.map((orderDetail, index) => (
                    <tr key={index}>
                      <td>
                        <img
                          src={orderDetail.image}
                          alt={orderDetail.name}
                          style={{ width: '50px', height: '50px', objectFit: 'cover' }}
                        />
                      </td>
                      <td>{orderDetail.name}</td>
                      <td>{orderDetail.price.toLocaleString()} VND</td>
                      <td>{orderDetail.quantity}</td>
                      <td>{(orderDetail.price * orderDetail.quantity).toLocaleString()} VND</td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            </>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseModal}>
            Đóng
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
};

export default PaymentHistory;