# Restaurant Management System - Setup & Configuration Guide

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.9 or higher
- Git (optional)

---

## Step-by-Step Setup

### Step 1: Database Setup

1. **Create Database**
   ```sql
   CREATE DATABASE restaurant_management_system;
   USE restaurant_management_system;
   ```

2. **Verify Tables** (Hibernate will auto-create)
   The following tables will be auto-generated:
   - customer
   - menu_item
   - sitting_table
   - order_by
   - order_detail
   - reservation
   - payment
   - payment_config
   - employee
   - category

### Step 2: Configure Application Properties

Edit `src/main/resources/application.properties`:

```properties
# Application
spring.application.name=restorent_management_system

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/restaurant_management_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Mail Configuration (for future email features)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_email_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Razorpay Configuration
razorpay.key.id=YOUR_RAZORPAY_KEY_ID
razorpay.key.secret=YOUR_RAZORPAY_KEY_SECRET

# Server Configuration (optional)
server.port=8080
```

### Step 3: Build the Project

```bash
cd /home/sagar/project/restorent_management_system
mvn clean install
```

### Step 4: Seed Sample Data

Insert sample data for testing:

```sql
-- Menu Items
INSERT INTO menu_item (menu_item_id, item_name, description, price, category, veg) VALUES
(1, 'Biryani', 'Fragrant rice with meat and spices', 250.00, 'Main Course', false),
(2, 'Paneer Tikka', 'Grilled cottage cheese with Indian spices', 180.00, 'Appetizers', true),
(3, 'Dal Makhani', 'Creamy lentil curry', 200.00, 'Main Course', true),
(4, 'Butter Chicken', 'Chicken in butter cream sauce', 280.00, 'Main Course', false),
(5, 'Gulab Jamun', 'Sweet fried milk solids in syrup', 80.00, 'Desserts', true),
(6, 'Tandoori Chicken', 'Spiced grilled chicken', 320.00, 'Main Course', false),
(7, 'Aloo Gobi', 'Potato and cauliflower curry', 150.00, 'Vegetables', true),
(8, 'Samosas', 'Crispy triangular pastry', 60.00, 'Appetizers', true);

-- Tables
INSERT INTO sitting_table (table_id, table_number, seating_capacity, status) VALUES
(1, 101, 2, 'AVAILABLE'),
(2, 102, 2, 'AVAILABLE'),
(3, 103, 4, 'AVAILABLE'),
(4, 104, 4, 'AVAILABLE'),
(5, 105, 6, 'AVAILABLE'),
(6, 106, 6, 'AVAILABLE'),
(7, 107, 8, 'AVAILABLE'),
(8, 108, 8, 'AVAILABLE');
```

### Step 5: Run the Application

```bash
mvn spring-boot:run
```

Or build and run:
```bash
mvn clean package
java -jar target/restorent_management_system-0.0.1-SNAPSHOT.jar
```

### Step 6: Verify Application is Running

- Open browser and go to: `http://localhost:8080`
- Check logs for startup messages
- You should see "Started RestaurantManagementSystemApplication..."

---

## API Base URL

```
http://localhost:8080/api/customers
```

---

## Testing the API

### Using cURL (Register)
```bash
curl -X POST http://localhost:8080/api/customers/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "contactNumber": "9876543210",
    "address": "123 Main St",
    "password": "TestPass123",
    "confirmPassword": "TestPass123"
  }'
```

### Using Postman
- Import the included Postman collection
- Set base URL to `http://localhost:8080/api/customers`
- Start with Register endpoint

---

## Project Structure

```
restorent_management_system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── edu/qsp/restorent_management_system/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── model/
│   │   │       ├── dto/
│   │   │       ├── config/
│   │   │       └── RestaurantManagementSystemApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
└── pom.xml
```

---

## Maven Dependencies

The project includes:
- **Spring Boot 3.3.2**
- **Spring Data JPA**
- **MySQL Connector**
- **Spring Mail**
- **Razorpay** (payment integration)

All dependencies are managed in `pom.xml`

---

## Configuration Files to Know

1. **application.properties**: Main configuration
   - Database settings
   - Mail configuration
   - File upload limits
   - Payment keys

2. **pom.xml**: Project dependencies
   - Spring Boot version
   - Database drivers
   - Additional libraries

---

