import React, { useEffect, useState } from 'react';
import { Container, Nav, Row, Col } from 'react-bootstrap';
import { useSelector } from 'react-redux';
import { Navigate } from 'react-router-dom';

import SellerSettingHome from './SellerSetting/SellerSettingHome';
import CustomerSettingHome from './CutomerSetting/CustomerSettingHome';

const Setting = () => {

    const user = useSelector(state => state.authentication)
  

    if (!user) {

        return <Navigate to="/" />;
    }

    return (

        <Container className="py-4">
            {user != null && <>
                {user.userRole==="Seller" && <>
                    <SellerSettingHome/>
                
                </>}
                {user.userRole==="Customer" && <>
                    <CustomerSettingHome/>
                
                </>}
                
            </>}
        </Container>
    );
};

export default Setting;