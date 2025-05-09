import React from 'react';
import { Card, Button, Row, Col } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { addToCart } from '../../actions/cartActions';
import { toast } from 'react-toastify';

const ProductCard = ({ product }) => {
  const dispatch = useDispatch();
  const user = useSelector((state) => state.authentication);

  const handleAddToCart = () => {
    if (user == null) {
      toast.error("Vui lòng đăng nhập để theo vào giỏ hàng!")
      return
    }
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
    <Card
      style={{
        width: '18rem',
        height: '350px',
        minHeight: '350px',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'space-between',
        marginBottom: '1rem'
      }}
    >
      <Card.Img
        style={{ height: '150px', objectFit: 'cover' }}
        variant="top"
        src={product.image}
        alt={product.name}
      />
      <Card.Body style={{ flexGrow: 1 }}>
        <Card.Title style={{
          fontSize: "1.1rem", overflow: "hidden",
          textOverflow: "ellipsis",
          whiteSpace: "nowrap"
        }}>{product.name}</Card.Title>
        <Card.Text style={{
          overflow: "hidden",
          textOverflow: "ellipsis",
          whiteSpace: "nowrap"
        }}>Giá: {product.price.toLocaleString()} VND</Card.Text>
       
        <Card.Text style={{
            overflow: "hidden",
            textOverflow: "ellipsis",
            whiteSpace: "nowrap",

          }}>
          Cửa hàng:{' '}
          <Link to={`/shops/${product.shopId}`} style={{ color: '#2e7d32' }}>
            {product.shopName}
          </Link>
        </Card.Text>
      </Card.Body>
      <Card.Footer style={{ background: "white", borderTop: "none" }}>
        <Row>
          <Col>
            <Button
              variant="outline-primary"
              as={Link}
              to={`/products/${product.id}`}
              className="me-2 mb-2"
            >
              Xem chi tiết
            </Button>
            <Button variant="primary" onClick={handleAddToCart}>
              Thêm vào giỏ
            </Button>
          </Col>
        </Row>
      </Card.Footer>
    </Card>
  );
};

export default ProductCard;