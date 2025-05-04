import React, { useState, useEffect } from 'react';
import { Table, Button, Form, Alert, Modal, } from 'react-bootstrap';
import { FaEdit, FaTrash } from 'react-icons/fa';
import { toast } from 'react-toastify';
import Apis, { authApis, endpoints } from '../configs/Apis';
import { useDispatch, useSelector } from 'react-redux';

const ManageShop = () => {
  const [shop, setShop] = useState(null);
  const [editShop, setEditShop] = useState(null);
  const [loading, setLoading] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const user = useSelector(state => state.authentication);
  const loadShop = async () => {
    setLoading(true);
    try {
      const userId = user.id; 
      const res = await Apis.get(endpoints['shop-of-user'](userId));
      setShop(res.data); // Lưu dữ liệu shop vào state
    } catch (err) {
      toast.error('Không thể tải thông tin cửa hàng. Vui lòng thử lại.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => {

    loadShop();
  }, []);

  // Xử lý chỉnh sửa shop
  const handleEdit = () => {
    setEditShop({ name: shop.name, isActive: shop.isActive });
  };

  // Xử lý cập nhật shop
  const handleUpdate = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const res =await authApis().put(endpoints['shop-with-id'](shop.id), editShop);
      console.log(editShop)
      setShop({ ...shop, name: editShop.name, isActive: editShop.isActive });
      setEditShop(null);
      toast.success('Cập nhật cửa hàng thành công!');
    } catch (err) {
      if(err.response!=null)
      toast.error('Lỗi khi cập nhật cửa hàng '+ err.response.data.error);
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Xử lý xóa shop
  const handleDelete = async () => {
    setLoading(true);
    try {
      await Apis.delete(endpoints['shop-with-id'](shop.id));
      setShop(null);
      setShowDeleteModal(false);
      toast.success('Xóa cửa hàng thành công!');
    } catch (err) {
      if(err.response!=null)
      toast.error('Lỗi khi xóa cửa hàng '+err.response.data.error);
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Xử lý thay đổi dữ liệu form
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setEditShop({
      ...editShop,
      [name]: type === 'checkbox' ? checked : value,
    });
  };

  return (
    <div>
      <h4>Quản lý Shop</h4>
      {loading ? (
        <p>Đang tải...</p>
      ) : !shop ? (
        <Alert variant="info">Bạn chưa có cửa hàng nào.</Alert>
      ) : (
        <>
          <Table striped bordered hover responsive>
            <thead>
              <tr>
                <th>Tên</th>
                <th>Chủ sở hữu</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{shop.name}</td>
                <td>{shop.sellerNickname}</td>
                <td>{shop.isActive ? 'Hoạt động' : 'Không hoạt động'}</td>
                <td>
                  <Button
                    variant="warning"
                    size="sm"
                    onClick={handleEdit}
                    className="me-2"
                    disabled={loading}
                  >
                    <FaEdit />
                  </Button>
                  <Button
                    variant="danger"
                    size="sm"
                    onClick={() => setShowDeleteModal(true)}
                    disabled={loading}
                  >
                    <FaTrash />
                  </Button>
                </td>
              </tr>
            </tbody>
          </Table>

          {editShop && (
            <div className="mt-4">
              <h5>Chỉnh sửa Shop</h5>
              <Form onSubmit={handleUpdate}>
                <Form.Group className="mb-3" controlId="shopName">
                  <Form.Label>Tên cửa hàng</Form.Label>
                  <Form.Control
                    type="text"
                    name="name"
                    value={editShop.name}
                    onChange={handleChange}
                    placeholder="Nhập tên cửa hàng"
                    required
                    disabled={loading}
                  />
                </Form.Group>
                <Form.Group className="mb-3" controlId="shopIsActive">
                  <Form.Check
                    type="checkbox"
                    name="isActive"
                    label="Hoạt động"
                    checked={editShop.isActive}
                    onChange={handleChange}
                    disabled={loading}
                  />
                </Form.Group>
                <Button variant="primary" type="submit" className="me-2" disabled={loading}>
                  {loading ? 'Đang cập nhật...' : 'Cập nhật'}
                </Button>
                <Button
                  variant="secondary"
                  onClick={() => setEditShop(null)}
                  disabled={loading}
                >
                  Hủy
                </Button>
              </Form>
            </div>
          )}

          <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
            <Modal.Header closeButton>
              <Modal.Title>Xác nhận xóa</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              Bạn có chắc muốn xóa cửa hàng "{shop?.name}"? Hành động này không thể hoàn tác.
            </Modal.Body>
            <Modal.Footer>
              <Button
                variant="secondary"
                onClick={() => setShowDeleteModal(false)}
                disabled={loading}
              >
                Hủy
              </Button>
              <Button variant="danger" onClick={handleDelete} disabled={loading}>
                {loading ? 'Đang xóa...' : 'Xóa'}
              </Button>
            </Modal.Footer>
          </Modal>
        </>
      )}
    </div>
  );
};

export default ManageShop;