import React, { useEffect, useState } from 'react';
import { Container, Nav, Row, Col } from 'react-bootstrap';

import CreateShop from './CreateShop';
import CreateProduct from './CreateProduct';
import ManageShop from './ManageShop';
import ManageProduct from './ManageProduct';
import { toast } from 'react-toastify';
import Apis, { endpoints } from '../../../configs/Apis';
import UserInfo from '../UserInfo';
import { useSelector } from 'react-redux';
import RevenueStatistics from './RevenueStatistics';


const SellerSettingHome = () => {
    const [activeTab, setActiveTab] = useState('userInfo');

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

    return (
        <>
            <h2>Cài đặt Seller</h2>
            <span >Trạng thái </span><span style={{ color: seller.status === 'PENDING' ? 'orange' : seller.status === 'APPROVED' ? 'green' : seller.status === 'REJECT' ? 'red' : 'black' }}>{seller.status}
            </span>
            <Nav variant="tabs" activeKey={activeTab} onSelect={(key) => setActiveTab(key)} className="mb-3 mt-2">
                <Nav.Item>
                    <Nav.Link eventKey="userInfo">Thông tin cá nhân</Nav.Link>
                </Nav.Item>
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
                <Nav.Item>
                    <Nav.Link eventKey="statistics">Thống kê Doanh thu</Nav.Link>
                </Nav.Item>
            </Nav>
            <Row>
                <Col>
                    {activeTab === 'userInfo' && <UserInfo />}
                    {activeTab === 'manageShop' && <ManageShop />}
                    {activeTab === 'manageProduct' && <ManageProduct />}
                    {activeTab === 'shop' && <CreateShop />}
                    {activeTab === 'product' && <CreateProduct />}
                    {activeTab === 'statistics' && <RevenueStatistics />}
                </Col>
            </Row>
        </>
    )
}
export default SellerSettingHome