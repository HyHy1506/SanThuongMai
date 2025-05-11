import React, { useState, useEffect } from "react";
import { Alert, Button, Form, ListGroup, Card } from "react-bootstrap";
import Apis, { authApis, endpoints } from "../../configs/Apis";
import { useSelector } from "react-redux";
import { toast } from "react-toastify";
import MySpinner from "../layouts/MySpinner";

const CommentSection = ({ productId }) => {
  const [comments, setComments] = useState([]);
  const [commentText, setCommentText] = useState("");
  const [replyText, setReplyText] = useState("");
  const [loading, setLoading] = useState(false);
  const user = useSelector((state) => state.authentication);

  useEffect(() => {
    const fetchComments = async () => {
      setLoading(true);
      try {
        const response = await Apis.get(`${endpoints.comments}?productId=${productId}`);
        setComments(response.data);
      } catch (err) {
        toast.error("Lỗi khi tải bình luận");
      } finally {
        setLoading(false);
      }
    };
    fetchComments();
  }, [productId]);

  const handleCommentSubmit = async (e) => {
    e.preventDefault();
    if (!user) {
      toast.error("Vui lòng đăng nhập để bình luận!");
      return;
    }
    setLoading(true);
    try {
      let params = {
        productId: productId,
        content: commentText
      };
      const response = await authApis().post(endpoints.comments, params);
      setComments([response.data.comment, ...comments]);
      setCommentText("");
      toast.success("Bình luận đã được gửi!");
    } catch (err) {
      toast.error("Lỗi khi gửi bình luận: " + err.response?.data?.error);
    } finally {
      setLoading(false);
    }
  };

  const handleReplySubmit = async (commentId) => {
    if (!user) {
      toast.error("Vui lòng đăng nhập để trả lời!");
      return;
    }
    setLoading(true);
    try {
      const response = await authApis().post(endpoints.replies(commentId), {
        text: replyText,
      });
      setComments(
        comments.map((c) =>
          c.id === commentId
            ? { ...c, replies: [...(c.replies || []), response.data] }
            : c
        )
      );
      setReplyText("");
      toast.success("Trả lời đã được gửi!");
    } catch (err) {
      toast.error("Lỗi khi gửi trả lời");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <style>
        {`
          @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap');
          
          .comment-section-custom {
            font-family: 'Poppins', sans-serif;
            color: #537D5D;
          }
          
          .comment-title {
            font-size: 1.5rem;
            font-weight: 600;
            color: #537D5D;
            margin-bottom: 1rem;
          }
          
          .comment-card-custom {
            background-color: #FFFFFF;
            border: 1px solid #9EBC8A;
            border-radius: 10px;
            margin-bottom: 1rem;
            transition: all 0.3s ease;
          }
          
          .comment-card-custom:hover {
            box-shadow: 0 4px 8px rgba(83, 125, 93, 0.2);
          }
          
          .comment-avatar-custom {
            border: 2px solid #9EBC8A;
            transition: all 0.3s ease;
          }
          
          .comment-avatar-custom:hover {
            border-color: #73946B;
            transform: scale(1.05);
          }
          
          .comment-input-custom {
            background-color: #F9F9F9;
            border: 1px solid #9EBC8A;
            border-radius: 10px;
            transition: all 0.3s ease;
          }
          
          .comment-input-custom:focus {
            border-color: #73946B;
            box-shadow: 0 0 5px rgba(115, 148, 107, 0.5);
          }
          
          .btn-comment-custom {
            background-color: #73946B;
            border: none;
            color: #FFFFFF;
            padding: 0.5rem 1rem;
            border-radius: 20px;
            transition: all 0.3s ease;
          }
          
          .btn-comment-custom:hover {
            background-color: #9EBC8A;
            transform: scale(1.05);
          }
          
          .btn-comment-custom:disabled {
            background-color: #9EBC8A;
            opacity: 0.6;
          }
        `}
      </style>
      <div className="comment-section-custom">
        {loading && <MySpinner />}
        <h3 className="comment-title">Bình luận</h3>
        <ListGroup variant="flush">
          {comments.length === 0 && (
            <Alert variant="success">Chưa có bình luận nào!</Alert>
          )}
          {comments.map((comment) => (
            <ListGroup.Item key={comment.id} className="border-0">
              <Card className="comment-card-custom">
                <Card.Body>
                  <div className="d-flex align-items-start">
                    <Card.Img
                      src={comment.customerAvatar}
                      style={{
                        height: "40px",
                        width: "40px",
                        objectFit: "cover",
                        borderRadius: "50%",
                        marginRight: "12px"
                      }}
                      className="comment-avatar-custom"
                    />
                    <div>
                      <div className="d-flex align-items-center mb-1">
                        <strong className="me-2">{comment.customerNickname}</strong>
                        <small className="text-muted">{new Date(comment.createAt).toLocaleDateString()}</small>
                      </div>
                      <p className="mb-0">{comment.content}</p>
                    </div>
                  </div>
                </Card.Body>
              </Card>
            </ListGroup.Item>
          ))}
        </ListGroup>
        {user && (
          <Form onSubmit={handleCommentSubmit} className="mt-3">
            <Form.Group className="mb-3">
              <Form.Control
                as="textarea"
                rows={3}
                placeholder="Viết bình luận của bạn..."
                value={commentText}
                onChange={(e) => setCommentText(e.target.value)}
                className="comment-input-custom"
              />
            </Form.Group>
            <Button
              type="submit"
              variant="primary"
              disabled={loading || !commentText}
              className="btn-comment-custom"
            >
              Gửi bình luận
            </Button>
          </Form>
        )}
      </div>
    </>
  );
};

export default CommentSection;