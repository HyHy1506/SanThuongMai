# SanThuongMai - E-Commerce Platform

SanThuongMai is a full-stack e-commerce web application designed to facilitate online buying and selling. It features a robust backend built with Spring MVC and a dynamic frontend developed using React JS. The platform supports user authentication, product management, shopping cart, payment processing, and more, catering to both customers and sellers.

![SanThuongMai Home Page](https://res.cloudinary.com/df5wj9kts/image/upload/v1748936183/Screenshot_2025-06-03_143601_ejlbv4.png)

## Table of Contents
- [Screenshots](#screenshots)
- [Features](#features)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [Setup Instructions](#setup-instructions)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)


## Screenshots
### Product Detail Page
![Product Detail](https://res.cloudinary.com/df5wj9kts/image/upload/v1748936409/Screenshot_2025-06-03_143843_ww1rce.png)
![Product Detail](https://res.cloudinary.com/df5wj9kts/image/upload/v1748936409/Screenshot_2025-06-03_143922_ddsmve.png)
![Product Detail](https://res.cloudinary.com/df5wj9kts/image/upload/v1748936407/Screenshot_2025-06-03_143940_c4hh4m.png)


### Shopping Cart
![Shopping Cart](https://res.cloudinary.com/df5wj9kts/image/upload/v1748936675/Screenshot_2025-06-03_144254_tadwco.png)

### Seller Dashboard
![Seller Dashboard](https://res.cloudinary.com/df5wj9kts/image/upload/v1748936676/Screenshot_2025-06-03_144354_avorqv.png)

### Chat
![Chat](https://res.cloudinary.com/df5wj9kts/image/upload/v1748936675/Screenshot_2025-06-03_144419_bbsrd8.png)


## Features
- **User Authentication**: Supports login/register via username/password and Google OAuth using Firebase.
- **Product Management**: Sellers can create, update, and delete products and shops.
- **Shopping Cart**: Users can add, remove, and update quantities of products in their cart.
- **Payment Processing**: Integrates multiple payment methods including COD, PayPal, ZaloPay, and Momo.
- **Product Reviews and Ratings**: Customers can rate products and leave comments.
- **Search and Filter**: Search products by keywords and filter by categories.
- **Seller Dashboard**: Manage shops, products, and view seller status (Pending/Approved/Rejected).
- **Responsive Design**: Built with Bootstrap for a mobile-friendly interface.

## Technologies
### Backend
- **Spring MVC**: Core framework for handling web requests.
- **Hibernate**: ORM for database interactions.
- **Spring Security**: Manages authentication and authorization.
- **Cloudinary**: Handles image uploads.
- **MySQL**: Relational database for data storage.
- **Maven**: Dependency management.
- **PayPal SDK**: For PayPal payment integration.

### Frontend
- **React JS**: JavaScript library for building user interfaces.
- **Redux**: State management for authentication and cart.
- **React Bootstrap**: UI components for styling.
- **Axios**: HTTP client for API requests.
- **Firebase**: Google OAuth authentication.
- **React Toastify**: Notification system.
- **PayPal React SDK**: PayPal payment integration.

## Project Structure
### Backend (Spring MVC)
```
SanThuongMai/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/example/santhuongmai/
│   │   │   │   ├── config/ (Security, Web, Cloudinary configs)
│   │   │   │   ├── controller/ (REST and MVC controllers)
│   │   │   │   ├── dto/ (Data transfer objects)
│   │   │   │   ├── model/ (Entities like Product, User, Shop)
│   │   │   │   ├── repository/ (JPA repositories)
│   │   │   │   ├── service/ (Business logic)
│   │   │   │   └── util/ (Utilities like email, JWT)
│   │   ├── resources/
│   │   │   ├── templates/ (Thymeleaf templates)
│   │   │   ├── static/ (CSS, JS, images)
│   │   │   └── application.properties (Configurations)
│   └── test/ (Unit tests)
├── pom.xml
```

### Frontend (React JS)
```
santhuongmai-web/
├── src/
│   ├── actions/ (Redux actions for authentication, cart)
│   ├── components/
│   │   ├── Cart.js
│   │   ├── Checkout/Checkout.js
│   │   ├── Home.js
│   │   ├── Login.js
│   │   ├── Product/ (ProductCard, ProductDetail, CommentSection, StarRating)
│   │   ├── Register.js
│   │   ├── Search/SearchHome.js
│   │   ├── SettingManager/ (Customer and Seller settings)
│   │   └── layouts/ (Header, Footer, MySpinner)
│   ├── configs/ (API endpoints, Firebase config)
│   ├── image/ (Static assets like google-logo.png)
│   ├── reducers/ (Redux reducers for state management)
│   ├── App.js
│   ├── index.js
│   ├── App.css
│   ├── index.css
│   └── logo.svg
├── package.json
```

## Setup Instructions
### Prerequisites
- **Java 17** or later
- **Node.js** (v16 or later)
- **MySQL** database
- **Cloudinary** account (for image uploads)
- **PayPal Developer Account** (for payment integration)
- **Firebase Project** (for Google OAuth)

### Backend Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/SanThuongMai.git
   cd SanThuongMai
   ```
2. Configure `application.properties` in `src/main/resources`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/santhuongmai
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   cloudinary.cloud-name=your-cloud-name
   cloudinary.api-key=your-api-key
   cloudinary.api-secret=your-api-secret
   ```
3. Build and run the project:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
4. The backend will be available at `http://localhost:8080`.

### Frontend Setup
1. Navigate to the frontend directory:
   ```bash
   cd santhuongmai-web
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Create a `.env` file in `santhuongmai-web` and add Firebase and PayPal credentials:
   ```env
   REACT_APP_FIREBASE_APIKEY=your-firebase-api-key
   REACT_APP_FIREBASE_AUTHDOMAIN=your-auth-domain
   REACT_APP_FIREBASE_PROJECTID=your-project-id
   REACT_APP_FIREBASE_STORAGEBUCKET=your-storage-bucket
   REACT_APP_FIREBASE_MESSAGINGSENDERID=your-sender-id
   REACT_APP_FIREBASE_APPID=your-app-id
   REACT_APP_FIREBASE_MEASUREMENTID=your-measurement-id
   REACT_APP_PAYPAL_CLIENT_ID=your-paypal-client-id
   ```
4. Start the development server:
   ```bash
   npm start
   ```
5. The frontend will be available at `http://localhost:3000`.

## Usage
1. **Register/Login**: Create an account or log in using credentials or Google OAuth.
2. **Browse Products**: View products on the homepage, filter by category, or search by keywords.
3. **Add to Cart**: Add products to your cart and proceed to checkout.
4. **Checkout**: Choose a payment method (COD, PayPal, ZaloPay, Momo) to complete the purchase.
5. **Seller Features**: Access the seller dashboard to create/manage shops and products.
6. **Rate and Comment**: Leave ratings and comments on purchased products.

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -m 'Add your feature'`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a Pull Request.

