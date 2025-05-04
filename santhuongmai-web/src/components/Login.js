import { useState } from "react";
import { Alert, Button, Card, Col, Container, Form, Row } from "react-bootstrap";
import MySpinner from "./layouts/MySpinner";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import Apis, { authApis, endpoints } from "../configs/Apis";
import cookie from "react-cookies";
import { loginAction } from "../actions/authentication";

const Login = () => {
  const userAuthentication = useSelector((state) => state.authentication);
  const dispatch = useDispatch();
  const [user, setUser] = useState({});
  const [msg, setMsg] = useState();
  const [loading, setLoading] = useState(false);
  const nav = useNavigate();

  const info = [
    { title: "Tên đăng nhập", field: "username", type: "text" },
    { title: "Mật khẩu", field: "password", type: "password" },
  ];

  const setState = (value, field) => {
    setUser({ ...user, [field]: value });
  };

  const login = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const res = await Apis.post(endpoints.login, user);
      if (res.data.status === "success") {
        cookie.save("token", res.data.token);
        try {
          const resUser = await authApis().get(endpoints["current-user"]);
          dispatch(loginAction(resUser.data));

          nav("/");
        } catch (error) {
          console.error("Lỗi lấy thông tin user hiện tại", error);
        }
      }
    } catch (error) {
      if (error.response.data.error) {
        setMsg("Đăng nhập thất bại: " + error.response.data.error);
      } else {
        setMsg("Đăng nhập thất bại: " + error);
      }
    }
    setTimeout(() => {
      setLoading(false);
    }, 5000);
  };

  return (
    <Container className="d-flex align-items-center justify-content-center" style={{ minHeight: "100vh" }}>
      <Row className="w-100">
        <Col md={{ span: 6, offset: 3 }}>
          <Card className="shadow-lg p-4 rounded-4" style={{ border: "none" }}>
            <Card.Body>
              <h2 className="text-center text-success mb-4">Đăng Nhập</h2>

              {msg && <Alert variant="danger">{msg}</Alert>}

              <Form onSubmit={login}>
                {info.map((i) => (
                  <Form.Group className="mb-3" key={i.field}>
                    <Form.Label>{i.title}</Form.Label>
                    <Form.Control
                      type={i.type}
                      placeholder={i.title}
                      value={user[i.field] || ""}
                      onChange={(e) => setState(e.target.value, i.field)}
                      required
                      style={{ fontSize: "1rem" }}
                    />
                  </Form.Group>
                ))}
                {loading ? (
                  <div className="text-center">
                    <MySpinner />
                  </div>
                ) : (
                  <Button
                    type="submit"
                    variant="success"
                    className="w-100"
                    style={{ fontSize: "1.1rem", padding: "10px" }}
                  >
                    Đăng Nhập
                  </Button>
                )}
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Login;