import { useContext, useEffect, useState } from "react";
import { Badge, Button, Col, Container, Form, Nav, Navbar, NavDropdown, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";
import { useDispatch, useSelector } from "react-redux";
import { logoutAction } from "../../actions/authentication";
import { FaShoppingCart } from "react-icons/fa";
import cookie from "react-cookies";
const Header = () => {
  const authentication = useSelector(state => state.authentication);
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
    dispatch(logoutAction())
    cookie.remove("token");

  }
  return (
    <Navbar expand="lg" bg="dark" variant="dark" sticky="top" style={{ boxShadow: "0 4px 6px rgba(0,0,0,0.1)" }}>
      <Container>
        <Navbar.Brand as={Link} to="/" style={{ fontWeight: "bold", fontSize: "1.5rem" }}>
          Santhuongmai
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto align-items-center">
            <Nav.Link as={Link} to="/" style={{ fontSize: "1.1rem" }}>
              Trang chủ
            </Nav.Link>

            {user === null ? (
              <>
                <Nav.Link
                  as={Link}
                  to="/login"
                  className="text-warning"
                  style={{ fontSize: "1.1rem" }}
                >
                  Đăng nhập
                </Nav.Link>
                <Nav.Link
                  as={Link}
                  to="/register"
                  className="text-success"
                  style={{ fontSize: "1.1rem" }}
                >
                  Đăng ký
                </Nav.Link>
              </>
            ) : (
              <>
                <Nav.Link as={Link} to="/cart">
                  <FaShoppingCart /> Giỏ hàng{' '}
                  {totalItems > 0 && (
                    <Badge bg="warning" text="dark">
                      {totalItems}
                    </Badge>
                  )}
                </Nav.Link>
                <Nav.Link as={Link} to="/setting" className="d-flex align-items-center">
                  <img
                    src={user.avatar}
                    width="32"
                    height="32"
                    className="rounded-circle me-2"
                    alt="User avatar"
                    style={{ objectFit: "cover" }}
                  />
                  <span className="text-light" style={{ fontSize: "1.1rem" }}>
                    Chào {user.username}!
                  </span>
                </Nav.Link>

                <Button
                  variant="outline-danger"
                  size="sm"
                  onClick={handleLogout}
                  style={{ marginLeft: "10px", fontSize: "1rem" }}
                >
                  Đăng xuất
                </Button>
              </>
            )}
          </Nav>
          <Form onSubmit={search} className="d-flex">
            <Form.Control
              type="search"
              value={kw}
              onChange={(e) => setKw(e.target.value)}
              placeholder="Tìm sản phẩm"
              style={{ maxWidth: "200px", fontSize: "1rem" }}
              className="me-2"
            />
            <Button variant="success" type="submit" style={{ fontSize: "1rem" }}>
              Tìm
            </Button>
          </Form>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;