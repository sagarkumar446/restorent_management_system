# Complete Customer Management API Documentation

## Overview
This is a comprehensive REST API for customer management in a restaurant system. It includes features for customer registration, authentication, order management, and table reservations.

## Base URL
```
http://localhost:8080/api/customers
```

## API Endpoints

### Authentication Endpoints

#### 1. Register Customer
**POST** `/register`

Register a new customer account.

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "contactNumber": "9876543210",
  "address": "123 Main St, City, Country",
  "password": "SecurePassword123",
  "confirmPassword": "SecurePassword123"
}
```

**Response (201 Created):**
```json
{
  "statusCode": 201,
  "message": "Customer registered successfully",
  "data": {
    "customerId": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "contactNumber": "9876543210",
    "address": "123 Main St, City, Country",
    "registrationDate": "2026-02-22 11:30:00",
    "isActive": true,
    "loyaltyPoints": 0.0
  },
  "success": true
}
```

---

#### 2. Login Customer
**POST** `/login`

Authenticate customer with email and password.

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "SecurePassword123"
}
```

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Login successful",
  "data": {
    "customerId": 1,
    "email": "john@example.com",
    "name": "John Doe",
    "loginTime": "2026-02-22 11:35:00"
  },
  "success": true
}
```

---

#### 3. Logout Customer
**POST** `/{customerId}/logout`

Logout the currently logged-in customer.

**Path Parameters:**
- `customerId` (Long): The ID of the customer

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Logout successful",
  "data": true,
  "success": true
}
```

---

#### 4. Get Customer Profile
**GET** `/{customerId}/profile`

Retrieve the profile information of a customer.

**Path Parameters:**
- `customerId` (Long): The ID of the customer

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Profile fetched successfully",
  "data": {
    "customerId": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "contactNumber": "9876543210",
    "address": "123 Main St, City, Country",
    "loyaltyPoints": 150.0,
    "isActive": true
  },
  "success": true
}
```

---

#### 5. Update Customer Profile
**PUT** `/{customerId}/profile`

Update customer profile information.

**Path Parameters:**
- `customerId` (Long): The ID of the customer

**Request Body:**
```json
{
  "name": "John Updated",
  "contactNumber": "9876543210",
  "address": "456 Update St, City, Country"
}
```

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Profile updated successfully",
  "data": {
    "customerId": 1,
    "name": "John Updated",
    "email": "john@example.com",
    "contactNumber": "9876543210",
    "address": "456 Update St, City, Country",
    "registrationDate": "2026-02-22 11:30:00",
    "isActive": true,
    "loyaltyPoints": 150.0
  },
  "success": true
}
```

---

#### 6. Change Password
**POST** `/{customerId}/change-password`

Change the customer's password.

**Path Parameters:**
- `customerId` (Long): The ID of the customer

**Query Parameters:**
- `oldPassword` (String): Current password
- `newPassword` (String): New password

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Password changed successfully",
  "data": true,
  "success": true
}
```

---

#### 7. Delete Customer Account
**DELETE** `/{customerId}`

Deactivate/delete a customer account.

**Path Parameters:**
- `customerId` (Long): The ID of the customer

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Account deleted successfully",
  "data": true,
  "success": true
}
```

---

### Menu Endpoints

#### 8. Get All Menu Items
**GET** `/menu`

Retrieve all available menu items.

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Menu items fetched successfully",
  "data": [
    {
      "menuItemId": 1,
      "itemName": "Biryani",
      "description": "Fragrant rice with spices",
      "price": 250.0,
      "category": "Main Course",
      "veg": false
    },
    {
      "menuItemId": 2,
      "itemName": "Paneer Tikka",
      "description": "Grilled cottage cheese",
      "price": 180.0,
      "category": "Appetizers",
      "veg": true
    }
  ],
  "success": true
}
```

---

#### 9. Get Specific Menu Item
**GET** `/menu/{menuItemId}`

Retrieve details of a specific menu item.

**Path Parameters:**
- `menuItemId` (Integer): The ID of the menu item

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Menu item fetched successfully",
  "data": {
    "menuItemId": 1,
    "itemName": "Biryani",
    "description": "Fragrant rice with spices",
    "price": 250.0,
    "category": "Main Course",
    "veg": false
  },
  "success": true
}
```

---

### Table Endpoints

#### 10. Get All Tables
**GET** `/tables`

