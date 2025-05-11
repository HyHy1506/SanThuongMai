import { useEffect, useState } from "react";
import { Alert, Button, Card, Col, Container, Row, Spinner } from "react-bootstrap";
import { useSearchParams, Link } from "react-router-dom";
import { useSelector } from "react-redux";
import axios from "axios";
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
      let url = `${endpoints.products}?${params}`;
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

  const handleCategorySelect = (categoryId) => {
    setSelectedCategory(categoryId);
    setPage(1); // Reset page when category changes
    setProducts([]); // Clear products to reload
    const loadFilteredProducts = async () => {
      setLoading(true);
      try {
        const params = new URLSearchParams();
        params.append("page", 1);
        if (categoryId) params.append("categoryId", categoryId);
        const url = `${endpoints.products}?${params}`;
        const response = await Apis.get(url);
        setProducts(response.data);
      } catch (error) {
        console.error("Lỗi lọc sản phẩm:", error);
      } finally {
        setLoading(false);
      }
    };
    loadFilteredProducts();
  };

  const loadMore = () => {
    if (!loading && page > 0) setPage(page + 1);
  };

  return (
    <>
      <style>
        {`
          @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap');
          
          .home-container-custom {
            background-color:rgb(255, 255, 255);
            padding: 2rem 0;
            min-height: 100vh;
            font-family: 'Poppins', sans-serif;
          }
          
          .category-btn-custom {
            background-color: #73946B;
            color: #FFFFFF;
            border: none;
            padding: 0.5rem 1.5rem;
            margin: 0 0.5rem 0.5rem 0;
            border-radius: 20px;
            transition: all 0.3s ease;
          }
          
          .category-btn-custom:hover,
          .category-btn-custom.active {
            background-color: #537D5D;
            color: #FFFFFF;
            transform: scale(1.05);
            box-shadow: 0 4px 8px rgba(83, 125, 93, 0.3);
          }
          
          .product-card-custom {
            background-color: #FFFFFF;
            border: none;
            border-radius: 10px;
            overflow: hidden;
            transition: all 0.3s ease;
            margin-bottom: 1.5rem;
          }
          
          .product-card-custom:hover {
            transform: translateY(-5px) scale(1.02);
            box-shadow: 0 8px 16px rgba(83, 125, 93, 0.3);
          }
          
          .load-more-btn-custom {
            background-color: #73946B;
            color: #FFFFFF;
            border: none;
            padding: 0.75rem 2rem;
            border-radius: 20px;
            transition: all 0.3s ease;
          }
          
          .load-more-btn-custom:hover {
            background-color: #9EBC8A;
            color: #537D5D;
            transform: scale(1.05);
            box-shadow: 0 4px 8px rgba(83, 125, 93, 0.3);
          }
          
          .spinner-custom {
            color: #537D5D;
          }
        `}
      </style>
      <Container fluid className="home-container-custom">
        {/* Category Filter */}
        <div className="mb-4 d-flex overflow-auto">
          <Button
            variant="primary"
            className={`category-btn-custom ${selectedCategory === null ? "active" : ""}`}
            onClick={() => handleCategorySelect(null)}
          >
            All
          </Button>
          {categories.map((category) => (
            <Button
              key={category.id}
              variant="outline-primary"
              className={`category-btn-custom ${selectedCategory === category.id ? "active" : ""}`}
              onClick={() => handleCategorySelect(category.id)}
            >
              {category.name}
            </Button>
          ))}
        </div>

        {/* Show Products */}
        {products.length === 0 && !loading && (
          <Alert variant="info" className="text-center">
            No products found!
          </Alert>
        )}
        <Row>
          {products.map((product) => (
            <Col key={product.id} xs={12} sm={6} md={4} lg={3} className="mb-4">
              <ProductCard product={product} />
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
              className="load-more-btn-custom"
            >
              {loading ? <Spinner animation="border" size="sm" className="spinner-custom" /> : "Load More"}
            </Button>
          </div>
        )}
      </Container>
    </>
  );
};

export default Home;