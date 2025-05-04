import React from 'react';
import { Card, Button, Row, Col } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { addToCart } from '../../actions/cartActions';
import { toast } from 'react-toastify';

const ProductCard = ({ product }) => {
  const dispatch = useDispatch();

  const handleAddToCart = () => {
    dispatch(
      addToCart({
        productId: product.id,
        name: product.name,
        price: product.price,
        image: product.image,
      })
    );
    toast.success('Đã thêm sản phẩm vào giỏ hàng');
  };

  return (
    <Card style={{ width: '18rem', marginBottom: '1rem' }}>
      <Card.Img variant="top" src={product.image} alt={product.name} />
      <Card.Body>
        <Card.Title>{product.name}</Card.Title>
        <Card.Text>Giá: {product.price.toLocaleString()} VND</Card.Text>
        <Card.Text>Cửa hàng: {product.shopName}</Card.Text>
        <Row>
          <Col>
            <Button
              variant="outline-primary"
              as={Link}
              to={`/products/${product.id}`}
              className="me-2"
            >
              Xem chi tiết
            </Button>
            <Button variant="primary" onClick={handleAddToCart}>
              Thêm vào giỏ
            </Button>
          </Col>
        </Row>
      </Card.Body>
    </Card>
  );
};

export default ProductCard;