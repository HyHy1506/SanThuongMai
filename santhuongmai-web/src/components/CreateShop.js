import React, { useState } from 'react';
import { Form, Button, Alert } from 'react-bootstrap';

import { toast } from 'react-toastify';
import Apis, { authApis, endpoints } from '../configs/Apis';

const CreateShop = () => {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    address: '',
    image: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);


  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.name || !formData.address) {
      toast.info('Vui lòng điền tên và địa chỉ cửa hàng');
      return;
    }
    try {
      setLoading(true)
      const resUser = await authApis().get(endpoints["current-user"]);
      let newForm={
        ...formData,userId:resUser.data.id
      }
      console.log(newForm)
      console.log(resUser.data.id)

      const res = await authApis().post(endpoints.shops, newForm)
      toast.info(newForm)
      toast.success('Tạo cửa hàng thành công!');
      setFormData({ name: '', description: '', address: '', image: '' });
    } catch (err) {
      toast.error(err.response.data.error)
    } finally {

      setLoading(true)
    }

  };

  return (
    <div>
      <h4>Tạo Cửa Hàng</h4>
      {error && <Alert variant="danger">{error}</Alert>}
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3" controlId="shopName">
          <Form.Label>Tên cửa hàng</Form.Label>
          <Form.Control
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            placeholder="Nhập tên cửa hàng"
            required
          />
        </Form.Group>
        <Form.Group className="mb-3" controlId="shopDescription">
          <Form.Label>Mô tả</Form.Label>
          <Form.Control
            as="textarea"
            name="description"
            value={formData.description}
            onChange={handleChange}
            placeholder="Nhập mô tả cửa hàng"
          />
        </Form.Group>
        <Form.Group className="mb-3" controlId="shopAddress">
          <Form.Label>Địa chỉ</Form.Label>
          <Form.Control
            type="text"
            name="address"
            value={formData.address}
            onChange={handleChange}
            placeholder="Nhập địa chỉ cửa hàng"
            required
          />
        </Form.Group>
        {/* <Form.Group className="mb-3" controlId="shopImage">
          <Form.Label>URL hình ảnh</Form.Label>
          <Form.Control
            type="text"
            name="image"
            value={formData.image}
            onChange={handleChange}
            placeholder="Nhập URL hình ảnh"
          />
        </Form.Group> */}
        <Button variant="primary" type="submit" disabled={loading}>
          {loading ? 'Đang tạo...' : 'Tạo Cửa Hàng'}
        </Button>
      </Form>
    </div>
  );
};

export default CreateShop;