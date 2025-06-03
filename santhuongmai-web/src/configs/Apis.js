import axios from "axios";


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
  'user-with-id': (userId) => `/users/${userId}`,
  'payments': '/payments',
  'paymentHistory': '/payments/history',
  'create-paypal-order': '/paypal/create-order', // New endpoint for PayPal
  'capture-paypal-order': '/paypal/capture-order',
  'googleLogin': '/google-login',
  'soft-delete-shop-with-id': (shopId) => `/shops/soft/${shopId}`,
  'soft-delete-product-with-id': (productId) => `/products/soft/${productId}`,
  'revenueStatistics': (period, year, categoryId, shopId) =>
    `/statistics/revenue?period=${period}&year=${year}&categoryId=${categoryId || ''}&shopId=${shopId || ''}`
}

export const authApis = () => {
  return axios.create({
    baseURL: BASE_URL,
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
  })
}

export default axios.create({
  baseURL: BASE_URL
});