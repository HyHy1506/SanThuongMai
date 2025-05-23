import React from 'react';
import { Card, Button, Row, Col } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { addToCart } from '../../actions/cartActions';
import { toast } from 'react-toastify';
import { Xanh5 } from '../../utils/MyColors';
import { FaStar, FaStarHalfAlt, FaRegStar } from "react-icons/fa";

const ProductCard = ({ product }) => {
  const dispatch = useDispatch();
  const user = useSelector((state) => state.authentication);

  const handleAddToCart = () => {
    if (user == null) {
      toast.error("Vui lòng đăng nhập để thêm vào giỏ hàng!");
      return;
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
  const renderStars = (rating) => {
    const stars = [];
    for (let i = 1; i <= 5; i++) {
      if (i <= Math.floor(rating)) {
        stars.push(<FaStar key={i} color="#9EBC8A" />);
      } else if (i === Math.ceil(rating) && rating % 1 !== 0) {
        stars.push(<FaStarHalfAlt key={i} color="#9EBC8A" />);
      } else {
        stars.push(<FaRegStar key={i} color="#9EBC8A" />);
      }
    }
    return stars;
  };
  return (
    <>
      <style>
        {`
          @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap');
          
          .product-card-custom {
            width: 18rem;
            height: 400px;
            border: none;
            border-radius: 10px;
            background-color: #FFFFFF;
            box-shadow: 0 4px 8px rgba(83, 125, 93, 0.1);
            overflow: hidden;
            transition: all 0.3s ease;
            font-family: 'Poppins', sans-serif;
          }
          
          .product-card-custom:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 16px rgba(83, 125, 93, 0.2);
          }
          
          .card-img-custom {
            height: 200px;
            object-fit: cover;
            border-bottom: 1px solid #9EBC8A;
          }
          
          .card-title-custom {
            font-size: 1.2rem;
            font-weight: 500;
            color: #537D5D;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            margin-bottom: 0.5rem;
          }
          
          .card-text-custom {
            font-size: 1rem;
            color: ${Xanh5};
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            margin-bottom: 0.5rem;
          }
          
          .rating-custom {
            color: #9EBC8A;
            font-size: 0.9rem;
            margin-bottom: 0.5rem;
          }
          
          .btn-detail-custom {
            background-color: #9EBC8A;
            color: #FFFFFF;
            border: none;
            padding: 0.5rem 1rem;
            border-radius: 20px;
            transition: all 0.3s ease;
          }
          
          .btn-detail-custom:hover {
            background-color: #73946B;
            color: #FFFFFF;
            transform: scale(1.05);
          }
          
          .btn-cart-custom {
            background-color: #537D5D;
            color: #FFFFFF;
            border: none;
            padding: 0.5rem 1rem;
            border-radius: 20px;
            transition: all 0.3s ease;
          }
          
          .btn-cart-custom:hover {
            background-color: #73946B;
            transform: scale(1.05);
          }
        `}
      </style>
      <Card className="product-card-custom">
        <Card.Img
          variant="top"
          src={product.image}
          alt={product.name}
          className="card-img-custom"
        />
        <Card.Body className="d-flex flex-column justify-content-between">
          <div>
            <Card.Title className="card-title-custom"  as={Link}
                  to={`/products/${product.id}`}>{product.name}</Card.Title>
            <Card.Text className="card-text-custom">
              Giá: {product.price.toLocaleString()} VND
            </Card.Text>
            {/* <div className="rating-custom">
              ★★★★★ (12) 
            </div> */}
            <div className="rating-custom">
              {renderStars(product.averageRating)}
              <span className="ms-2 text-muted">{product.averageRating} </span>
            </div>
          </div>
          <Card.Footer style={{ background: 'none', borderTop: 'none' }}>
            <Row>
              <Col>
                <Button
                  variant="primary"
                  onClick={handleAddToCart}
                  className="btn-cart-custom w-100"
                >
                  Thêm vào giỏ
                </Button>
              </Col>
            </Row>
          </Card.Footer>
        </Card.Body>
      </Card>
    </>
  );
};

export default ProductCard;