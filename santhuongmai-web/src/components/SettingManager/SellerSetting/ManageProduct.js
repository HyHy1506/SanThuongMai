import React, { useState, useEffect } from 'react';
import { Table, Button, Form, Alert, Modal, Card, Row, Col, InputGroup } from 'react-bootstrap';
import { FaEdit, FaTrash } from 'react-icons/fa';
import { toast } from 'react-toastify';
import Apis, { authApis, endpoints } from '../../../configs/Apis';
import { useSelector } from 'react-redux';

const ManageProduct = () => {
  const [products, setProducts] = useState([]);
  const [editProduct, setEditProduct] = useState(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [productToDelete, setProductToDelete] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [seller, setSeller] = useState({})
  const user = useSelector(state => state.authentication)
  const loadSeller = async () => {
    try {
      const res = await Apis.get(endpoints['seller-with-id'](user.id))
      setSeller(res.data)
      
    } catch (error) {
      toast.error(error)
    }
  }

  useEffect(() => {
    loadSeller()
    
  }, []);
  useEffect(() => {
    if (seller.shopId) {
      loadProducts();
    }
  }, [seller.shopId]);
  const loadProducts = async () => {
    setLoading(true);
    try {
      console.log(seller.shopId);
      const res = await Apis.get(`${endpoints['products']}?shopId=${seller.shopId}`);
      setProducts(res.data);
      setError('');
    } catch (err) {
      const message = err.response?.data?.error || 'Lỗi khi tải danh sách sản phẩm';
      
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (product) => {
    setEditProduct({
      id: product.id,
      name: product.name,
      price: product.price,
      description: product.description || '',
      categoryId: product.categoryId,
      stock: product.stock || '',
      image: null, // File input starts empty
      attributes: product.attributes || [{ attributeId: '', value: '' }],
    });
  };

  const handleChange = (e, index) => {
    const { name, value, files } = e.target;
    if (index !== undefined) {
      const updatedAttributes = [...editProduct.attributes];
      updatedAttributes[index] = { ...updatedAttributes[index], [name]: value };
      setEditProduct({ ...editProduct, attributes: updatedAttributes });
    } else if (name === 'image') {
      setEditProduct({ ...editProduct, image: files[0] });
    } else {
      setEditProduct({ ...editProduct, [name]: value });
    }
  };

  const addAttribute = () => {
    setEditProduct({
      ...editProduct,
      attributes: [...editProduct.attributes, { attributeId: '', value: '' }],
    });
  };

  const removeAttribute = (index) => {
    const updatedAttributes = editProduct.attributes.filter((_, i) => i !== index);
    setEditProduct({ ...editProduct, attributes: updatedAttributes });
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    // Validation
    if (!editProduct.name || !editProduct.price || !editProduct.categoryId || !editProduct.stock) {
      setError('Vui lòng điền đầy đủ tên, giá, danh mục và số lượng tồn kho');
      setLoading(false);
      return;
    }
    if (editProduct.attributes.some(attr => !attr.attributeId || !attr.value)) {
      setError('Vui lòng điền đầy đủ ID thuộc tính và giá trị');
      setLoading(false);
      return;
    }

    try {
      const formData = new FormData();
      const params = {
        name: editProduct.name,
        price: editProduct.price,
        description: editProduct.description,
        categoryId: parseInt(editProduct.categoryId),
        attributes: editProduct.attributes.map(attr => ({
          attributeId: parseInt(attr.attributeId),
          value: attr.value,
        })),
      };
      formData.append('params', JSON.stringify(params));
      if (editProduct.image) {
        formData.append('image', editProduct.image);
      }

      const res = await authApis().put(endpoints['products-with-id'](editProduct.id), formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      toast.success('Cập nhật sản phẩm thành công!');
      setEditProduct(null);
      loadProducts();
    } catch (err) {
      const message = err.response?.data?.error || 'Lỗi khi cập nhật sản phẩm';
      setError(message);
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    setLoading(true);
    try {
      await authApis().delete(endpoints['products-with-id'](productToDelete));
      toast.success('Xóa sản phẩm thành công!');
      setProductToDelete(null);
      loadProducts();
    } catch (err) {
      const message = err.response?.data?.error || 'Lỗi khi xóa sản phẩm';
      toast.error(message);
    } finally {
      setLoading(false);
      setShowDeleteModal(false)
    }
  };

  return (
    <div className="container my-4">
      <h4 className="mb-4">Quản lý Sản Phẩm</h4>
      {error && <Alert variant="danger">{error}</Alert>}
      {loading && !editProduct && <Alert variant="info">Đang tải...</Alert>}
      {!loading && products.length === 0 ? (
        <Alert variant="info">Bạn chưa có sản phẩm nào.</Alert>
      ) : (
        <>
          <Card className="shadow-sm mb-4">
            <Card.Body>
              <Table striped bordered hover responsive>
                <thead>
                  <tr>
                    <th>Tên</th>
                    <th>Giá (VND)</th>
                    <th>Danh mục</th>
                    {/* <th>Tồn kho</th> */}
                    <th>Hình ảnh</th>
                    <th>Hành động</th>
                  </tr>
                </thead>
                <tbody>
                  {products.map((product) => (
                    <tr key={product.id}>
                      <td>{product.name}</td>
                      <td>{parseFloat(product.price).toLocaleString('vi-VN')}</td>
                      <td>{product.categoryName}</td>
                      {/* <td>{product.stock}</td> */}
                      <td>
                        {product.image ? (
                          <img src={product.image} alt={product.name} width="50" className="rounded" />
                        ) : (
                          'Không có'
                        )}
                      </td>
                      <td>
                        <Button
                          variant="warning"
                          size="sm"
                          onClick={() => handleEdit(product)}
                          className="me-2"
                        >
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
            </Card.Body>
          </Card>

          {editProduct && (
            <Card className="shadow-sm p-4">
              <Card.Body>
                <h5 className="mb-4">Chỉnh sửa Sản Phẩm</h5>
                {error && <Alert variant="danger">{error}</Alert>}
                <Form onSubmit={handleUpdate}>
                  <Row>
                    <Col md={6}>
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
                    </Col>
                    <Col md={6}>
                      <Form.Group className="mb-3" controlId="productPrice">
                        <Form.Label>Giá (VND)</Form.Label>
                        <Form.Control
                          type="number"
                          name="price"
                          value={editProduct.price}
                          onChange={handleChange}
                          min="0"
                          step="0.01"
                          required
                        />
                      </Form.Group>
                    </Col>
                  </Row>
                  <Form.Group className="mb-3" controlId="productDescription">
                    <Form.Label>Mô tả</Form.Label>
                    <Form.Control
                      as="textarea"
                      name="description"
                      value={editProduct.description}
                      onChange={handleChange}
                      rows={4}
                    />
                  </Form.Group>
                  <Row>
                    <Col md={6}>
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
                    </Col>
                    <Col md={6}>
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
                    </Col>
                  </Row>
                  <Form.Group className="mb-3" controlId="productImage">
                    <Form.Label>Hình ảnh</Form.Label>
                    <Form.Control
                      type="file"
                      name="image"
                      accept="image/*"
                      onChange={handleChange}
                    />
                    {editProduct.image && (
                      <small className="text-muted">Đã chọn: {editProduct.image.name}</small>
                    )}
                  </Form.Group>
                  <h6 className="mt-4">Thuộc tính sản phẩm</h6>
                  {editProduct.attributes.map((attr, index) => (
                    <Row key={index} className="mb-3">
                      <Col md={5}>
                        <Form.Group controlId={`attributeId-${index}`}>
                          <Form.Label>ID thuộc tính</Form.Label>
                          <Form.Control
                            type="number"
                            name="attributeId"
                            value={attr.attributeId}
                            onChange={(e) => handleChange(e, index)}
                            required
                          />
                        </Form.Group>
                      </Col>
                      <Col md={5}>
                        <Form.Group controlId={`attributeValue-${index}`}>
                          <Form.Label>Giá trị</Form.Label>
                          <Form.Control
                            type="text"
                            name="value"
                            value={attr.value}
                            onChange={(e) => handleChange(e, index)}
                            required
                          />
                        </Form.Group>
                      </Col>
                      <Col md={2} className="d-flex align-items-end">
                        <Button
                          variant="danger"
                          size="sm"
                          onClick={() => removeAttribute(index)}
                          disabled={editProduct.attributes.length === 1}
                        >
                          Xóa
                        </Button>
                      </Col>
                    </Row>
                  ))}
                  <Button variant="outline-primary" size="sm" onClick={addAttribute} className="mb-4">
                    Thêm thuộc tính
                  </Button>
                  <div className="text-center">
                    <Button variant="primary" type="submit" disabled={loading} className="me-2" size="lg">
                      {loading ? 'Đang cập nhật...' : 'Cập nhật'}
                    </Button>
                    <Button variant="secondary" onClick={() => setEditProduct(null)} size="lg">
                      Hủy
                    </Button>
                  </div>
                </Form>
              </Card.Body>
            </Card>
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

export default ManageProduct;