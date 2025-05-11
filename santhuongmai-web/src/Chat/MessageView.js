import { useEffect, useRef, useState } from "react";
import { Button, Col, Form, Image, Row } from "react-bootstrap";
import { toast } from "react-toastify";
import { createImageMessage, createTextMessage, getMessages } from "../utils/ChatFunctions";
import { authApis, endpoints } from "../configs/Apis";
import MySpinner from "../components/layouts/MySpinner";

import axios from "axios";

const MessageView = ({ conversation, currentUser, users, onBack }) => {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [imagePreview, setImagePreview] = useState(null); // State để lưu URL ảnh xem trước
  const messagesEndRef = useRef(null);
  const [otherUser, setOtherUser] = useState([]);
  const [loading, setLoading] = useState(false);
  const image = useRef();

  const loadOtherUser = async () => {
    try {
      const res = await authApis().get(endpoints["user-with-id"](otherUserId));
      setOtherUser(res.data.data);
    } catch (error) {
      toast.error("Lỗi tải thông tin người dùng");
    }
  };

  useEffect(() => {
    loadOtherUser();
  }, []);

  useEffect(() => {
    if (conversation) {
      const unsubscribe = getMessages(conversation.id, setMessages);
      return unsubscribe;
    }
  }, [conversation]);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  // Xử lý khi chọn file ảnh
  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {


      // Tạo URL xem trước
      const previewUrl = URL.createObjectURL(file);
      setImagePreview(previewUrl);
    } else {
      setImagePreview(null);
    }
  };

  // Xóa ảnh xem trước
  const handleRemoveImage = () => {
    image.current.value = "";
    setImagePreview(null);
  };

  const handleSendMessage = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      // Gửi ảnh
      if (image.current.files.length > 0) {
        const cloudName = process.env.REACT_APP_CLOUDINARY_CLOUD_NAME
        let form = new FormData();
        form.append("file", image.current.files[0]);
        form.append("upload_preset", "santhuongmai-reactjs");
        form.append("cloud_name", cloudName);

        const response = await axios.post(
          `https://api.cloudinary.com/v1_1/${cloudName}/image/upload`,
          form
        );
        if (response.data?.url) {
          console.log(response.data.url);
          const result = await createImageMessage(
            conversation.id,
            currentUser.id,
            response.data.url
          );
          if (result.success) {
            // Xóa file và ảnh xem trước
            image.current.value = "";
            setImagePreview(null);
          } else {
            toast.error("Lỗi gửi tin nhắn: " + result.error);
          }
        }
      }

      // Gửi tin nhắn văn bản
      if (newMessage.trim()) {
        const result = await createTextMessage(conversation.id, currentUser.id, newMessage);
        if (result.success) {
          setNewMessage("");
        } else {
          toast.error("Lỗi gửi tin nhắn: " + result.error);
        }
      }
    } catch (error) {
      toast.error("Lỗi gửi tin nhắn");
    } finally {
      setLoading(false);
    }
  };

  if (!conversation) return null;

  const otherUserId =
    conversation.participants.user_1 === currentUser.id
      ? conversation.participants.user_2
      : conversation.participants.user_1;

  return (
    <div className="d-flex flex-column h-100">
      <div className="p-3 border-bottom d-flex align-items-center">
        <Button variant="link" onClick={onBack} className="me-2 p-0">
          <i className="fas fa-arrow-left" />
        </Button>
        <Image
          src={otherUser.avatar}
          roundedCircle
          style={{ width: "40px", height: "40px", objectFit: "cover" }}
          onError={(e) => (e.target.src = "https://picsum.photos/200/300")}
        />
        <h5 className="ms-2 mb-0">{otherUser.nickname}</h5>
      </div>
      <div className="flex-grow-1 p-3 overflow-auto">
        {messages.map((msg) => (
          <div
            key={msg.id}
            className={`message-bubble ${msg.sender_id === currentUser.id ? "sent" : "received"}`}
          >
            {msg.type === "text" ? (
              <p className="mb-0">{msg.text}</p>
            ) : msg.type === "image" ? (
              <Image width="100%" src={msg.image} alt="Lỗi ảnh" />
            ) : (
              <p className="mb-0">Shared a post</p>
            )}
            <small className="text-muted" style={{ fontSize: "0.7rem" }}>
              {new Date(msg.created_at).toLocaleTimeString()}
            </small>
          </div>
        ))}
        <div ref={messagesEndRef} />
      </div>
      <Form onSubmit={handleSendMessage} className="p-3 border-top">
        {/* Input chọn file ảnh */}
        <Form.Control
          type="file"
          ref={image}
          accept="image/jpeg,image/jpg,image/png" // Giới hạn định dạng
          onChange={handleImageChange}
          style={{ fontSize: "1rem" }}
        />
        {/* Hiển thị ảnh xem trước */}
        {imagePreview && (
          <div className="mt-2 d-flex align-items-center">
            <Image
              src={imagePreview}
              alt="Preview"
              style={{ width: "100px", height: "100px", objectFit: "cover", borderRadius: "10px", borderWidth: "2px", borderColor: "green", borderStyle: "solid" }}
            />
            <Button
              variant="danger"
              size="sm"
              className="ms-2"
              onClick={handleRemoveImage}
            >
              Xóa
            </Button>
          </div>
        )}
        <Row className="align-items-center mt-2">
          <Col>
            <Form.Control
              type="text"
              value={newMessage}
              onChange={(e) => setNewMessage(e.target.value)}
              placeholder="Type a message..."
              className="message-input"
            />
          </Col>
          <Col xs="auto">
            {loading ? <MySpinner /> :
              <Button type="submit" className="send-button" disabled={loading}>
                <i className="fas fa-paper-plane"></i>
              </Button>
            }

          </Col>
        </Row>
      </Form>
    </div>
  );
};

export default MessageView;