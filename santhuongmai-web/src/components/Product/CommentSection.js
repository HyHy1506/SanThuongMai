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

  // Fetch comments
  useEffect(() => {
    const fetchComments = async () => {
      setLoading(true);
      try {

        const response = await Apis.get(`${endpoints.comments}?productId=${productId}`);
        setComments(response.data);
        console.log(response.data)
      } catch (err) {
        toast.error("Lỗi khi tải bình luận");
      } finally {
        setLoading(false);
      }
    };
    fetchComments();
  }, [productId]);

  // Handle comment submission
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
      }
      const response = await authApis().post(endpoints.comments, params);
      setComments([response.data.comment,...comments ]);
      
      setCommentText("");
      toast.success("Bình luận đã được gửi!");
    } catch (err) {
      toast.error("Lỗi khi gửi bình luận: "+err.response.data.error);
    } finally {
      setLoading(false);
    }
  };
useEffect(()=>{
  console.log(comments)
},[comments])
  // Handle reply submission
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
    <div>
      {loading && <MySpinner/>}
      
      <h3 className="fw-bold mb-3">Bình luận</h3>
      <ListGroup variant="flush">
        {comments.length === 0 && (
          <Alert variant="info">Chưa có bình luận nào!</Alert>
        )}
        {comments.map((comment) => (
          <ListGroup.Item key={comment.id} className="border-0">
            <Card className="mb-3">
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
            />
          </Form.Group>
          <Button
            type="submit"
            variant="primary"
            disabled={loading || !commentText}
          >
            Gửi bình luận
          </Button>
        </Form>
      )}
    </div>
  );
};

export default CommentSection;