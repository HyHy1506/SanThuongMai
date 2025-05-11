import React, { useRef, useState } from "react";
import { Alert, Button, Card, Col, Container, Form, Modal, Row, Image, ListGroup } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import Apis, { authApis, endpoints } from "../../configs/Apis";
import { toast } from "react-toastify";
import MySpinner from "../layouts/MySpinner";
import { loginAction } from "../../actions/authentication";

const UserInfo = () => {
    const user = useSelector((state) => state.authentication);
    const didpatch =useDispatch()

    const [editMode, setEditMode] = useState(false);
    const [formData, setFormData] = useState({
        nickname: user?.nickname || "",
        email: user?.email || "",
    });
    const [errors, setErrors] = useState({});
    const [loading, setLoading] = useState(false);
    const avatar = useRef()
    const loadUser = async () => {
        try {
            const res=await authApis().get(endpoints["current-user"])
            didpatch(loginAction(res.data))
        } catch (err) {
            toast.error("Lỗi cập nhập")
        }
    }
    // Validate form data
    const validateForm = () => {
        const newErrors = {};
        if (!formData.nickname.trim()) {
            newErrors.nickname = "Tên hiển thị không được để trống";
        }
        if (!formData.email) {
            newErrors.email = "Email không được để trống";
        } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
            newErrors.email = "Email không hợp lệ";
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    // Handle form input changes
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
        if (errors[name]) {
            setErrors({ ...errors, [name]: "" });
        }
    };

    // Handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validateForm()) return;

        setLoading(true);
        try {
            let form = new FormData();
            if (formData.nickname) form.append("nickname", formData.nickname)
            if (formData.email) form.append("email", formData.email)
            if (avatar.current.files.length > 0) {

                form.append("avatar", avatar.current.files[0]);
            }
            await authApis().put(endpoints['user-with-id'](user.id), form,
                {
                    headers: {
                        "Content-Type": "multipart/form-data",
                    },
                }
            );
            toast.success("Cập nhật thông tin thành công!");
            setEditMode(false);
            loadUser()
        } catch (err) {
            const message = err.response?.data?.error || "Lỗi khi cập nhật thông tin";
            toast.error(message);
        } finally {
            setLoading(false);
        }
    };

    if (!user) {
        return <Alert variant="warning">Vui lòng đăng nhập để xem thông tin!</Alert>;
    }

    return (
        <Container fluid className="py-5" style={{ backgroundColor: "#f8f9fa", minHeight: "100vh" }}>
            <Row className="justify-content-center">
                <Col md={8} lg={6}>
                    <Card className=" border-0" style={{ borderRadius: "15px", }}>
                        <Card.Body className="p-5">
                            <div className="text-center mb-4">
                                <Image
                                    src={user.avatar}
                                    alt="User avatar"
                                    roundedCircle
                                    style={{ width: "150px", height: "150px", objectFit: "cover" }}
                                    onError={(e) => (e.target.src = "https://via.placeholder.com/150")}
                                />
                                <h2 className="mt-3 fw-bold">{user.nickname}</h2>
                                <span className="text-muted">@{user.username} - </span>
                                <span className="text-muted">ID: {user.id}</span>
                            </div>
                            <ListGroup variant="flush">
                                <ListGroup.Item className="py-3">
                                    <strong>Email:</strong> {user.email}
                                </ListGroup.Item>
                                <ListGroup.Item className="py-3">
                                    <strong>Vai trò:</strong> {user.userRole}
                                </ListGroup.Item>
                                <ListGroup.Item className="py-3">
                                    <strong>Ngày tạo:</strong>{" "}
                                    {new Date(user.createAt).toLocaleDateString("vi-VN")}
                                </ListGroup.Item>
                                <ListGroup.Item className="py-3">
                                    <strong>Cập nhật lần cuối:</strong>{" "}
                                    {new Date(user.updateAt).toLocaleDateString("vi-VN")}
                                </ListGroup.Item>
                                <ListGroup.Item className="py-3">
                                    <strong>Trạng thái:</strong>{" "}
                                    <span className={user.isActive ? "text-success" : "text-danger"}>
                                        {user.isActive ? "Hoạt động" : "Không hoạt động"}
                                    </span>
                                </ListGroup.Item>
                            </ListGroup>
                            <div className="text-center mt-4">
                                <Button
                                    variant="primary"
                                    size="lg"
                                    onClick={() => setEditMode(true)}
                                    style={{ borderRadius: "10px", padding: "10px 30px" }}
                                >
                                    Chỉnh sửa thông tin
                                </Button>
                            </div>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            {/* Edit Modal */}
            <Modal show={editMode} onHide={() => setEditMode(false)} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Chỉnh sửa thông tin</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3" controlId="formNickname">
                            <Form.Label>Tên hiển thị</Form.Label>
                            <Form.Control
                                type="text"
                                name="nickname"
                                value={formData.nickname}
                                onChange={handleChange}
                                isInvalid={!!errors.nickname}
                                placeholder="Nhập tên hiển thị"
                            />
                            <Form.Control.Feedback type="invalid">
                                {errors.nickname}
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formEmail">
                            <Form.Label>Email</Form.Label>
                            <Form.Control
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                isInvalid={!!errors.email}
                                placeholder="Nhập email"
                            />
                            <Form.Control.Feedback type="invalid">
                                {errors.email}
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formAvatar">
                            <Form.Label>URL ảnh đại diện</Form.Label>
                            <Form.Control
                                type="file"
                                name="avatar"
                                ref={avatar}
                                placeholder=" ảnh đại diện"
                            />
                            <Form.Control.Feedback type="invalid">
                                {errors.avatar}
                            </Form.Control.Feedback>
                            {formData.avatar && (
                                <div className="mt-2 text-center">
                                    <Image
                                        src={formData.avatar}
                                        alt="Avatar preview"
                                        roundedCircle
                                        style={{ width: "100px", height: "100px", objectFit: "cover" }}
                                        onError={(e) => (e.target.src = "https://via.placeholder.com/100")}
                                    />
                                </div>
                            )}
                        </Form.Group>
                        <div className="text-center">
                            <Button
                                variant="secondary"
                                onClick={() => setEditMode(false)}
                                className="me-2"
                                disabled={loading}
                            >
                                Hủy
                            </Button>
                            <Button variant="primary" type="submit" disabled={loading}>
                                {loading ? <MySpinner size="sm" /> : "Lưu thay đổi"}
                            </Button>
                        </div>
                    </Form>
                </Modal.Body>
            </Modal>
        </Container>
    );
};

export default UserInfo;