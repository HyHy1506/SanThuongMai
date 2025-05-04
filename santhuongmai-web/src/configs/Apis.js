import axios from "axios";
import cookie from 'react-cookies'

const BASE_URL = 'http://localhost:8080/SanThuongMai/api/';

export const endpoints = {

    'register': '/users',
    'login': '/login',
    'current-user': '/secure/profile',
    'products':'/products',
    'categories':'/categories',
    'productDetail':(productId)=>`/products/${productId}`,
    'shops':'/shops',
    'shop-with-id':(shopId)=>`/shops/${shopId}`,
    'shop-of-user':(userId)=>`/shops/user/${userId}`,
}

export const authApis = () => {
    return axios.create({
        baseURL: BASE_URL,
        headers: {
            'Authorization': `Bearer ${cookie.load('token')}`
        }
    })
}

export default axios.create({
    baseURL: BASE_URL
});