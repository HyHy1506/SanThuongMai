import axios from "axios";
import cookie from 'react-cookies'

const BASE_URL = 'http://localhost:8080/SanThuongMai/api/';

export const endpoints = {
  'register': '/users',
  'login': '/login',
  'current-user': '/secure/profile',
  'products': '/products',
  'products-with-id': (productId) => `/products/${productId}`,
  'categories': '/categories',
  'productDetail': (productId) => `/products/${productId}`,
  'shops': '/shops',
  'shop-with-id': (shopId) => `/shops/${shopId}`,
  'shop-of-user': (userId) => `/shops/user/${userId}`,
  'attributes': '/attributes',
  'seller-with-id': (sellerId) => `/seller/${sellerId}`,
  'ratings': '/product-ratings',
  'comments': `/comments`,
  'replies': (commentId) => `/comments/${commentId}/replies`,
  'updateUser': (userId) => `/users/${userId}`,
  'payments': '/payments',
  'paymentHistory': '/payments/history',
  'create-paypal-order': '/paypal/create-order', // New endpoint for PayPal
  'capture-paypal-order': '/paypal/capture-order'
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