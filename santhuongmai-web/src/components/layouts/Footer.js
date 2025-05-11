import { Container, Alert } from "react-bootstrap";

const Footer = () => {
  return (
    <>
      <style>
        {`
          @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap');
          
          .footer-custom {
            background-color: #537D5D !important;
            color: #FFFFFF !important;
            font-family: 'Poppins', sans-serif;
            padding: 1rem 0;
            transition: all 0.3s ease;
            box-shadow: 0 -2px 4px rgba(0,0,0,0.1);
          }
          
          .footer-text-custom {
            font-size: 1rem;
            transition: color 0.3s ease, transform 0.3s ease;
          }
          
          .footer-text-custom:hover {
            color: #9EBC8A !important;
            transform: scale(1.05);
          }
        `}
      </style>
      <Alert variant="dark" className="footer-custom text-center mt-4 mb-0 py-3">
        <Container>
          <span className="footer-text-custom">Santhuongmai TranXuanDuc &copy; 2025</span>
        </Container>
      </Alert>
    </>
  );
};

export default Footer;