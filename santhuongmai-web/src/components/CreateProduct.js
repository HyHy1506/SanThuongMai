import React, { useState, useEffect, useRef } from 'react';
import { Form, Button, Alert, Card, Col, Row } from 'react-bootstrap';
import { toast } from 'react-toastify';
import Apis, { authApis, endpoints } from '../configs/Apis';

const CreateProduct = () => {
  const [formData, setFormData] = useState({
    name: '',
    price: '',
    description: '',
    categoryId: 1,
    stock: '',
    attributes: [{ attributeId: '', value: '' }],
  });
  const [attributes, setAttributes] = useState([]);
  const [categories, setCategories] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [attributesLoading, setAttributesLoading] = useState(false);
  const image = useRef();
  // Fetch attributes on mount
  useEffect(() => {
    const loadAttributes = async () => {
      setAttributesLoading(true);
      try {
        const res = await Apis.get(endpoints['attributes']);
        // Filter active attributes
        const activeAttributes = res.data.filter(attr => attr.isActive);
        setAttributes(activeAttributes);
      } catch (err) {
        const message = err.response?.data?.error || 'Lỗi khi tải danh sách thuộc tính';
        toast.error(message);
      } finally {
        setAttributesLoading(false);
      }
    };
    const loadCategories=async()=>{
      try {
        const res = await Apis.get(endpoints['categories']);
        setCategories(res.data);
      } catch (err) {
        const message = err.response?.data?.error || 'Lỗi khi tải danh sách danh mục';
        toast.error(message);
      } finally {
      }
    }
    loadCategories()
    loadAttributes();
  }, []);

  const handleChange = (e, index) => {
    const { name, value } = e.target;
    if (index !== undefined) {
      // Handle attribute changes
      const updatedAttributes = [...formData.attributes];


      updatedAttributes[index] = { ...updatedAttributes[index], [name]: value };

      setFormData({ ...formData, attributes: updatedAttributes });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  const addAttribute = () => {
    setFormData({
      ...formData,
      attributes: [...formData.attributes, { attributeId: '', value: '' }],
    });
  };

  const removeAttribute = (index) => {
    const updatedAttributes = formData.attributes.filter((_, i) => i !== index);
    setFormData({ ...formData, attributes: updatedAttributes });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    // Validation
    if (!formData.name || !formData.price || !formData.categoryId || !formData.stock) {
      setError('Vui lòng điền đầy đủ tên, giá, danh mục và số lượng tồn kho');
      setLoading(false);
      return;
    }
    if (formData.attributes.some(attr => !attr.attributeId || !attr.value)) {
      setError('Vui lòng chọn thuộc tính và nhập giá trị cho tất cả các thuộc tính');
      setLoading(false);
      return;
    }

    try {
      const payload = {
        name: formData.name,
        price: formData.price,
        description: formData.description,
        categoryId: parseInt(formData.categoryId),
        attributes: formData.attributes.map(attr => ({
          attributeId: parseInt(attr.attributeId),
          value: attr.value,
        })),
      };
      let params = JSON.stringify(payload)
      const newForm = new FormData()
      newForm.append('params', params)
      newForm.append('image', image.current.files[0])

      const res = await authApis().post(endpoints['products'], newForm, {
        headers: {
          "Content-Type": "multipart/form-data",
        }
      });
      toast.success('Tạo sản phẩm thành công!');
      setFormData({
        name: '',
        price: '',
        description: '',
        categoryId: '',
        stock: '',
        attributes: [{ attributeId: '', value: '' }],
      });
      if (image.current) {
        image.current.value = '';  
      }
    } catch (err) {
      const message = err.response?.data?.error || 'Lỗi khi tạo sản phẩm. Vui lòng kiểm tra xem bạn đã tạo shop chưa.';
      setError(message);
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card className="shadow-sm p-4 mx-auto" style={{ maxWidth: '700px', marginTop: '20px' }}>
      <Card.Body>
        <h4 className="mb-4 text-center">Tạo Sản Phẩm</h4>
        {error && <Alert variant="danger">{error}</Alert>}
        {attributesLoading && <Alert variant="info">Đang tải danh sách thuộc tính...</Alert>}
        {!attributesLoading && attributes.length === 0 && (
          <Alert variant="warning">Không có thuộc tính nào khả dụng. Vui lòng thêm thuộc tính trước.</Alert>
        )}
        <Form onSubmit={handleSubmit}>
          <Row>
            <Col md={6}>
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
            </Col>
            <Col md={6}>
              <Form.Group className="mb-3" controlId="productPrice">
                <Form.Label>Giá (VND)</Form.Label>
                <Form.Control
                  type="number"
                  name="price"
                  value={formData.price}
                  onChange={handleChange}
                  placeholder="Nhập giá sản phẩm"
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
              value={formData.description}
              onChange={handleChange}
              placeholder="Nhập mô tả sản phẩm"
              rows={4}
            />
          </Form.Group>
          <Row>
          <Col md={5}>
                <Form.Group >
                  <Form.Label>Danh mục</Form.Label>
                  <Form.Select
                    name="categoryId"
                    // value={}
                    onChange={(e) => handleChange(e)}
                    required
                    disabled={ categories.length === 0}
                  >
                    {categories.map((category) => (
                      <option key={category.id} value={category.id}>
                        {category.name}
                      </option>
                    ))}
                  </Form.Select>
                </Form.Group>
              </Col>
           
            <Col md={6}>
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
            </Col>
            <Col md={6}>
              <Form.Group className="mb-3" controlId="productStock">
                <Form.Label>Ảnh</Form.Label>
                <Form.Control
                  type="file"
                  name="image"
                  ref={image}
                  required
                />
              </Form.Group>
            </Col>
          </Row>
          <h6 className="mt-4">Thuộc tính sản phẩm</h6>
          {formData.attributes.map((attr, index) => (
            <Row key={index} className="mb-3">
              <Col md={5}>
                <Form.Group controlId={`attributeId-${index}`}>
                  <Form.Label>Thuộc tính</Form.Label>
                  <Form.Select
                    name="attributeId"
                    value={attr.attributeId}
                    onChange={(e) => handleChange(e, index)}
                    required
                    disabled={attributesLoading || attributes.length === 0}
                  >
                    <option value="">Chọn thuộc tính</option>
                    {attributes.map((attribute) => (
                      <option key={attribute.id} value={attribute.id}>
                        {attribute.name}
                      </option>
                    ))}
                  </Form.Select>
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
                    placeholder="Nhập giá trị thuộc tính"
                    required
                  />
                </Form.Group>
              </Col>
              <Col md={2} className="d-flex align-items-end">
                <Button
                  variant="danger"
                  size="sm"
                  onClick={() => removeAttribute(index)}
                  disabled={formData.attributes.length === 1}
                >
                  Xóa
                </Button>
              </Col>
            </Row>
          ))}
          <Button
            variant="outline-primary"
            size="sm"
            onClick={addAttribute}
            className="mb-4"
            disabled={attributesLoading || attributes.length === 0}
          >
            Thêm thuộc tính
          </Button>
          <div className="text-center">
            <Button
              variant="primary"
              type="submit"
              disabled={loading || attributesLoading || attributes.length === 0}
              size="lg"
            >
              {loading ? 'Đang tạo...' : 'Tạo Sản Phẩm'}
            </Button>
          </div>
        </Form>
      </Card.Body>
    </Card>
  );
};

export default CreateProduct;