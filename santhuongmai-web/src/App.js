import logo from './logo.svg';
import './App.css';
import { Button, Container } from 'react-bootstrap';
import { useState } from 'react';
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

function App() {
  const [counter, setCouter] = useState(null)
  const authentication = useSelector(state => state.authentication)
  const dispatch = useDispatch()
  const handle = () => {
    setCouter("12")
    dispatch(loginAction({ name: "xuanduc" }))
  }
  return (
    <BrowserRouter>
      <Header />

      <Container className='min-vh-100'>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
          <Route path="/product/:id" element={<ProductDetail />} />
          <Route path="/search" element={<SearchHome />} />
        </Routes>
      </Container>

      <Footer />
    </BrowserRouter>
  );
}

export default App;