Retrieve all available tables in the restaurant.

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Tables fetched successfully",
  "data": [
    {
      "tableId": 1,
      "tableNumber": 101,
      "seatingCapacity": 4,
      "status": "AVAILABLE"
    },
    {
      "tableId": 2,
      "tableNumber": 102,
      "seatingCapacity": 6,
      "status": "OCCUPIED"
    }
  ],
  "success": true
}
```

---

#### 11. Get Available Tables for Date
**GET** `/tables/available`

Retrieve available tables for a specific date and number of guests.

**Query Parameters:**
- `date` (String): Reservation date (format: YYYY-MM-DD)
- `numberOfGuests` (Integer): Number of guests

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Available tables fetched successfully",
  "data": [
    {
      "tableId": 1,
      "tableNumber": 101,
      "seatingCapacity": 4,
      "status": "AVAILABLE"
    }
  ],
  "success": true
}
```

---

### Order Endpoints

#### 12. Place Order
**POST** `/{customerId}/orders`

Place a new order.

**Path Parameters:**
- `customerId` (Long): The ID of the customer

**Request Body:**
```json
{
  "items": [
    {
      "menuItemId": 1,
      "quantity": 2
    },
    {
      "menuItemId": 2,
      "quantity": 1
    }
  ],
  "orderDate": "2026-02-22",
  "orderTime": "12:30:00",
  "specialInstructions": "No spices, please."
}
```

**Response (201 Created):**
```json
{
  "statusCode": 201,
  "message": "Order placed successfully",
  "data": {
    "orderId": 10001,
    "orderDate": "2026-02-22",
    "orderTime": "12:30:00",
    "totalAmount": 680.0,
    "orderDetails": [
      {
        "orderDetailId": 1,
        "quantity": 2,
        "unitPrice": 250.0,
        "menuItem": { "menuItemId": 1, "itemName": "Biryani" }
      }
    ]
  },
  "success": true
}
```

---

#### 13. Get Customer Orders
**GET** `/{customerId}/orders`

Retrieve all orders placed by a customer.

**Path Parameters:**
- `customerId` (Long): The ID of the customer

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Orders fetched successfully",
  "data": [
    {
      "orderId": 10001,
      "orderDate": "2026-02-22",
      "orderTime": "12:30:00",
      "totalAmount": 680.0
    }
  ],
  "success": true
}
```

---

#### 14. Get Order Details
**GET** `/orders/{orderId}`

Retrieve detailed information about a specific order.

**Path Parameters:**
- `orderId` (Long): The ID of the order

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Order details fetched successfully",
  "data": {
    "orderId": 10001,
    "orderDate": "2026-02-22",
    "orderTime": "12:30:00",
    "totalAmount": 680.0,
    "orderDetails": [
      {
        "orderDetailId": 1,
        "quantity": 2,
        "unitPrice": 250.0
      }
    ]
  },
  "success": true
}
```

---

#### 15. Cancel Order
**DELETE** `/orders/{orderId}`

Cancel a previously placed order.

**Path Parameters:**
- `orderId` (Long): The ID of the order

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Order cancelled successfully",
  "data": true,
  "success": true
}
```

---

#### 16. Get Order History
**GET** `/{customerId}/order-history`

Retrieve order history with pagination.

**Path Parameters:**
- `customerId` (Long): The ID of the customer

**Query Parameters:**
- `page` (Integer, default: 0): Page number
- `size` (Integer, default: 10): Number of items per page

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Order history fetched successfully",
  "data": [
    {
      "orderId": 10001,
      "orderDate": "2026-02-22",
      "orderTime": "12:30:00",
      "totalAmount": 680.0
    }
  ],
  "success": true
}
```

---

#### 17. Get Total Spending
**GET** `/{customerId}/total-spending`

Get the total amount spent by a customer.

**Path Parameters:**
- `customerId` (Long): The ID of the customer

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Total spending fetched successfully",
  "data": 1250.50,
  "success": true
}
```

---

### Reservation Endpoints

#### 18. Make Reservation
**POST** `/{customerId}/reservations`

Make a table reservation.

**Path Parameters:**
- `customerId` (Long): The ID of the customer

**Request Body:**
```json
{
  "tableId": 1,
  "reservationDate": "2026-03-15",
  "reservationTime": "19:00",
  "numberOfGuests": 4,
  "specialRequests": "Window seating please"
}
```

**Response (201 Created):**
```json
{
  "statusCode": 201,
  "message": "Reservation made successfully",
  "data": {
    "reservationId": 5001,
    "reservationDate": "2026-03-15",
    "reservationTime": "19:00",
    "numberOfGuests": 4,
    "specialRequests": "Window seating please",
    "reservationStatus": "CONFIRMED",
    "sitting_table": {
      "tableId": 1,
      "tableNumber": 101,
      "seatingCapacity": 4
    }
  },
  "success": true
}
```

---

#### 19. Get Customer Reservations
**GET** `/{customerId}/reservations`

Retrieve all reservations made by a customer.

**Path Parameters:**
- `customerId` (Long): The ID of the customer

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Reservations fetched successfully",
  "data": [
    {
      "reservationId": 5001,
      "reservationDate": "2026-03-15",
      "reservationTime": "19:00",
      "numberOfGuests": 4,
      "reservationStatus": "CONFIRMED"
    }
  ],
  "success": true
}
```

