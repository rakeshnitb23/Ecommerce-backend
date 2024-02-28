# E-Commerce Backend API

This is a Spring Boot-based backend API for an e-commerce application. It provides RESTful endpoints for managing products, users, orders, and more.

## Features
- User authentication and authorization
- Product management
- Order processing
- Cart functionality
- Exception handling
- Integration with databases

## Technologies Used
- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Security
- H2/MySQL/PostgreSQL (configurable)
- Maven
- Docker
- JUnit
- Prometheus (monitoring)
- SignalFx/Splunk (observability)

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven
- Docker (for containerization)

### Running Locally
1. Clone the repository:
   ```sh
   git clone <your-repo-url>
   cd e-commerce-backend-api
   ```
2. Build the project:
   ```sh
   mvn clean install
   ```
3. Run the application:
   ```sh
   mvn spring-boot:run
   ```

### Running with Docker
1. Build the Docker image:
   ```sh
   docker build -t ecommerce-backend-api .
   ```
2. Run the Docker container:
   ```sh
   docker run -p 8080:8080 ecommerce-backend-api
   ```

### Monitoring Setup
1. Add the monitoring dependencies to your `pom.xml` (see `monitoring-dependencies.xml`)
2. Run Prometheus alongside your application:
   ```sh
   docker run -d --name prometheus -p 9090:9090 -v $(pwd)/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus
   ```
3. Configure SignalFx by setting the environment variables:
   ```sh
   export $(cat src/main/resources/signalfx.env | xargs)
   ```
4. Access Prometheus dashboard at [http://localhost:9090](http://localhost:9090)

### API Documentation
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Project Structure
```
e-commerce-backend-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── ecommerce/
│   │   │               ├── config/          # Application configuration classes
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   ├── SwaggerConfig.java
│   │   │               │   └── MonitoringConfig.java
│   │   │               ├── controller/      # REST API controllers
│   │   │               │   ├── ProductController.java
│   │   │               │   ├── UserController.java
│   │   │               │   └── OrderController.java
│   │   │               ├── dto/             # Data Transfer Objects
│   │   │               │   ├── ProductDto.java
│   │   │               │   ├── UserDto.java
│   │   │               │   └── OrderDto.java
│   │   │               ├── entity/          # Domain entities (JPA)
│   │   │               │   ├── Product.java
│   │   │               │   ├── User.java
│   │   │               │   ├── Order.java
│   │   │               │   └── OrderItem.java
│   │   │               ├── exception/       # Custom exceptions and handlers
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   └── ResourceNotFoundException.java
│   │   │               ├── repository/      # Data access layer
│   │   │               │   ├── ProductRepository.java
│   │   │               │   ├── UserRepository.java
│   │   │               │   └── OrderRepository.java
│   │   │               ├── service/         # Business logic layer
│   │   │               │   ├── ProductService.java
│   │   │               │   ├── UserService.java
│   │   │               │   └── OrderService.java
│   │   │               └── util/            # Utility classes
│   │   │                   └── JwtTokenUtil.java
│   │   └── resources/
│   │       ├── application.properties       # Application configuration
│   │       ├── application-dev.properties   # Dev environment config
│   │       ├── application-prod.properties  # Production environment config
│   │       ├── application-test.properties  # Test environment config
│   │       ├── prometheus-config.yml        # Prometheus metrics configuration
│   │       └── signalfx.env                 # SignalFx environment variables
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── ecommerce/
│       │               ├── controller/      # Controller tests
│       │               │   ├── ProductControllerTest.java
│       │               │   └── UserControllerTest.java
│       │               ├── integration/     # Integration tests
│       │               │   └── ProductIntegrationTest.java
│       │               └── service/         # Service layer tests
│       │                   ├── ProductServiceTest.java
│       │                   └── OrderServiceTest.java
│       └── resources/
│           └── application-test.properties  # Test-specific properties
├── .dockerignore                            # Docker ignore file
├── .gitignore                               # Git ignore file
├── Dockerfile                               # Docker configuration
├── pom.xml                                  # Maven build configuration
├── prometheus.yml                           # Prometheus server configuration
└── README.md                                # Project documentation
```



## License
This project is licensed under the MIT License.
