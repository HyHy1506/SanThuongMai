import React, { useEffect, useState } from 'react';
import { Container, Nav, Row, Col } from 'react-bootstrap';


import UserInfo from '../UserInfo';
import { useSelector } from 'react-redux';
import PaymentHistory from './PaymentHistory';


const CustomerSettingHome = () => {
    const [activeTab, setActiveTab] = useState('userInfo');

    const user = useSelector(state => state.authentication)


    return (
        <>
              <Row>
                           <Col className="mb-6">
                               <h2>Cài đặt  </h2>
                           </Col>
                           <Col className="mb-6">
                               <h2>
                                   <a className="text-success" href="http://moonice.fun:8080/SanThuongMai">
                                       --->  Trang quản trị 
                                   </a>
                               </h2>
                           </Col>
                       </Row>

            <Nav variant="tabs" activeKey={activeTab} onSelect={(key) => setActiveTab(key)} className="mb-3 mt-2">
                <Nav.Item>
                    <Nav.Link   style={{ color: activeTab === "userInfo" ? "green" : "black" }} eventKey="userInfo">Thông tin cá nhân</Nav.Link>

                </Nav.Item>
                <Nav.Item>
                    <Nav.Link   style={{ color: activeTab === "paymentHistory" ? "green" : "black" }} eventKey="paymentHistory">Lịch sử thanh toán</Nav.Link>

                </Nav.Item>

            </Nav>
            <Row>
                <Col>
                    {activeTab === 'userInfo' && <UserInfo />}
                    {activeTab === 'paymentHistory' && <PaymentHistory />}


                </Col>
            </Row>
        </>
    )
}
export default CustomerSettingHome