## Common Issues & Solutions

### Issue: "Cannot connect to database"
**Solution**: 
- Verify MySQL is running
- Check credentials in application.properties
- Ensure database exists: `CREATE DATABASE restaurant_management_system;`

### Issue: "Port 8080 already in use"
**Solution**:
- Change port in application.properties: `server.port=8081`
- Or kill process using port: `lsof -ti:8080 | xargs kill -9`

### Issue: "Compilation errors"
**Solution**:
- Clean and rebuild: `mvn clean compile`
- Check Java version: `java -version`
- Update Maven: `mvn -v`

### Issue: "Tables not created"
**Solution**:
- Check `spring.jpa.hibernate.ddl-auto=update`
- Check database logs
- Verify entity classes are properly annotated

### Issue: "CORS errors in frontend"
**Solution**:
- Update CORS origin in CustomerController
- Allowed origins currently: `http://localhost:3000`

---

## Environment Variables (Optional)

For production, use environment variables:

```bash
export DB_URL=jdbc:mysql://localhost:3306/restaurant_management_system
export DB_USER=root
export DB_PASSWORD=your_password
export MAIL_USERNAME=your_email@gmail.com
export MAIL_PASSWORD=your_app_password
export RAZORPAY_KEY_ID=your_key_id
export RAZORPAY_KEY_SECRET=your_key_secret
```

---

## Logging Configuration

To enable detailed logging, add to `application.properties`:

```properties
logging.level.root=INFO
logging.level.edu.qsp.restorent_management_system=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

---

## Database Connection Pool

Configured in Spring Boot by default with HikariCP:

```properties
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.maximum-pool-size=5
```

---

## Best Practices

1. **Always use HTTPS** in production
2. **Use strong passwords** and hash them with BCrypt
3. **Implement JWT** for token-based auth
4. **Add rate limiting** to prevent brute force
5. **Use database transactions** for critical operations
6. **Implement logging** for audit trails
7. **Validate all inputs** on backend
8. **Use parameterized queries** to prevent SQL injection
9. **Add exception handling** at global level
10. **Monitor performance** regularly

---

## Deployment Checklist

- [ ] Database configured and optimized
- [ ] application.properties updated for production
- [ ] SSL certificates configured
- [ ] Logging configured
- [ ] Backup strategy in place
- [ ] Monitoring set up
- [ ] Security headers configured
- [ ] Rate limiting enabled
- [ ] Database backups automated
- [ ] Error logging to external service

---

## Updating Dependencies

```bash
# Check for updates
mvn versions:display-dependency-updates

# Update to latest versions
mvn versions:use-latest-releases
```

---

## Performance Optimization Tips

1. **Add database indexes** on frequently searched columns
2. **Implement caching** for menu items
3. **Use pagination** for large result sets
4. **Optimize database queries**
5. **Implement connection pooling**
6. **Use async processing** for emails

---

## Monitoring & Maintenance

1. **Check logs regularly**
   ```bash
   tail -f logs/application.log
   ```

2. **Monitor database size**
   ```sql
   SELECT table_name, ROUND(((data_length + index_length) / 1024 / 1024), 2) AS SIZE_MB
   FROM information_schema.TABLES 
   WHERE table_schema = 'restaurant_management_system';
   ```

3. **Monitor connections**
   ```sql
   SHOW STATUS WHERE variable_name = 'Threads_connected';
   ```

---

## Backup & Recovery

**Backup Database:**
```bash
mysqldump -u root -p restaurant_management_system > backup.sql
```

**Restore Database:**
```bash
mysql -u root -p restaurant_management_system < backup.sql
```

---

## Support & Documentation

- **API Documentation**: See `API_DOCUMENTATION.md`
- **Testing Guide**: See `TESTING_GUIDE.md`
- **Implementation Summary**: See `IMPLEMENTATION_SUMMARY.md`

---

## Next Steps

1. Start the application
2. Register a test customer
3. Browse the menu
4. Place an order
5. Make a reservation
6. Explore all endpoints
7. Check the documentation

---

## Quick Reference Commands

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Test
mvn test

# Package
mvn package

# Clean
mvn clean

# Check dependencies
mvn dependency:tree

# Skip tests during build
mvn clean install -DskipTests
```

---

## License & Contact

For questions or support, please contact the development team.

Happy coding! 🚀
