import React, { useState } from 'react';
import { Form, Button, Alert } from 'react-bootstrap';
import { useDispatch, useSelector } from 'react-redux';
import { toast } from 'react-toastify';

const CreateProduct = () => {
  const [formData, setFormData] = useState({
    name: '',
    price: '',
    description: '',
    categoryId: '',
    image: '',
    stock: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState('');
  const dispatch = useDispatch();

  const handleChange = (e) => {
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.name || !formData.price || !formData.categoryId || !formData.stock) {
      setError('Vui lòng điền đầy đủ tên, giá, danh mục và số lượng tồn kho');
      return;
    }
    try {
      toast.success('Tạo sản phẩm thành công!');
      setFormData({
        name: '',
        price: '',
        description: '',
        categoryId: '',
        image: '',
        stock: '',
      });
      setError('');
    } catch (err) {
      setError('Lỗi khi tạo sản phẩm. Vui lòng kiểm tra xem bạn đã tạo shop chưa.');
    }
  };

  return (
    <div>
      <h4>Tạo Sản Phẩm</h4>
      {error && <Alert variant="danger">{error}</Alert>}
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3" controlId="productName">
          <Form.Label>Tên sản phẩm</Form.Label>
          <Form.Control
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            placeholder="Nhập tên sản phẩm"
            required
          />
        </Form.Group>
        <Form.Group className="mb-3" controlId="productPrice">
          <Form.Label>Giá</Form.Label>
          <Form.Control
            type="number"
            name="price"
            value={formData.price}
            onChange={handleChange}
            placeholder="Nhập giá sản phẩm"
            min="0"
            required
          />
        </Form.Group>
        <Form.Group className="mb-3" controlId="productDescription">
          <Form.Label>Mô tả</Form.Label>
          <Form.Control
            as="textarea"
            name="description"
            value={formData.description}
            onChange={handleChange}
            placeholder="Nhập mô tả sản phẩm"
          />
        </Form.Group>
        <Form.Group className="mb-3" controlId="productCategory">
          <Form.Label>ID danh mục</Form.Label>
          <Form.Control
            type="number"
            name="categoryId"
            value={formData.categoryId}
            onChange={handleChange}
            placeholder="Nhập ID danh mục"
            required
          />
        </Form.Group>
        <Form.Group className="mb-3" controlId="productImage">
          <Form.Label>URL hình ảnh</Form.Label>
          <Form.Control
            type="text"
            name="image"
            value={formData.image}
            onChange={handleChange}
            placeholder="Nhập URL hình ảnh"
          />
        </Form.Group>
        <Form.Group className="mb-3" controlId="productStock">
          <Form.Label>Số lượng tồn kho</Form.Label>
          <Form.Control
            type="number"
            name="stock"
            value={formData.stock}
            onChange={handleChange}
            placeholder="Nhập số lượng tồn kho"
            min="0"
            required
          />
        </Form.Group>
        <Button variant="primary" type="submit" disabled={loading}>
          {loading ? 'Đang tạo...' : 'Tạo Sản Phẩm'}
        </Button>
      </Form>
    </div>
  );
};

export default CreateProduct;