import { Container, Alert } from "react-bootstrap";

const Footer = () => {
  return (
    <Alert
      variant="dark"
      className="text-center mt-4 mb-0 py-3"
      style={{
        backgroundColor: "#343a40",
        color: "#ffffff",
        borderRadius: "0",
        boxShadow: "0 -2px 4px rgba(0,0,0,0.1)",
      }}
    >
      <Container>
        SanThuongMai TranXuanDuc &copy; 2025
      </Container>
    </Alert>
  );
};

export default Footer;