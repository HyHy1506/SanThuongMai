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
      await authApis().post(endpoints.ratings, { productId, "rating": rating.toString() });
      toast.success("Đánh giá của bạn đã được gửi!");
      setRating(0);
    } catch (err) {
      const message = err.response?.data?.error || "Lỗi khi gửi đánh giá";
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <style>
        {`
          @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap');
          
          .star-rating-custom {
            font-family: 'Poppins', sans-serif;
            color:rgb(204, 204, 204);
          }
          
          .star-rating-title {
            font-size: 1.5rem;
            font-weight: 600;
            color: #537D5D;
            margin-bottom: 1rem;
          }
          
          .star-custom {
            cursor: pointer;
            transition: color 0.3s ease, transform 0.3s ease;
            margin: 0 2px;
          }
          
          .star-custom:hover,
          .star-custom.active {
            color: #9EBC8A;
            transform: scale(1.2);
          }
          
          .btn-rating-custom {
            background-color: #73946B;
            border: none;
            color: #FFFFFF;
            padding: 0.5rem 1rem;
            border-radius: 20px;
            transition: all 0.3s ease;
          }
          
          .btn-rating-custom:hover {
            background-color: #9EBC8A;
            transform: scale(1.05);
          }
          
          .btn-rating-custom:disabled {
            background-color: #9EBC8A;
            opacity: 0.6;
          }
        `}
      </style>
      <div className="star-rating-custom">
        <h3 className="star-rating-title">Đánh giá sản phẩm</h3>
        <div className="d-flex align-items-center mb-3">
          {[...Array(5)].map((_, index) => {
            const ratingValue = index + 1;
            return (
              <FaStar
                key={index}
                size={30}
                className={ratingValue <= (hover || rating) ? "star-custom active" : "star-custom"}
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
          className="btn-rating-custom"
        >
          {loading ? "Đang gửi..." : "Gửi đánh giá"}
        </Button>
      </div>
    </>
  );
};

export default StarRating;