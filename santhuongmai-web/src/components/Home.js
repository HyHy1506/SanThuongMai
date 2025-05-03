import { useEffect, useState } from "react";
import { Alert, Button, Card, Col, Container, Form, Row, Spinner } from "react-bootstrap";
import { useSearchParams, Link } from "react-router-dom";
import { useSelector } from "react-redux";
import axios from "axios";
import MySpinner from "./layouts/MySpinner";
import Apis, { endpoints } from "../configs/Apis";
import ProductCard from "./Product/ProductCard";

const Home = () => {
    const [products, setProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [page, setPage] = useState(1);
    const [loading, setLoading] = useState(false);
    const [selectedCategory, setSelectedCategory] = useState(null);
    const userAuthentication = useSelector((state) => state.authentication);

    const fetchCategories = async () => {
        try {
            const response = await Apis.get(endpoints.categories);
            setCategories(response.data);
        } catch (error) {
            console.error("Lỗi lấy danh mục:", error);
        }
    };
    useEffect(() => {
        fetchCategories();
    }, []);

    const loadProducts = async () => {
        setLoading(true);
        try {
            const params = new URLSearchParams();
            params.append("page", page);
            let url=`${endpoints.products}?${params}`

            const response = await Apis.get(url);
            setProducts((prev) => (page === 1 ? response.data : [...prev, ...response.data]));
        } catch (error) {
            console.error("Lỗi lấy sản phẩm:", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (page > 0) loadProducts();
    }, [page]);

    const handleCategorySelect=()=>{

    }


    // Load more products
    const loadMore = () => {
        if (!loading && page > 0) setPage(page + 1);
    };

    return (
        <Container fluid className="py-4 " >

            {/* Category Filter */}
            <div className="mb-4 d-flex overflow-auto">
                <Button
                    variant={selectedCategory === null ? "primary" : "outline-primary"}
                    className="me-2 rounded-pill"
                    onClick={() => handleCategorySelect(null)}
                >
                    All
                </Button>
                {categories.map((category) => (
                    <Button
                        key={category.id}
                        variant={selectedCategory === category.id ? "primary" : "outline-primary"}
                        className="me-2 rounded-pill"
                        onClick={() => handleCategorySelect(category.id)}
                    >
                        {category.name}
                    </Button>
                ))}
            </div>

            {/* show products */}
            {products.length === 0 && !loading && (
                <Alert variant="info" className="text-center">
                    No products found!
                </Alert>
            )}
            <Row>
                {products.map((product) => (
                     <Col key={product.id} xs={12} sm={6} md={4} lg={3} className="mb-4">
        
                         <ProductCard product={product}/>
                     </Col>
                ))}
            </Row>

            {/* Load More Button */}
            {page > 0 && products.length > 0 && (
                <div className="text-center mt-4">
                    <Button
                        variant="primary"
                        onClick={loadMore}
                        disabled={loading}
                        className="rounded-pill px-4"
                    >
                        {loading ? <Spinner animation="border" size="sm" /> : "Load More"}
                    </Button>
                </div>
            )}

            {/* Loading Spinner */}
            {loading && <MySpinner />}
        </Container>
    );
};

export default Home;