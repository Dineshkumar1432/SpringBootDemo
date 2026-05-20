# SpringBootDemo

A comprehensive Spring Boot application demonstrating user authentication, authorization, order management, and RESTful API design patterns with security best practices.

## Overview

This is a full-featured Spring Boot application that showcases:
- User registration and authentication with JWT tokens
- Role-based access control (RBAC)
- RESTful API endpoints with comprehensive security
- Database persistence with JPA/Hibernate
- API documentation with Swagger/OpenAPI
- Message queuing with Apache Kafka
- Caching with Redis
- Application monitoring with Actuator

## Technology Stack

- **Framework**: Spring Boot 3.3.2
- **Java Version**: Java 17
- **Build Tool**: Maven
- **Database**: MySQL
- **Authentication**: Spring Security with JWT (JSON Web Tokens)
- **ORM**: Spring Data JPA with Hibernate
- **API Documentation**: SpringDoc OpenAPI (Swagger UI)
- **Message Queue**: Apache Kafka
- **Caching**: Redis
- **Development Tools**: Lombok, Spring DevTools
- **Testing**: JUnit 5, Spring Boot Test, Spring Security Test

## Key Dependencies

```xml
- spring-boot-starter-web: RESTful web services
- spring-boot-starter-security: Authentication and authorization
- spring-boot-starter-data-jpa: JPA-based data access
- jjwt (0.11.5): JWT handling (API, implementation, and Jackson support)
- spring-kafka: Apache Kafka messaging
- spring-boot-starter-data-redis: Redis caching
- springdoc-openapi-starter-webmvc-ui (2.5.0): API documentation
- spring-boot-starter-actuator: Application monitoring and management
- spring-boot-starter-validation: Bean validation
- mysql-connector-j: MySQL database driver
- h2: In-memory database for testing
- lombok: Boilerplate code reduction
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── DemoApplication.java          # Main Spring Boot application class
│   │   ├── controller/
│   │   │   ├── AuthController.java       # User registration and login endpoints
│   │   │   ├── UserController.java       # User management endpoints
│   │   │   ├── OrderController.java      # Order management endpoints
│   │   │   └── HelloController.java      # Sample endpoints
│   │   ├── service/                      # Business logic layer
│   │   ├── model/                        # Entity classes (User, Order, etc.)
│   │   ├── dto/                          # Data Transfer Objects
│   │   ├── repository/                   # JPA repository interfaces
│   │   ├── security/                     # Security configurations
│   │   ├── exception/                    # Custom exception classes
│   │   └── config/                       # Application configurations
│   └── resources/
│       ├── application.properties        # Application configuration
│       └── application-*.properties      # Environment-specific configs
└── test/
    └── java/com/example/demo/
        └── DemoApplicationTests.java     # Integration tests
```

## API Endpoints

### Authentication Endpoints
- `POST /api/register` - Register a new user
- `POST /api/login` - Login and receive JWT token

### User Management Endpoints (Admin Required)
- `GET /api/users` - Get all users (Admin only)
- `GET /api/usersPaginated?page=0&size=10` - Get paginated users
- `GET /api/users/{id}` - Get user by ID (Own profile or Admin)
- `POST /api/users` - Create new user (Admin only)
- `PUT /api/users/{id}` - Update user (Owner or Admin)
- `DELETE /api/users/{id}` - Delete user (Admin only)
- `GET /api/users/search?name=searchTerm` - Search users by name

### Order Management Endpoints
- `GET /api/orders` - Get all orders (Admin only)
- `GET /api/users/{id}/orders` - Get user's orders
- `POST /api/users/{id}/orders?product=productName` - Create order for user
- `GET /api/users/{id}/orders/{orderId}` - Get specific order

### Sample Endpoints
- `GET /` - Health check endpoint
- `GET /hello` - Sample greeting endpoint

## Security Features

- **JWT Authentication**: Secure token-based authentication
- **Role-Based Access Control**: Admin and User roles with authorization checks
- **Password Security**: Spring Security password encoding
- **Ownership Verification**: Users can only access their own data unless they're administrators
- **Pre-authorization Annotations**: `@PreAuthorize` for declarative security

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL database running
- Redis server (optional, for caching)
- Kafka broker (optional, for messaging)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Dineshkumar1432/SpringBootDemo.git
   cd SpringBootDemo
   ```

2. **Configure database connection**
   
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/demo_db
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**
   - Main API: `http://localhost:8080`
   - Swagger UI Documentation: `http://localhost:8080/swagger-ui.html`
   - Actuator Health: `http://localhost:8080/actuator/health`

## Testing

Run the test suite:
```bash
mvn test
```

Tests include:
- Unit tests for business logic
- Integration tests for API endpoints
- Security tests for authentication and authorization
- H2 in-memory database for isolated testing

## API Documentation

The project includes Swagger/OpenAPI documentation accessible at:
```
http://localhost:8080/swagger-ui.html
```

This provides an interactive interface to explore and test all API endpoints.

## Application Monitoring

Spring Boot Actuator provides various monitoring endpoints:
- `/actuator/health` - Application health status
- `/actuator/metrics` - Application metrics
- `/actuator/env` - Environment properties

## Development

### Key Features for Development
- **Spring DevTools**: Auto-restart on file changes
- **Lombok**: Reduces boilerplate code (@Data, @Getter, @Setter, etc.)
- **H2 Database**: In-memory database for testing

### Project Configuration
- **Java Version**: 17
- **Maven Version**: 3.6+
- **IDE Recommendation**: IntelliJ IDEA or Visual Studio Code

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is open source and available under the MIT License.

## Author

Dinesh Kumar

## Support

For issues, feature requests, or questions, please open an issue on the [GitHub repository](https://github.com/Dineshkumar1432/SpringBootDemo/issues).
