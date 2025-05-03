import { useEffect, useState } from "react";
import { Alert, Button, Card, Col, Container, Form, Row, Spinner } from "react-bootstrap";
import { useSearchParams, Link } from "react-router-dom";
import { useSelector } from "react-redux";

const ProductCard = ({ product }) => {
    return (
        <Card className="h-100 shadow-sm border-0">
            <Card.Img
                variant="top"
                src={product.image}
                alt={product.name}
                style={{ height: "200px", objectFit: "cover" }}
            />
            <Card.Body>
                <Card.Title className="fs-6 fw-bold">{product.name}</Card.Title>
                <Card.Text className="text-muted">{product.categoryName}</Card.Text>
                <Card.Text className="fw-bold text-primary">
                    {product.price.toLocaleString()} VNĐ
                </Card.Text>
                <div className="d-flex justify-content-between">
                    <Button
                        as={Link}
                        to={`/product/${product.id}`}
                        variant="outline-primary"
                        size="sm"
                    >
                        View Details
                    </Button>
                    <Button variant="success" size="sm">
                        Add to Cart
                    </Button>
                </div>
            </Card.Body>
        </Card>
    )

}
export default ProductCard