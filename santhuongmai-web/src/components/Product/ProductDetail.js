import { useEffect, useState } from "react";
import { Alert, Button, Card, Col, Container, Form, ListGroup, Row } from "react-bootstrap";
import { useParams, Link } from "react-router-dom";
import axios from "axios";
import { FaStar, FaStarHalfAlt, FaRegStar } from "react-icons/fa";
import MySpinner from "../layouts/MySpinner";
import Apis, { endpoints } from "../../configs/Apis";

const ProductDetail = () => {
    const { id } = useParams();
    const [product, setProduct] = useState(null);
    const [relatedProducts, setRelatedProducts] = useState([]);
    const [loading, setLoading] = useState(false);
    const [commentText, setCommentText] = useState("");

    // Sample comments data
    const comments = [
        {
            id: 1,
            user: "JohnDoe",
            text: "Great phone, amazing camera quality!",
            date: "2025-04-10",
            replies: [
                { id: 2, user: "ShopOwner", text: "Thank you for your feedback!", date: "2025-04-11" }
            ]
        },
        {
            id: 3,
            user: "JaneSmith",
            text: "Battery life could be better.",
            date: "2025-04-12",
            replies: []
        }
    ];

    // Sample rating data
    const rating = 4.5;
    const reviews = 120;

    // Fetch product details and related products
    useEffect(() => {
        const fetchProduct = async () => {
            setLoading(true);
            try {
                const response = await Apis.get(endpoints.productDetail(id));
                setProduct({
                    ...response.data,
                    description: "A high-performance smartphone with advanced camera and sleek design."
                });

                // Fetch related products
                const relatedResponse = await axios.get(
                    `http://localhost:8080/SanThuongMai/api/products?categoryId=${response.data.categoryId}&page=1`
                );
                setRelatedProducts(relatedResponse.data.filter((p) => p.id !== parseInt(id)));
            } catch (error) {
                console.error("Error fetching product:", error);
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

    // Handle comment submission (mock)
    const handleCommentSubmit = (e) => {
        e.preventDefault();
        console.log("New comment:", commentText);
        setCommentText("");
    };

    if (loading) return <MySpinner />;
    if (!product) return <Alert variant="danger">Product not found!</Alert>;

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
                        {renderStars(rating)}
                        <span className="ms-2 text-muted">({reviews} reviews)</span>
                    </div>
                    <h4 className="text-primary">{product.price.toLocaleString()} VNĐ</h4>
                    <p className="text-muted">Category: {product.categoryName}</p>
                    <p className="text-muted">Shop: {product.shopName}</p>
                    <p>{product.description}</p>
                    <Button variant="primary" size="lg">
                        Add to Cart
                    </Button>
                </Col>
            </Row>

            {/* Comments Section */}
            <Row className="mb-5">
                <Col>
                    <h3 className="fw-bold mb-3">Comments</h3>
                    <ListGroup variant="flush">
                        {comments.map((comment) => (
                            <ListGroup.Item key={comment.id} className="border-0">
                                <div>
                                    <strong>{comment.user}</strong> <small>{comment.date}</small>
                                    <p>{comment.text}</p>
                                    {comment.replies.map((reply) => (
                                        <div key={reply.id} className="ms-4 mt-2">
                                            <strong>{reply.user}</strong> <small>{reply.date}</small>
                                            <p>{reply.text}</p>
                                        </div>
                                    ))}
                                </div>
                            </ListGroup.Item>
                        ))}
                    </ListGroup>
                    <Form onSubmit={handleCommentSubmit} className="mt-3">
                        <Form.Group className="mb-3">
                            <Form.Control
                                as="textarea"
                                rows={3}
                                placeholder="Write your comment..."
                                value={commentText}
                                onChange={(e) => setCommentText(e.target.value)}
                            />
                        </Form.Group>
                        <Button type="submit" variant="primary">
                            Submit Comment
                        </Button>
                    </Form>
                </Col>
            </Row>

            {/* Related Products */}
            <Row>
                <h3 className="fw-bold mb-3">Related Products</h3>
                {relatedProducts.length === 0 && (
                    <Alert variant="info">No related products found!</Alert>
                )}
                {relatedProducts.map((related) => (
                    <Col key={related.id} xs={12} sm={6} md={4} lg={3} className="mb-4">
                        <Card className="h-100 shadow-sm border-0">
                            <Card.Img
                                variant="top"
                                src={related.image}
                                alt={related.name}
                                style={{ height: "200px", objectFit: "cover" }}
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
                                        to={`/product/${related.id}`}
                                        variant="outline-primary"
                                        size="sm"
                                    >
                                        View Details
                                    </Button>
                                    <Button variant="primary" size="sm">
                                        Add to Cart
                                    </Button>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                ))}
            </Row>
        </Container>
    );
};

export default ProductDetail;