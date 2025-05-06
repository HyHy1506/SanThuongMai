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
function App() {
  const dispatch=useDispatch()
  useEffect(()=>{
    
    let user=JSON.parse(localStorage.getItem("user"))

    if(user!=null && user.id!=null){
      dispatch(loginAction(user))
    }
  },[])
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
        </Routes>
      </Container>

      <Footer />
    </BrowserRouter>
  );
}

export default App;