---

#### 20. Get Reservation Details
**GET** `/reservations/{reservationId}`

Retrieve detailed information about a specific reservation.

**Path Parameters:**
- `reservationId` (Long): The ID of the reservation

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Reservation details fetched successfully",
  "data": {
    "reservationId": 5001,
    "reservationDate": "2026-03-15",
    "reservationTime": "19:00",
    "numberOfGuests": 4,
    "specialRequests": "Window seating please",
    "reservationStatus": "CONFIRMED",
    "sitting_table": { "tableId": 1, "tableNumber": 101 }
  },
  "success": true
}
```

---

#### 21. Cancel Reservation
**DELETE** `/reservations/{reservationId}`

Cancel a table reservation.

**Path Parameters:**
- `reservationId` (Long): The ID of the reservation

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Reservation cancelled successfully",
  "data": true,
  "success": true
}
```

---

#### 22. Modify Reservation
**PUT** `/reservations/{reservationId}`

Modify reservation date or time.

**Path Parameters:**
- `reservationId` (Long): The ID of the reservation

**Request Body:**
```json
{
  "tableId": 1,
  "reservationDate": "2026-03-16",
  "reservationTime": "20:00",
  "numberOfGuests": 4
}
```

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Reservation modified successfully",
  "data": {
    "reservationId": 5001,
    "reservationDate": "2026-03-16",
    "reservationTime": "20:00",
    "numberOfGuests": 4,
    "reservationStatus": "CONFIRMED"
  },
  "success": true
}
```

---

#### 23. Get Reservation History
**GET** `/{customerId}/reservation-history`

Retrieve reservation history with pagination.

**Path Parameters:**
- `customerId` (Long): The ID of the customer

**Query Parameters:**
- `page` (Integer, default: 0): Page number
- `size` (Integer, default: 10): Number of items per page

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Reservation history fetched successfully",
  "data": [
    {
      "reservationId": 5001,
      "reservationDate": "2026-03-15",
      "reservationTime": "19:00",
      "numberOfGuests": 4,
      "reservationStatus": "CONFIRMED"
    }
  ],
  "success": true
}
```

---

### Loyalty & Points Endpoints

#### 24. Get Loyalty Points
**GET** `/{customerId}/loyalty-points`

Get the loyalty points balance for a customer.

**Path Parameters:**
- `customerId` (Long): The ID of the customer

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "message": "Loyalty points fetched successfully",
  "data": 850.50,
  "success": true
}
```

---

## Error Responses

### 400 Bad Request
```json
{
  "statusCode": 400,
  "message": "Validation error message",
  "data": null,
  "success": false
}
```

### 401 Unauthorized
```json
{
  "statusCode": 401,
  "message": "Invalid credentials or expired token",
  "data": null,
  "success": false
}
```

### 404 Not Found
```json
{
  "statusCode": 404,
  "message": "Resource not found",
  "data": null,
  "success": false
}
```

### 500 Internal Server Error
```json
{
  "statusCode": 500,
  "message": "Internal server error",
  "data": null,
  "success": false
}
```

---

## Features

### Authentication
- Customer registration with email validation
- Login with email and password
- Session management
- Password change functionality
- Account deactivation

### Order Management
- Place new orders with multiple items
- View order history
- Order cancellation
- Total spending calculation
- Loyalty points tracking (1 point per rupee)

### Table Reservations
- Make table reservations with date and time
- Automatic availability checking
- Table capacity validation
- Modify or cancel reservations
- View reservation history

### Menu Management
- View all menu items
- Get detailed item information
- Filter by category and dietary preferences

### Loyalty Program
- Earn loyalty points on orders
- Track total loyalty points
- Use points for future orders

---

## Installation & Setup

1. **Database Configuration**
   Update `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/restaurant_management_system
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

2. **Build the Project**
   ```bash
   mvn clean install
   ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the API**
   Base URL: `http://localhost:8080/api/customers`

---

## Security Notes

1. **Password Security**: Passwords are encoded using Base64 (recommended to use BCrypt in production)
2. **Input Validation**: All inputs are validated
3. **Email Verification**: Implemented
4. **Duplicate Prevention**: Email and contact numbers are unique

---

## Future Enhancements

1. JWT Token-based authentication
2. Email notifications for orders and reservations
3. Payment integration
4. Order status tracking (Preparing, Ready, Delivered)
5. Rating and reviews system
6. Dining history analytics
7. SMS notifications
8. Mobile app support

---

## Contact & Support

For API support and inquiries, please contact the development team.
