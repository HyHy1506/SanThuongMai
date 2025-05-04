import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Table, Button, Form, Alert, Modal } from 'react-bootstrap';
import { FaEdit, FaTrash } from 'react-icons/fa';
import { toast } from 'react-toastify';

const ManageProduct = () => {
  const dispatch = useDispatch();
  const [editProduct, setEditProduct] = useState(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [productToDelete, setProductToDelete] = useState(null);
  const [loading, setLoading] = useState(null);
  const [products,setProducts]=useState([])


  const handleEdit = (product) => {
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    try {
     
      toast.success('Cập nhật sản phẩm thành công!');
      setEditProduct(null);
    } catch (err) {
      toast.error('Lỗi khi cập nhật sản phẩm');
    }
  };

  const handleDelete = async () => {
    try {
      toast.success('Xóa sản phẩm thành công!');
      setShowDeleteModal(false);
      setProductToDelete(null);
    } catch (err) {
      toast.error('Lỗi khi xóa sản phẩm');
    }
  };

  const handleChange = (e) => {
  };

  return (
    <div>
      <h4>Quản lý Sản Phẩm</h4>
      {loading ? (
        <p>Đang tải...</p>
      ) : products.length === 0 ? (
        <Alert variant="info">Bạn chưa có sản phẩm nào.</Alert>
      ) : (
        <>
          <Table striped bordered hover responsive>
            <thead>
              <tr>
                <th>Tên</th>
                <th>Giá</th>
                <th>Danh mục</th>
                <th>Tồn kho</th>
                <th>Hình ảnh</th>
                <th>Hành động</th>
              </tr>
            </thead>
            <tbody>
              {products.map((product) => (
                <tr key={product.id}>
                  <td>{product.name}</td>
                  <td>{product.price.toLocaleString()} VND</td>
                  <td>{product.categoryId}</td>
                  <td>{product.stock}</td>
                  <td>
                    {product.image ? <img src={product.image} alt={product.name} width="50" /> : 'Không có'}
                  </td>
                  <td>
                    <Button variant="warning" size="sm" onClick={() => handleEdit(product)} className="me-2">
                      <FaEdit />
                    </Button>
                    <Button
                      variant="danger"
                      size="sm"
                      onClick={() => {
                        setProductToDelete(product.id);
                        setShowDeleteModal(true);
                      }}
                    >
                      <FaTrash />
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>

          {editProduct && (
            <div className="mt-4">
              <h5>Chỉnh sửa Sản Phẩm</h5>
              <Form onSubmit={handleUpdate}>
                <Form.Group className="mb-3" controlId="productName">
                  <Form.Label>Tên sản phẩm</Form.Label>
                  <Form.Control
                    type="text"
                    name="name"
                    value={editProduct.name}
                    onChange={handleChange}
                    required
                  />
                </Form.Group>
                <Form.Group className="mb-3" controlId="productPrice">
                  <Form.Label>Giá</Form.Label>
                  <Form.Control
                    type="number"
                    name="price"
                    value={editProduct.price}
                    onChange={handleChange}
                    min="0"
                    required
                  />
                </Form.Group>
                <Form.Group className="mb-3" controlId="productDescription">
                  <Form.Label>Mô tả</Form.Label>
                  <Form.Control
                    as="textarea"
                    name="description"
                    value={editProduct.description}
                    onChange={handleChange}
                  />
                </Form.Group>
                <Form.Group className="mb-3" controlId="productCategory">
                  <Form.Label>ID danh mục</Form.Label>
                  <Form.Control
                    type="number"
                    name="categoryId"
                    value={editProduct.categoryId}
                    onChange={handleChange}
                    required
                  />
                </Form.Group>
                <Form.Group className="mb-3" controlId="productImage">
                  <Form.Label>URL hình ảnh</Form.Label>
                  <Form.Control
                    type="text"
                    name="image"
                    value={editProduct.image}
                    onChange={handleChange}
                  />
                </Form.Group>
                <Form.Group className="mb-3" controlId="productStock">
                  <Form.Label>Số lượng tồn kho</Form.Label>
                  <Form.Control
                    type="number"
                    name="stock"
                    value={editProduct.stock}
                    onChange={handleChange}
                    min="0"
                    required
                  />
                </Form.Group>
                <Button variant="primary" type="submit" className="me-2">
                  Cập nhật
                </Button>
                <Button variant="secondary" onClick={() => setEditProduct(null)}>
                  Hủy
                </Button>
              </Form>
            </div>
          )}

          <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
            <Modal.Header closeButton>
              <Modal.Title>Xác nhận xóa</Modal.Title>
            </Modal.Header>
            <Modal.Body>Bạn có chắc muốn xóa sản phẩm này? Hành động không thể hoàn tác.</Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
                Hủy
              </Button>
              <Button variant="danger" onClick={handleDelete}>
                Xóa
              </Button>
            </Modal.Footer>
          </Modal>
        </>
      )}
    </div>
  );
};

export default ManageProduct;