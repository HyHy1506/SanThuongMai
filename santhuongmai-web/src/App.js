import logo from './logo.svg';
import './App.css';
import { Button, Container } from 'react-bootstrap';
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { loginAction } from './actions/authentication';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Header from './components/layouts/Header';
import Home from './components/Home';
import Register from './components/Register';
import Login from './components/Login';
import Footer from './components/layouts/Footer';
import ProductDetail from './components/Product/ProductDetail';
import SearchHome from './components/Search/SearchHome';
import Cart from './components/Cart';
import { ToastContainer } from 'react-toastify';
import Setting from './components/SettingManager/Setting';
import Checkout from './components/Checkout/Checkout';
import PaymentHistory from './components/SettingManager/CutomerSetting/PaymentHistory';
import ShopDetail from './components/Shop/ShopDetail';
import ChatIcon from './Chat/ChatIcon';
import ChatModal from './Chat/ChatModal';
function App() {
  const dispatch = useDispatch()
  const [showChat, setShowChat] = useState(false);
  const user = useSelector((state) => state.authentication);
  useEffect(() => {

    let user = JSON.parse(localStorage.getItem("user"))
    let token = localStorage.getItem('token')
    if (user != null && user.id != null && token != null) {
      dispatch(loginAction(user))
    }
  }, [])
  // const handleToggleChat = () => {

  //   setShowChat(!showChat);
  // };

  return (
    <BrowserRouter>
      <ToastContainer position="top-right" autoClose={3000} />
      <Header />

      <Container className='min-vh-100'>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
          <Route path="/products/:id" element={<ProductDetail />} />
          <Route path="/search" element={<SearchHome />} />
          <Route path="/cart" element={<Cart />} />
          <Route path="/checkout" element={<Checkout />} />
          <Route path="/setting" element={<Setting />} />
          <Route path="/payment-history" element={<PaymentHistory />} />
          <Route path="/shops/:id" element={<ShopDetail />} />
        </Routes>
      </Container>
      {user != null && <>
        <ChatIcon />
        <ChatModal

          user={user}

        />
      </>}

      <Footer />
    </BrowserRouter>
  );
}

export default App;
