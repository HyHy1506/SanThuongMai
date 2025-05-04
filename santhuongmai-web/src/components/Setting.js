import React, { useEffect, useState } from 'react';
import { Container, Nav, Row, Col } from 'react-bootstrap';
import { useSelector } from 'react-redux';
import { Navigate } from 'react-router-dom';
import CreateShop from './CreateShop';
import CreateProduct from './CreateProduct';
import ManageShop from './ManageShop';
import ManageProduct from './ManageProduct';
import { toast } from 'react-toastify';
import Apis, { endpoints } from '../configs/Apis';

const Setting = () => {
    const [activeTab, setActiveTab] = useState('manageShop');
    const userInfo = useSelector((state) => state.authentication);
    const [seller, setSeller] = useState({})
    const user = useSelector(state => state.authentication)
    const loadSeller = async () => {
        try {
            const res = await Apis.get(endpoints['seller-with-id'](user.id))
            setSeller(res.data)
        } catch (error) {
            toast.error(error)
        }
    }
    useEffect(() => {
        loadSeller()
    }, [])
    // Chuyển hướng nếu chưa đăng nhập
    if (!userInfo) {
        return <Navigate to="/login" />;
    }

    return (
        <Container className="py-4">
            <h2>Cài đặt Seller</h2>
            <span >Trạng thái </span><span style={{color: seller.status === 'PENDING' ? 'orange' :seller.status === 'APPROVED' ? 'green' :seller.status === 'REJECT' ? 'red' : 'black'}}>{seller.status}
</span>
                <Nav variant="tabs" activeKey={activeTab} onSelect={(key) => setActiveTab(key)} className="mb-3 mt-2">
                    <Nav.Item>
                        <Nav.Link eventKey="manageShop">Quản lý Shop</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                        <Nav.Link eventKey="manageProduct">Quản lý Sản Phẩm</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                        <Nav.Link eventKey="shop">Tạo Shop</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                        <Nav.Link eventKey="product">Tạo Sản Phẩm</Nav.Link>
                    </Nav.Item>
                </Nav>
                <Row>
                    <Col>
                        {activeTab === 'manageShop' && <ManageShop />}
                        {activeTab === 'manageProduct' && <ManageProduct />}
                        {activeTab === 'shop' && <CreateShop />}
                        {activeTab === 'product' && <CreateProduct />}
                    </Col>
                </Row>
        </Container>
    );
};

export default Setting;