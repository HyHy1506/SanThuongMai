import React, { useState, useEffect } from "react";
import { Button, Form, Alert } from "react-bootstrap";
import { FaStar } from "react-icons/fa";
import Apis, { authApis, endpoints } from "../../configs/Apis";
import { useSelector } from "react-redux";
import { toast } from "react-toastify";

const StarRating = ({ productId }) => {
  const [rating, setRating] = useState(0);
  const [hover, setHover] = useState(0);
  const [loading, setLoading] = useState(false);
  const user = useSelector((state) => state.authentication);

  const handleRatingSubmit = async () => {
    if (!user) {
      toast.error("Vui lòng đăng nhập để đánh giá!");
      return;
    }
    setLoading(true);
    try {
      await authApis().post(endpoints.ratings, { productId, "rating":rating.toString() });
      toast.success("Đánh giá của bạn đã được gửi!");
      setRating(0);
    } catch (err) {
      const message = err.response?.data?.error || "Lỗi khi gửi đánh giá" ;
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h3 className="fw-bold mb-3">Đánh giá sản phẩm</h3>
      <div className="d-flex align-items-center mb-3">
        {[...Array(5)].map((_, index) => {
          const ratingValue = index + 1;
          return (
            <FaStar
              key={index}
              size={30}
              color={ratingValue <= (hover || rating) ? "#ffc107" : "#e4e5e9"}
              style={{ cursor: "pointer" }}
              onClick={() => setRating(ratingValue)}
              onMouseEnter={() => setHover(ratingValue)}
              onMouseLeave={() => setHover(0)}
            />
          );
        })}
      </div>
      <Button
        variant="primary"
        onClick={handleRatingSubmit}
        disabled={loading || rating === 0}
      >
        {loading ? "Đang gửi..." : "Gửi đánh giá"}
      </Button>
    </div>
  );
};

export default StarRating;