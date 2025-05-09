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

const ProductDetail = () => {
    const { id } = useParams();
    const [product, setProduct] = useState(null);
    const [relatedProducts, setRelatedProducts] = useState([]);
    const [loading, setLoading] = useState(false);
    const [showPairwiseCompareModal, setShowPairwiseCompareModal] = useState(false);
    const [selectedCompareProduct, setSelectedCompareProduct] = useState(null);
    const dispatch = useDispatch();
    const user = useSelector((state) => state.authentication);
    // Fetch product details and related products
    useEffect(() => {
        const fetchProduct = async () => {
            setLoading(true);
            try {
                const response = await Apis.get(endpoints.productDetail(id));
                setProduct(response.data);

                // Fetch related products in the same category
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

    // Render star rating
    const renderStars = (rating) => {
        const stars = [];
        for (let i = 1; i <= 5; i++) {
            if (i <= Math.floor(rating)) {
                stars.push(<FaStar key={i} color="#ffc107" />);
            } else if (i === Math.ceil(rating) && rating % 1 !== 0) {
                stars.push(<FaStarHalfAlt key={i} color="#ffc107" />);
            } else {
                stars.push(<FaRegStar key={i} color="#ffc107" />);
            }
        }
        return stars;
    };

    // Handle add to cart
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
        toast.success("Đã thêm sản phẩm vào giỏ hàng");
    };

    // Handle pairwise comparison
    const handlePairwiseCompare = async (relatedProduct) => {
        try {
            const res = await Apis.get(endpoints["products-with-id"](relatedProduct.id))

            setSelectedCompareProduct(res.data);
            setShowPairwiseCompareModal(true);
        } catch (err) {
            toast.error("Lỗi khi so sánh")
        }

    };

    if (loading) return <MySpinner />;
    if (!product) return <Alert variant="danger">Không tìm thấy sản phẩm!</Alert>;

    return (
        <Container fluid className="py-4">
            {/* Product Details */}
            <Row className="mb-5">
                <Col md={6}>
                    <Card className="border-0 shadow-sm">
                        <Card.Img
                            src={product.image}
                            alt={product.name}
                            style={{ height: "400px", objectFit: "cover" }}
                        />
                    </Card>
                </Col>
                <Col md={6}>
                    <h2 className="fw-bold">{product.name}</h2>
                    <div className="d-flex align-items-center mb-2">
                        {renderStars(product.averageRating)}
                        <span className="ms-2 text-muted">({product.totalRatings} đánh giá)</span>
                    </div>
                    <h4 className="text-primary">{product.price.toLocaleString()} VNĐ</h4>
                    <p className="text-muted">Danh mục: {product.categoryName}</p>
                    <p className="text-muted">Cửa hàng: {" "}
                        <Link to={`/shops/${product.shopId}`} style={{ color: '#2e7d32' }}>
                            {product.shopName}
                        </Link>
                    </p>

                    <div className="mt-3">
                        <Button variant="primary" size="lg" onClick={handleAddToCart} className="me-2">
                            Thêm vào giỏ hàng
                        </Button>

                    </div>
                </Col>
                <Col lg={4}>
                    <h4 className="mt-3"><strong>Thông số kỹ thuật</strong></h4>
                    <Table variant="flush" striped bordered hover responsive>
                        <thead>
                            <tr>
                                <th>Thuộc tính</th>
                                <th>Thông tin</th>

                            </tr>
                        </thead>
                        <tbody>



                            {Object.entries(product.attributes).length == 0 ? <h5>Chưa có thông số</h5> : Object.entries(product.attributes).map(([key, value]) => (
                                // <ListGroup.Item key={key}>
                                //     <strong>{key}:</strong> {value}
                                // </ListGroup.Item>
                                <tr key={key}>
                                    <td>{key}</td>
                                    <td>{value}</td>
                                </tr>
                            ))}

                        </tbody>
                    </Table>
                </Col>
            </Row>

            {/* Star Rating Section */}
            <Row className="mb-5">
                <Col>
                    <StarRating productId={product.id} />
                </Col>
            </Row>

            {/* Comments Section */}
            <Row className="mb-5">
                <Col>
                    <CommentSection productId={product.id} />
                </Col>
            </Row>

            {/* Related Products */}
            <Row>
                <h3 className="fw-bold mb-3">Sản phẩm liên quan</h3>
                {relatedProducts.length === 0 && (
                    <Alert variant="info">Không tìm thấy sản phẩm liên quan!</Alert>
                )}
                {relatedProducts.map((related) => (
                    <Col key={related.id} xs={12} sm={6} md={4} lg={3} className="mb-4">
                        <Card className="h-100 shadow-sm border-0">
                            <Card.Img
                                variant="top"
                                src={related.image}
                                alt={related.name}
                                style={{ height: "200px", objectFit: "contain" }}
                            />
                            <Card.Body>
                                <Card.Title className="fs-6 fw-bold">{related.name}</Card.Title>
                                <Card.Text className="text-muted">{related.categoryName}</Card.Text>
                                <Card.Text className="fw-bold text-primary">
                                    {related.price.toLocaleString()} VNĐ
                                </Card.Text>
                                <div className="d-flex justify-content-between">
                                    <Button
                                        as={Link}
                                        to={`/products/${related.id}`}
                                        variant="outline-primary"
                                        size="sm"
                                    >
                                        Xem chi tiết
                                    </Button>
                                    <Button
                                        variant="outline-secondary"
                                        size="sm"
                                        onClick={() => handlePairwiseCompare(related)}
                                    >
                                        So sánh
                                    </Button>
                                    <Button
                                        variant="primary"
                                        size="sm"
                                        onClick={() => {
                                            dispatch(
                                                addToCart({
                                                    productId: related.id,
                                                    name: related.name,
                                                    price: related.price,
                                                    image: related.image,
                                                })
                                            );
                                            toast.success("Đã thêm sản phẩm vào giỏ hàng");
                                        }}
                                    >
                                        Thêm vào giỏ
                                    </Button>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                ))}
            </Row>



            {/* Pairwise Comparison Modal */}
            <Modal
                show={showPairwiseCompareModal}
                onHide={() => setShowPairwiseCompareModal(false)}
                size="lg"
                centered
            >
                <Modal.Header closeButton>
                    <Modal.Title>
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
                    >
                        Đóng
                    </Button>
                </Modal.Footer>
            </Modal>
        </Container>
    );
};

export default ProductDetail;