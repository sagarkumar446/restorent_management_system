# Quick API Testing Guide

## Using Postman/cURL

### 1. Register a Customer
```bash
curl -X POST http://localhost:8080/api/customers/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "contactNumber": "9876543210",
    "address": "123 Main St",
    "password": "SecurePass123",
    "confirmPassword": "SecurePass123"
  }'
```

### 2. Login Customer
```bash
curl -X POST http://localhost:8080/api/customers/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePass123"
  }'
```

### 3. Get All Menu Items
```bash
curl -X GET http://localhost:8080/api/customers/menu
```

### 4. Place an Order (replace {customerId} with actual ID)
```bash
curl -X POST http://localhost:8080/api/customers/1/orders \
  -H "Content-Type: application/json" \
  -d '{
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
    "specialInstructions": "No spices"
  }'
```

### 5. Get Available Tables
```bash
curl -X GET "http://localhost:8080/api/customers/tables/available?date=2026-03-15&numberOfGuests=4"
```

### 6. Make a Reservation (replace {customerId} with actual ID)
```bash
curl -X POST http://localhost:8080/api/customers/1/reservations \
  -H "Content-Type: application/json" \
  -d '{
    "tableId": 1,
    "reservationDate": "2026-03-15",
    "reservationTime": "19:00",
    "numberOfGuests": 4,
    "specialRequests": "Window seating"
  }'
```

### 7. Get Customer Profile (replace {customerId} with actual ID)
```bash
curl -X GET http://localhost:8080/api/customers/1/profile
```

### 8. Get Order History (replace {customerId} with actual ID)
```bash
curl -X GET "http://localhost:8080/api/customers/1/order-history?page=0&size=10"
```

### 9. Get Loyalty Points (replace {customerId} with actual ID)
```bash
curl -X GET http://localhost:8080/api/customers/1/loyalty-points
```

### 10. Change Password (replace {customerId} with actual ID)
```bash
curl -X POST "http://localhost:8080/api/customers/1/change-password?oldPassword=SecurePass123&newPassword=NewPass456"
```

---

## Postman Collection (JSON)

Import this into Postman for easy testing:

```json
{
  "info": {
    "name": "Restaurant Customer API",
    "version": "1.0"
  },
  "item": [
    {
      "name": "Auth",
      "item": [
        {
          "name": "Register",
          "request": {
            "method": "POST",
            "url": "http://localhost:8080/api/customers/register",
            "body": {
              "mode": "raw",
              "raw": "{\"name\":\"John\",\"email\":\"john@example.com\",\"contactNumber\":\"9876543210\",\"address\":\"123 Main\",\"password\":\"Pass123\",\"confirmPassword\":\"Pass123\"}"
            }
          }
        },
        {
          "name": "Login",
          "request": {
            "method": "POST",
            "url": "http://localhost:8080/api/customers/login",
            "body": {
              "mode": "raw",
              "raw": "{\"email\":\"john@example.com\",\"password\":\"Pass123\"}"
            }
          }
        }
      ]
    },
    {
      "name": "Orders",
      "item": [
        {
          "name": "Place Order",
          "request": {
            "method": "POST",
            "url": "http://localhost:8080/api/customers/1/orders",
            "body": {
              "mode": "raw",
              "raw": "{\"items\":[{\"menuItemId\":1,\"quantity\":2}],\"orderDate\":\"2026-02-22\",\"orderTime\":\"12:30:00\"}"
            }
          }
        },
        {
          "name": "Get Orders",
          "request": {
            "method": "GET",
            "url": "http://localhost:8080/api/customers/1/orders"
          }
        }
      ]
    },
    {
      "name": "Reservations",
      "item": [
        {
          "name": "Make Reservation",
          "request": {
            "method": "POST",
            "url": "http://localhost:8080/api/customers/1/reservations",
            "body": {
              "mode": "raw",
              "raw": "{\"tableId\":1,\"reservationDate\":\"2026-03-15\",\"reservationTime\":\"19:00\",\"numberOfGuests\":4}"
            }
          }
        },
        {
          "name": "Get Reservations",
          "request": {
            "method": "GET",
            "url": "http://localhost:8080/api/customers/1/reservations"
          }
        }
      ]
    }
  ]
}
```

---

## Database Seeding (Sample Data)

For testing, insert sample menu items:

```sql
INSERT INTO menu_item (menu_item_id, item_name, description, price, category, veg) VALUES
(1, 'Biryani', 'Fragrant rice with meat', 250.00, 'Main Course', 0),
(2, 'Paneer Tikka', 'Grilled cottage cheese', 180.00, 'Appetizers', 1),
(3, 'Dal Makhani', 'Creamy lentil curry', 200.00, 'Main Course', 1),
(4, 'Butter Chicken', 'Chicken in butter sauce', 280.00, 'Main Course', 0),
(5, 'Gulab Jamun', 'Sweet fried milk solids', 80.00, 'Desserts', 1);

INSERT INTO sitting_table (table_id, table_number, seating_capacity, status) VALUES
(1, 101, 4, 'AVAILABLE'),
(2, 102, 6, 'AVAILABLE'),
(3, 103, 2, 'AVAILABLE'),
(4, 104, 8, 'OCCUPIED');
```

---

## Key Points to Remember

1. **Customer ID**: After registration/login, use the returned `customerId` for all subsequent requests
2. **Date Format**: Use YYYY-MM-DD for dates
3. **Time Format**: Use HH:mm:ss for times
4. **Loyalty Points**: Automatically calculated (₹1 = 1 point)
5. **Table Status**: Valid values are AVAILABLE, OCCUPIED
6. **Reservation Status**: Valid values are CONFIRMED, CANCELLED, COMPLETED

---

## Testing Workflow

1. Register a new customer → Get customerId
2. Get menu items → Get menuItemId
3. Get available tables → Get tableId 
4. Place an order
5. Make a reservation
6. View order history
7. Check loyalty points
8. View reservations
9. Modify or cancel reservation
10. Logout

---

## Response Status Codes

- `200`: Success (GET, PUT requests)
- `201`: Created (successful POST for creation)
- `400`: Bad Request (validation failure)
- `401`: Unauthorized (authentication failure)
- `404`: Not Found (resource doesn't exist)
- `500`: Server Error

---

## Common Issues & Solutions

### Issue: "Email already registered"
**Solution**: Use a unique email that hasn't been registered before

### Issue: "Invalid email or password"
**Solution**: Verify you're using the correct credentials

### Issue: "Table is already reserved"
**Solution**: Select a different time or available table

### Issue: "Number of guests exceeds table capacity"
**Solution**: Choose a table with larger seating capacity

### Issue: "Menu item not found"
**Solution**: Use valid menu item IDs from your database

---

## Next Steps

1. Add JWT authentication for better security
2. Implement email notifications
3. Add payment gateway integration
4. Create admin dashboard for restaurant staff
5. Implement order status tracking
6. Add rating and review system
