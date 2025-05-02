import { useRef, useState } from "react";
import { Alert, Button, Card, Col, Container, Form, Row } from "react-bootstrap";
import Apis, { endpoints } from "../configs/Apis";
import MySpinner from "./layouts/MySpinner";
import { useNavigate } from "react-router-dom";

const Register = () => {
  const info = [
    { title: "Họ và tên", field: "nickname", type: "text" },
    { title: "Email", field: "email", type: "email" },
    { title: "Tên đăng nhập", field: "username", type: "text" },
    { title: "Mật khẩu", field: "password", type: "password" },
    { title: "Xác nhận mật khẩu", field: "confirm", type: "password" },
  ];
  const [user, setUser] = useState({ userRole: "Customer" });
  const avatar = useRef();
  const [msg, setMsg] = useState();
  const [loading, setLoading] = useState(false);
  const nav = useNavigate();

  const setState = (value, field) => {
    setUser({ ...user, [field]: value });
  };

  const register = async (e) => {
    e.preventDefault();
    if (user.confirm !== user.password) {
      setMsg("Mật khẩu không khớp");
    } else {
      let form = new FormData();
      for (let key in user) {
        if (key !== "confirm") form.append(key, user[key]);
      }
      form.append("avatar", avatar.current.files[0]);
      try {
        setLoading(true);
        await Apis.post(endpoints["register"], form, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });
        nav("/login");
      } catch (error) {
        if (error.response.data.error) {
          setMsg(error.response.data.error);
        } else {
          setMsg("Đăng ký thất bại: " + error);
        }
      } finally {
        setLoading(false);
      }
    }
  };

  return (
    <Container className="d-flex align-items-center justify-content-center" style={{ minHeight: "100vh" }}>
      <Row className="w-100">
        <Col md={{ span: 6, offset: 3 }}>
          <Card className="shadow-lg p-4 rounded-4" style={{ border: "none" }}>
            <Card.Body>
              <h2 className="text-center text-success mb-4">Đăng Ký</h2>

              {msg && <Alert variant="danger">{msg}</Alert>}

              <Form onSubmit={register}>
                {info.map((i) => (
                  <Form.Group className="mb-3" key={i.field}>
                    <Form.Label>{i.title}</Form.Label>
                    <Form.Control
                      type={i.type}
                      value={user[i.field] || ""}
                      onChange={(e) => setState(e.target.value, i.field)}
                      placeholder={i.title}
                      required
                      style={{ fontSize: "1rem" }}
                    />
                  </Form.Group>
                ))}
                <Form.Group className="mb-3">
                  <Form.Label>Vai trò</Form.Label>
                  <Form.Select
                    onChange={(e) => setState(e.target.value, "userRole")}
                    required
                    style={{ fontSize: "1rem" }}
                  >
                    <option value="Customer">Người dùng</option>
                    <option value="Seller">Người bán</option>
                  </Form.Select>
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Ảnh đại diện</Form.Label>
                  <Form.Control
                    type="file"
                    ref={avatar}
                    required
                    style={{ fontSize: "1rem" }}
                  />
                </Form.Group>
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
                    Đăng Ký
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

export default Register;