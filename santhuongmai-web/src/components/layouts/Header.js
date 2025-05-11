import { useEffect, useState } from "react";
import { Badge, Button, Col, Container, Form, Nav, Navbar } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";
import { useDispatch, useSelector } from "react-redux";
import { logoutAction } from "../../actions/authentication";
import { FaShoppingCart } from "react-icons/fa";
import { reloadCart } from "../../actions/cartActions";

const Header = () => {
  const authentication = useSelector((state) => state.authentication);
  const dispatch = useDispatch();
  const user = authentication;
  const [kw, setKw] = useState();
  const nav = useNavigate();
  const cartItems = useSelector((state) => state.cart.items);
  const totalItems = cartItems.reduce((total, item) => total + item.quantity, 0);

  const search = (e) => {
    e.preventDefault();
    nav(`/search?kw=${kw}`);
  };

  const handleLogout = () => {
    dispatch(logoutAction());
    dispatch(reloadCart())
  };

  return (
    <>
      <style>
        {`
          @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap');
          
          .navbar-custom {
            background-color: #537D5D !important;
            padding: 1rem 0;
            font-family: 'Poppins', sans-serif;
            transition: all 0.3s ease;
          }
          
          .navbar-brand-custom {
            font-weight: 600;
            font-size: 1.8rem !important;
            color: #FFFFFF !important;
            transition: color 0.3s ease;
          }
          
          .navbar-brand-custom:hover {
            color: #9EBC8A !important;
          }
          
          .nav-link-custom {
            color: #FFFFFF !important;
            font-size: 1.1rem;
            margin: 0 0.5rem;
            transition: all 0.3s ease;
            position: relative;
          }
          
          .nav-link-custom:hover {
            color: #9EBC8A !important;
            transform: translateY(-2px);
          }
          
          .nav-link-custom::after {
            content: '';
            position: absolute;
            width: 0;
            height: 2px;
            bottom: -2px;
            left: 0;
            background-color: #9EBC8A;
            transition: width 0.3s ease;
          }
          
          .nav-link-custom:hover::after {
            width: 100%;
          }
          
          .search-form-custom {
            max-width: 250px;
            transition: all 0.3s ease;
          }
          
          .search-input-custom {
            border: 1px solid #73946B !important;
            background-color: #9EBC8A !important;
            color: #537D5D !important;
            font-size: 1rem;
            transition: all 0.3s ease;
          }
          
          .search-input-custom:focus {
            box-shadow: 0 0 5px rgba(115, 148, 107, 0.5) !important;
            border-color: #FFFFFF !important;
          }
          
          .btn-search-custom {
            background-color: #73946B !important;
            border: none !important;
            color: #FFFFFF !important;
            font-weight: 500;
            transition: all 0.3s ease;
          }
          
          .btn-search-custom:hover {
            background-color: #9EBC8A !important;
            color: #537D5D !important;
            transform: scale(1.05);
          }
          
          .btn-logout-custom {
            border-color: #FFFFFF !important;
            color: #FFFFFF !important;
            font-weight: 500;
            transition: all 0.3s ease;
          }
          
          .btn-logout-custom:hover {
            background-color: #FFFFFF !important;
            color: #537D5D !important;
            transform: scale(1.05);
          }
          
          .cart-badge-custom {
            background-color: #FFFFFF !important;
            color: #537D5D !important;
            font-weight: 500;
          }
          
          .avatar-custom {
            border: 2px solid #FFFFFF;
            transition: all 0.3s ease;
          }
          
          .avatar-custom:hover {
            border-color: #9EBC8A;
            transform: scale(1.1);
          }
        `}
      </style>
      <Navbar expand="lg" className="navbar-custom" sticky="top">
        <Container>
          <Navbar.Brand as={Link} to="/" className="navbar-brand-custom">
            Santhuongmai
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto align-items-center">
              <Nav.Link as={Link} to="/" className="nav-link-custom">
                Trang chủ
              </Nav.Link>
              {user === null ? (
                <>
                  <Nav.Link
                    as={Link}
                    to="/login"
                    className="nav-link-custom text-warning"
                  >
                    Đăng nhập
                  </Nav.Link>
                  <Nav.Link
                    as={Link}
                    to="/register"
                    className="nav-link-custom text-success"
                  >
                    Đăng ký
                  </Nav.Link>
                </>
              ) : (
                <>
                  <Nav.Link as={Link} to="/cart" className="nav-link-custom">
                    <FaShoppingCart /> Giỏ hàng{' '}
                    {totalItems > 0 && (
                      <Badge className="cart-badge-custom">
                        {totalItems}
                      </Badge>
                    )}
                  </Nav.Link>
                  <Nav.Link as={Link} to="/setting" className="d-flex align-items-center nav-link-custom">
                    <img
                      src={user.avatar}
                      width="32"
                      height="32"
                      className="rounded-circle me-2 avatar-custom"
                      alt="User avatar"
                      style={{ objectFit: "cover" }}
                    />
                    <span>{user.nickname}</span>
                  </Nav.Link>
                  <Button
                    variant="outline"
                    className="btn-logout-custom"
                    size="sm"
                    onClick={handleLogout}
                  >
                    Đăng xuất
                  </Button>
                </>
              )}
            </Nav>
            <Form onSubmit={search} className="d-flex search-form-custom">
              <Form.Control
                type="search"
                value={kw}
                onChange={(e) => setKw(e.target.value)}
                placeholder="Tìm sản phẩm"
                className="search-input-custom me-2"
              />
              <Button variant="success" type="submit" className="btn-search-custom">
                Tìm
              </Button>
            </Form>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </>
  );
};

export default Header;