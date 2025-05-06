import React, { useEffect, useState } from 'react';
import { Container, Nav, Row, Col } from 'react-bootstrap';


import UserInfo from '../UserInfo';
import { useSelector } from 'react-redux';


const CustomerSettingHome = () => {
    const [activeTab, setActiveTab] = useState('userInfo');

    const user = useSelector(state => state.authentication)


    return (
        <>
            <h2>Cài đặt </h2>
 
            <Nav variant="tabs" activeKey={activeTab} onSelect={(key) => setActiveTab(key)} className="mb-3 mt-2">
                <Nav.Item>
                    <Nav.Link eventKey="userInfo">Thông tin cá nhân</Nav.Link>
                </Nav.Item>

            </Nav>
            <Row>
                <Col>
                    {activeTab === 'userInfo' && <UserInfo />}

                </Col>
            </Row>
        </>
    )
}
export default CustomerSettingHome