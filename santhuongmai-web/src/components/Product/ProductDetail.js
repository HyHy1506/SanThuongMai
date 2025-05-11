import React, { useEffect, useState } from "react";
import { Alert, Button, Card, Col, Container, Form, ListGroup, Row, Table, Modal } from "react-bootstrap";
import { useParams, Link } from "react-router-dom";
import { FaStar, FaStarHalfAlt, FaRegStar } from "react-icons/fa";
import MySpinner from "../layouts/MySpinner";
import Apis, { authApis, endpoints } from "../../configs/Apis";
import StarRating from "./StarRating";
import CommentSection from "./CommentSection";
import { useDispatch, useSelector } from "react-redux";
import { addToCart } from "../../actions/cartActions";
import { toast } from "react-toastify";
import { Xanh4, Xanh5 } from "../../utils/MyColors";

const ProductDetail = () => {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [relatedProducts, setRelatedProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showPairwiseCompareModal, setShowPairwiseCompareModal] = useState(false);
  const [selectedCompareProduct, setSelectedCompareProduct] = useState(null);
  const dispatch = useDispatch();
  const user = useSelector((state) => state.authentication);

  useEffect(() => {
    const fetchProduct = async () => {
      setLoading(true);
      try {
        const response = await Apis.get(endpoints.productDetail(id));
        setProduct(response.data);
        const relatedResponse = await Apis.get(
          `${endpoints.products}?categoryId=${response.data.categoryId}&page=1`
        );
        setRelatedProducts(relatedResponse.data.filter((p) => p.id !== parseInt(id)));
      } catch (error) {
        console.error("Error fetching product:", error);
        toast.error("Lỗi khi tải thông tin sản phẩm");
      } finally {
        setLoading(false);
      }
    };
    fetchProduct();
  }, [id]);

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
    toast.success("Đã thêm sản phẩm vào giỏ hàng");
  };

  const handlePairwiseCompare = async (relatedProduct) => {
    try {
      const res = await Apis.get(endpoints["products-with-id"](relatedProduct.id));
      setSelectedCompareProduct(res.data);
      setShowPairwiseCompareModal(true);
    } catch (err) {
      toast.error("Lỗi khi so sánh");
    }
  };

  if (loading) return <MySpinner />;
  if (!product) return <Alert variant="danger">Không tìm thấy sản phẩm!</Alert>;

  return (
    <>
      <style>
        {`
          @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap');
          
          .product-detail-custom {
            font-family: 'Poppins', sans-serif;
            background-color: #FFFFFF;
            padding: 2rem 0;
          }
          
          .product-card-custom {
            border: none;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(83, 125, 93, 0.1);
            transition: all 0.3s ease;
          }
          
          .product-card-custom:hover {
            box-shadow: 0 8px 16px rgba(83, 125, 93, 0.2);
          }
          
          .product-img-custom {
            height: 400px;
            object-fit: cover;
            border-bottom: 1px solid #9EBC8A;
          }
          
          .product-title-custom {
            font-size: 2rem;
            font-weight: 600;
            color: #537D5D;
          }
          
          .product-price-custom {
            font-size: 1.5rem;
            color: #73946B;
            font-weight: 500;
          }
          
          .btn-cart-custom {
            background-color: #73946B;
            border: none;
            color: #FFFFFF;
            padding: 0.75rem 1.5rem;
            border-radius: 20px;
            transition: all 0.3s ease;
          }
          
          .btn-cart-custom:hover {
            background-color: #9EBC8A;
            transform: scale(1.05);
          }
          
          .table-custom {
            background-color: #F9F9F9;
            border: 1px solid #9EBC8A;
            border-radius: 10px;
          }
          
          .related-card-custom {
            border: none;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(83, 125, 93, 0.1);
            transition: all 0.3s ease;
          }
          
          .related-card-custom:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 16px rgba(83, 125, 93, 0.2);
          }
          
          .modal-custom {
            font-family: 'Poppins', sans-serif;
          }
          
          .modal-title-custom {
            color: #537D5D;
            font-weight: 600;
          }
        `}
      </style>
      <Container fluid className="product-detail-custom">
        <Row className="mb-5">
          <Col md={6}>
            <Card className="product-card-custom">
              <Card.Img
                src={product.image}
                alt={product.name}
                className="product-img-custom"
              />
            </Card>
          </Col>
          <Col md={6}>
            <h2 className="product-title-custom">{product.name}</h2>
            <div className="d-flex align-items-center mb-2">
              {renderStars(product.averageRating)}
              <span className="ms-2 text-muted">({product.totalRatings} đánh giá)</span>
            </div>
            <h4 className="product-price-custom">{product.price.toLocaleString()} VNĐ</h4>
            <p className="text-muted">Danh mục: {product.categoryName}</p>
            <p className="text-muted">
              Cửa hàng:{" "}
              <Link to={`/shops/${product.shopId}`} style={{ color: '#537D5D' }}>
                {product.shopName}
              </Link>
            </p>
            <div className="mt-3">
              <Button variant="primary" size="lg" onClick={handleAddToCart} className="btn-cart-custom me-2">
                Thêm vào giỏ hàng
              </Button>
            </div>
          </Col>
          <Col lg={4}>
            <h4 className="mt-3"><strong>Thông số kỹ thuật</strong></h4>
            <Table className="table-custom" striped bordered hover responsive>
              <thead>
                <tr>
                  <th>Thuộc tính</th>
                  <th>Thông tin</th>
                </tr>
              </thead>
              <tbody>
                {Object.entries(product.attributes).length === 0 ? (
                  <tr><td colSpan="2"><h5>Chưa có thông số</h5></td></tr>
                ) : Object.entries(product.attributes).map(([key, value]) => (
                  <tr key={key}>
                    <td>{key}</td>
                    <td>{value}</td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </Col>
        </Row>
        <Row className="mb-5">
          <Col>
            <StarRating productId={product.id} />
          </Col>
        </Row>
        <Row className="mb-5">
          <Col>
            <CommentSection productId={product.id} />
          </Col>
        </Row>
        <Row>
          <h3 className="fw-bold mb-3" style={{color:Xanh4}}>Sản phẩm liên quan</h3>
          {relatedProducts.length === 0 && (
            <Alert variant="info">Không tìm thấy sản phẩm liên quan!</Alert>
          )}
          {relatedProducts.map((related) => (
            <Col key={related.id} xs={12} sm={6} md={4} lg={3} className="mb-4">
              <Card className="related-card-custom h-100">
                <Card.Img
                  variant="top"
                  src={related.image}
                  alt={related.name}
                  style={{ height: "200px", objectFit: "contain" }}
                />
                <Card.Body>
                  <Card.Title className="fs-6 fw-bold" style={{color:Xanh4}}>{related.name}</Card.Title>
                  <Card.Text className="text-muted">{related.categoryName}</Card.Text>
                  <Card.Text className="fw-bold " style={{color:Xanh5}}>
                    {related.price.toLocaleString()} VNĐ
                  </Card.Text>
                  <div className="d-flex justify-content-between">
                    <Button
                      as={Link}
                      to={`/products/${related.id}`}
                      variant="outline-primary"
                      size="sm"
                      className="btn-cart-custom"
                    >
                      Xem chi tiết
                    </Button>
                    <Button
                      variant="outline-secondary"
                      size="sm"
                      onClick={() => handlePairwiseCompare(related)}
                      className="btn-cart-custom"
                    >
                      So sánh
                    </Button>
                   
                  </div>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
        <Modal
          show={showPairwiseCompareModal}
          onHide={() => setShowPairwiseCompareModal(false)}
          size="lg"
          centered
          className="modal-custom"
        >
          <Modal.Header closeButton>
            <Modal.Title className="modal-title-custom">
              So sánh: {product.name} vs {selectedCompareProduct?.name}
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {selectedCompareProduct && (
              <Table striped bordered hover responsive>
                <thead>
                  <tr>
                    <th>Thuộc tính</th>
                    <th>{product.name}</th>
                    <th>{selectedCompareProduct.name}</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>Giá (VNĐ)</td>
                    <td>{product.price.toLocaleString()}</td>
                    <td>{selectedCompareProduct.price.toLocaleString()}</td>
                  </tr>
                  <tr>
                    <td>Đánh giá</td>
                    <td>{product.averageRating} ({product.totalRatings} lượt)</td>
                    <td>
                      {selectedCompareProduct.averageRating} ({selectedCompareProduct.totalRatings} lượt)
                    </td>
                  </tr>
                  {Object.keys(product.attributes).map((key) => (
                    <tr key={key}>
                      <td>{key}</td>
                      <td>{product.attributes[key]}</td>
                      <td>{selectedCompareProduct.attributes[key] || "N/A"}</td>
                    </tr>
                  ))}
                  {Object.keys(selectedCompareProduct.attributes)
                    .filter((key) => !product.attributes[key])
                    .map((key) => (
                      <tr key={key}>
                        <td>{key}</td>
                        <td>N/A</td>
                        <td>{selectedCompareProduct.attributes[key]}</td>
                      </tr>
                    ))}
                </tbody>
              </Table>
            )}
          </Modal.Body>
          <Modal.Footer>
            <Button
              variant="secondary"
              onClick={() => setShowPairwiseCompareModal(false)}
              className="btn-cart-custom"
            >
              Đóng
            </Button>
          </Modal.Footer>
        </Modal>
      </Container>
    </>
  );
};

export default ProductDetail;