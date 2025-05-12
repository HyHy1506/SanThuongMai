import { useEffect, useState } from "react";
import { Alert, Button, Card, Col, Container, Form, Row, Spinner } from "react-bootstrap";
import { useSearchParams, Link } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../layouts/MySpinner";
import ProductCard from "../Product/ProductCard";
import { Xanh4 } from "../../utils/MyColors";

const SearchHome = () => {
    const [products, setProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [shops, setShops] = useState([]);
    const [page, setPage] = useState(1);
    const [triger, setTriger] = useState(true);
    const [loading, setLoading] = useState(false);
    const [searchParams] = useSearchParams();
    const [filters, setFilters] = useState({
        kw: "",
        categoryId: "",
        shopId: "",
        fromPrice: "",
        toPrice: "",
        sortBy: "",
        orderBy: ""
    });

    const fetchCategories = async () => {
        try {
            const response = await Apis.get(endpoints.categories);
            setCategories(response.data);
        } catch (error) {
            console.error("Error fetching categories:", error);
        }
    };

    const fetchShops = async () => {
        try {
            const response = await Apis.get(endpoints.shops);
            setShops(response.data);
        } catch (error) {
            console.error("Error fetching shops:", error);
            // Mock shops if API not available
            setShops([
                { id: 1, name: "Cửa Hàng Điện Tử A Toan" },
                { id: 2, name: "Tech Shop" }
            ]);
        }
    };

    // Fetch categories and shops on mount
    useEffect(() => {
        fetchCategories();
        fetchShops();
    }, []);

    // Update keyword from URL
    useEffect(() => {
        console.log("loadddd")
        const kw = searchParams.get("kw") || "";
        setFilters((prev) => ({ ...prev, kw }));
        setPage(1);
        setProducts([]);
        setTriger(pre => !pre)
    }, [searchParams]);

    // Fetch products based on filters and page
    const loadProducts = async () => {
        setLoading(true);
        try {
            const kw = searchParams.get("kw") || "";
            const params = new URLSearchParams();
            params.append("page", page);
            params.append("kw", kw);
            if (filters.categoryId) params.append("categoryId", filters.categoryId);
            if (filters.shopId) params.append("shopId", filters.shopId);
            if (filters.fromPrice) params.append("fromPrice", filters.fromPrice);
            if (filters.toPrice) params.append("toPrice", filters.toPrice);
            if (filters.sortBy) params.append("sortBy", filters.sortBy);
            if (filters.orderBy) params.append("orderBy", filters.orderBy);
            params.append("isActive", true);

            const response = await Apis.get(`${endpoints.products}?${params}`);
            console.log(`${endpoints.products}?${params}`)
            setProducts((prev) => (page === 1 ? response.data : [...prev, ...response.data]));
        } catch (error) {
            console.error("Error fetching products:", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (page > 0) loadProducts();
    }, [page, triger]);

    // Handle filter changes
    const handleFilterChange = (e) => {
        const { name, value } = e.target;
        setFilters((prev) => ({ ...prev, [name]: value }));

    };
    const handleClickFilter = (e) => {
        setPage(1);
        setProducts([]);
        setTriger(pre => !pre)

    };
    // Load more products
    const loadMore = () => {
        if (!loading && page > 0) setPage(page + 1);
    };

    return (
        <Container fluid className="py-4">
            <h2 className="fw-bold mb-4">Tìm kiếm</h2>
            <Row>
                {/* Filter Sidebar */}
                <Col md={3} className="mb-4">
                    <Card className="p-3 shadow-sm border-0">
                        <h5 className="fw-bold mb-3">Bộ lọc</h5>
                        <Form>
                            <Form.Group className="mb-3">
                                <Form.Label>Danh mục</Form.Label>
                                <Form.Select
                                    name="categoryId"
                                    value={filters.categoryId}
                                    onChange={handleFilterChange}
                                >
                                    <option value="">All Categories</option>
                                    {categories.map((category) => (
                                        <option key={category.id} value={category.id}>
                                            {category.name}
                                        </option>
                                    ))}
                                </Form.Select>
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Của hàng</Form.Label>
                                <Form.Select
                                    name="shopId"
                                    value={filters.shopId}
                                    onChange={handleFilterChange}
                                >
                                    <option value="">All Shops</option>
                                    {shops.map((shop) => (
                                        <option key={shop.id} value={shop.id}>
                                            {shop.name}
                                        </option>
                                    ))}
                                </Form.Select>
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Giá</Form.Label>
                                <Row>
                                    <Col>
                                        <Form.Control
                                            type="number"
                                            name="fromPrice"
                                            placeholder="Tối thiểu"
                                            value={filters.fromPrice}
                                            onChange={handleFilterChange}
                                        />
                                    </Col>
                                    <Col>
                                        <Form.Control
                                            type="number"
                                            name="toPrice"
                                            placeholder="Tối đa"
                                            value={filters.toPrice}
                                            onChange={handleFilterChange}
                                        />
                                    </Col>
                                </Row>
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Sắp xếp</Form.Label>
                                <Form.Select
                                    name="sortBy"
                                    value={filters.sortBy}
                                    onChange={handleFilterChange}
                                >
                                    <option value="">Mặc định</option>
                                    <option value="name">Theo tên</option>
                                    <option value="price">Theo giá</option>
                                </Form.Select>
                                <Form.Select
                                    className="mt-2"
                                    name="orderBy"
                                    value={filters.orderBy}
                                    onChange={handleFilterChange}
                                >
                                    <option value="">Mặc định</option>
                                    <option value="asc">Tăng dần</option>
                                    <option value="desc">Giảm dần</option>
                                </Form.Select>
                            </Form.Group>
                            <Button variant="success" onClick={handleClickFilter}>Lọc</Button>
                        </Form>
                    </Card>
                </Col>

                {/* Products Grid */}
                <Col md={9}>
                    {products.length === 0 && !loading && (
                        <Alert variant="success" className="text-center">
                            Không có sản phẩm!
                        </Alert>
                    )}
                    <Row>
                        {products.map((product) => (
                            <Col key={product.id} xs={12} sm={12} md={6} lg={4} className="mb-4">
                                <ProductCard product={product} />
                            </Col>
                        ))}
                    </Row>
                    {page > 0 && products.length > 0 && (
                        <div className="text-center mt-4">
                            <Button

                                onClick={loadMore}
                                disabled={loading}
                                className="rounded-pill px-4"
                                style={{ backgroundColor: Xanh4 }}
                            >
                                {loading ? <Spinner animation="border" size="sm" /> : "Load More"}
                            </Button>
                        </div>
                    )}
                    {loading && <MySpinner />}
                </Col>
            </Row>
        </Container>
    );
};

export default SearchHome;