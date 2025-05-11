import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Container, Row, Col, Card, Button, Form, Alert, Spinner } from 'react-bootstrap';
import Apis, { endpoints } from '../../configs/Apis';
import ProductCard from '../Product/ProductCard';
import { toast } from 'react-toastify';
import { useDispatch, useSelector } from 'react-redux';
import { createConversation, createConversationId, createTextMessage, getStaticConversation } from '../../utils/ChatFunctions';
import { selectedConversationAction, showChatAction } from '../../actions/chatAction';
import MySpinner from '../layouts/MySpinner';

const ShopDetail = () => {
  const { id } = useParams();
  const [shop, setShop] = useState(null);
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [priceRange, setPriceRange] = useState({ fromPrice: '', toPrice: '' });
  const [sortOrder, setSortOrder] = useState('desc');
  const [sortBy, setSortBy] = useState('price');
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [loadingSendMsg, setloadingSendMsg] = useState(false);
  const dispatch = useDispatch()
  const user = useSelector((state) => state.authentication);

  // Fetch shop details
  const fetchShop = async () => {
    try {
      const response = await Apis.get(endpoints['shop-with-id'](id));
      setShop(response.data.shop);
    } catch (error) {
      toast.error('Lỗi khi tải thông tin cửa hàng');
    }
  };

  // Fetch categories
  const fetchCategories = async () => {
    try {
      const response = await Apis.get(endpoints.categories);
      setCategories(response.data);
    } catch (error) {
      toast.error('Lỗi khi tải danh mục');
    }
  };

  // Fetch products with filters
  const fetchProducts = async () => {
    setLoading(true);
    try {
      const params = new URLSearchParams();
      params.append('shopId', id);
      params.append('page', page);
      if (selectedCategory) params.append('categoryId', selectedCategory);
      if (priceRange.fromPrice) params.append('fromPrice', priceRange.fromPrice);
      if (priceRange.toPrice) params.append('toPrice', priceRange.toPrice);
      params.append('orderBy', sortOrder);
      params.append('sortBy', sortBy);

      const response = await Apis.get(`${endpoints.products}?${params}`);
      setProducts((prev) => (page === 1 ? response.data : [...prev, ...response.data]));
    } catch (error) {
      toast.error('Lỗi khi tải sản phẩm');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchShop();
    fetchCategories();
  }, [id]);

  useEffect(() => {
    fetchProducts();
  }, [id, page, selectedCategory, priceRange, sortOrder, sortBy]);

  const handleCategorySelect = (categoryId) => {
    setSelectedCategory(categoryId);
    setPage(1);
  };

  const handlePriceFilter = (e) => {
    e.preventDefault();
    setPage(1);
  };

  const handleSortChange = (e) => {
    const [newSortBy, newSortOrder] = e.target.value.split('-');
    setSortBy(newSortBy);
    setSortOrder(newSortOrder);
    setPage(1);
  };
  const handleClickChat = async () => {
    setloadingSendMsg(true)
    try {
      // Gửi tin nhắn văn bản
      if (user != null && shop.sellerId != null) {
        const conversationId = createConversationId(user.id, shop.sellerId)
        const newMessage = "Xin chào"
        const result = await createConversation(user.id, shop.sellerId)
        if (result.success ||result.exist!=null ) {
          const resultCreate = await createTextMessage(conversationId, user.id, newMessage);
          const resultConservation = await getStaticConversation(conversationId)
          if (resultConservation) {
            dispatch(showChatAction())
            // dispatch(selectedConversationAction(resultConservation))

          } else {
            toast.error("Lỗi gửi tin nhắn");
          }
        } else   {
          toast.error("Lỗi gửi tin nhắn");
        }

      }
    } catch (error) {
      toast.error("Lỗi gửi tin nhắn");
    } finally {
      setloadingSendMsg(false)

    }
  }
  const loadMore = () => {
    if (!loading) setPage(page + 1);
  };

  return (
    <Container fluid className="py-4" style={{ backgroundColor: '#f5f7f5' }}>
      {/* Shop Information */}
      {shop && (
        <Card className="mb-4 shadow-sm border-0" style={{ backgroundColor: '#e8f5e9' }}>
          <Card.Body>
            <Row className="align-items-center">
              <Col md={2} className="text-center">
                <img
                  src={shop.avatar}
                  alt="Shop Logo"
                  style={{ width: '100px', height: '100px', borderRadius: '50%' }}
                />
              </Col>
              <Col md={8}>
                <h2 style={{ color: '#2e7d32' }}>{shop.name}</h2>
                <p className="mb-1">
                  <strong>Chủ sở hữu:</strong> {shop.sellerNickname}
                </p>
                <p className="mb-1">
                  <strong>Trạng thái:</strong>{' '}
                  <span style={{ color: shop.isActive ? '#2e7d32' : '#d32f2f' }}>
                    {shop.isActive ? 'Hoạt động' : 'Không hoạt động'}
                  </span>
                </p>
                <p className="mb-0">
                  <strong>Địa chỉ:</strong> 123 Đường Công Nghệ, Quận 1, TP. HCM
                </p>
              </Col>
              {user == null || (user != null && user.id == shop.sellerId) ? <></> :

                <Col md={2} >
                  {loadingSendMsg ? <MySpinner /> :

                    <Button style={{ backgroundColor: "darkgreen" }} onClick={handleClickChat}>Chat Ngay</Button>
                  }
                </Col>
              }
            </Row>
          </Card.Body>
        </Card>
      )}

      {/* Filters Section */}
      <Row className="mb-4 align-items-center">
        <Col md={6}>
          <div className="d-flex overflow-auto">
            <Button
              variant={selectedCategory === null ? 'success' : 'outline-success'}
              className="me-2 rounded-pill"
              onClick={() => handleCategorySelect(null)}
              style={{ backgroundColor: selectedCategory === null ? '#2e7d32' : '', borderColor: '#2e7d32' }}
            >
              Tất cả
            </Button>
            {categories.map((category) => (
              <Button
                key={category.id}
                variant={selectedCategory === category.id ? 'success' : 'outline-success'}
                className="me-2 rounded-pill"
                onClick={() => handleCategorySelect(category.id)}
                style={{
                  backgroundColor: selectedCategory === category.id ? '#2e7d32' : '',
                  borderColor: '#2e7d32',
                }}
              >
                {category.name}
              </Button>
            ))}
          </div>
        </Col>
        <Col md={6}>
          <Form onSubmit={handlePriceFilter} className="d-flex align-items-center justify-content-end">
            <Form.Group className="me-2">
              <Form.Control
                type="number"
                placeholder="Từ giá"
                value={priceRange.fromPrice}
                onChange={(e) => setPriceRange({ ...priceRange, fromPrice: e.target.value })}
                style={{ width: '120px' }}
              />
            </Form.Group>
            <Form.Group className="me-2">
              <Form.Control
                type="number"
                placeholder="Đến giá"
                value={priceRange.toPrice}
                onChange={(e) => setPriceRange({ ...priceRange, toPrice: e.target.value })}
                style={{ width: '120px' }}
              />
            </Form.Group>
            <Form.Group className="me-2">
              <Form.Select onChange={handleSortChange} style={{ width: '150px' }}>
                <option value="price-desc">Giá: Cao đến thấp</option>
                <option value="price-asc">Giá: Thấp đến cao</option>
                <option value="name-asc">Tên: A-Z</option>
                <option value="name-desc">Tên: Z-A</option>
              </Form.Select>
            </Form.Group>
            <Button variant="success" type="submit" style={{ backgroundColor: '#2e7d32', borderColor: '#2e7d32' }}>
              Lọc
            </Button>
          </Form>
        </Col>
      </Row>

      {/* Products Section */}
      {products.length === 0 && !loading ? (
        <Alert variant="info" className="text-center">
          Không tìm thấy sản phẩm nào!
        </Alert>
      ) : (
        <Row>
          {products.map((product) => (
            <Col key={product.id} xs={12} sm={6} md={4} lg={3} className="mb-4">
              <ProductCard product={product} />
            </Col>
          ))}
        </Row>
      )}

      {/* Load More Button */}
      {products.length > 0 && (
        <div className="text-center mt-4">
          <Button
            variant="success"
            onClick={loadMore}
            disabled={loading}
            className="rounded-pill px-4"
            style={{ backgroundColor: '#2e7d32', borderColor: '#2e7d32' }}
          >
            {loading ? <Spinner animation="border" size="sm" /> : 'Xem thêm'}
          </Button>
        </div>
      )}
    </Container>
  );
};

export default ShopDetail;