# ESPN Cricinfo System

A production-grade cricket scoring and analytics system built with Spring Boot, implementing advanced design patterns and modern architecture.

## 🏗️ Architecture

### Design Patterns Implemented
- **Strategy Pattern**: Match format rules (T20, ODI, Test)
- **State Pattern**: Match, innings, and player lifecycle management

### Multi-Module Structure
```
espn-cricinfo-system/
├── espn-cricinfo-domain/          # Domain entities and value objects
├── espn-cricinfo-core/            # Business logic with design patterns
├── espn-cricinfo-infrastructure/  # Data access and external integrations
├── espn-cricinfo-api/             # REST API controllers and DTOs
├── espn-cricinfo-config/          # Configuration classes
└── espn-cricinfo-application/     # Main Spring Boot application
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL 14+
- Redis 7+
- Kafka 3.0+

### Running Locally

1. **Clone and build:**
```bash
mvn clean install
```

2. **Start dependencies:**
```bash
# Using Docker Compose (recommended)
docker-compose up -d postgres redis kafka
```

3. **Run the application:**
```bash
cd espn-cricinfo-application
mvn spring-boot:run
```

4. **Access the application:**
- API: http://localhost:8080/api/v1
- Swagger UI: http://localhost:8080/api/v1/swagger-ui.html
- Actuator: http://localhost:8080/api/v1/actuator/health

## ⚙️ Configuration

### Environment Variables
```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=cricinfo_db
DB_USERNAME=cricinfo_user
DB_PASSWORD=cricinfo_password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# Kafka
KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# Security
JWT_SECRET=your-secret-key
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8080

# Server
SERVER_PORT=8080
```

### Profiles
- `local`: Development with local services
- `docker`: Docker containerized environment

## 📊 Features

### Cricket Scoring
- ✅ Multiple match formats (T20, ODI, Test)
- ✅ Real-time ball-by-ball scoring
- ✅ Player statistics tracking
- ✅ Match state management

### Architecture
- ✅ **Strategy Pattern**: Format-specific scoring rules
- ✅ **State Pattern**: Lifecycle management
- ✅ **CQRS Pattern**: Read/write separation (planned)
- ✅ **Event Sourcing**: Cricket events (planned)

### Production Ready
- ✅ Database migrations (Flyway)
- ✅ Caching (Redis)
- ✅ Message queuing (Kafka)
- ✅ Monitoring (Actuator, Prometheus)
- ✅ Security (JWT, CORS)
- ✅ Resilience (Circuit Breaker)

## 🔧 Development

### Building
```bash
# Clean and compile all modules
mvn clean compile

# Run tests
mvn test

# Package application
mvn package

# Build Docker image
mvn clean package -Pdocker
```

### Testing
```bash
# Unit tests
mvn test

# Integration tests
mvn verify

# Code coverage
mvn jacoco:report
```

## 📈 API Endpoints

### Health Check
- `GET /api/v1/health` - Application health status
- `GET /api/v1/actuator/health` - Detailed health checks

### Match Management (Planned)
- `POST /api/v1/matches` - Create match
- `GET /api/v1/matches/{id}` - Get match details
- `PUT /api/v1/matches/{id}/start` - Start match
- `POST /api/v1/matches/{id}/balls` - Record ball

### Player Management (Planned)
- `GET /api/v1/players/{id}` - Get player details
- `GET /api/v1/players/{id}/stats` - Get player statistics

## 🐳 Docker Deployment

### Build and Run
```bash
# Build application
mvn clean package -Pdocker

# Run with Docker Compose
docker-compose up -d

# View logs
docker-compose logs -f app
```

### Kubernetes Deployment (Planned)
- Helm charts for K8s deployment
- Istio service mesh
- Horizontal Pod Autoscaling

## 📊 Monitoring

### Metrics
- Application metrics via Micrometer
- JVM metrics
- Database connection pool metrics
- Redis cache metrics

### Logging
- Structured logging with SLF4J
- Log aggregation (ELK stack planned)
- Distributed tracing (Jaeger planned)

## 🔒 Security

### Authentication
- JWT-based authentication
- Role-based access control (planned)

### API Security
- CORS configuration
- Rate limiting
- Input validation
- SQL injection prevention

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Implement changes with tests
4. Ensure code coverage > 80%
5. Submit pull request

## 📝 License

This project is licensed under the MIT License.

## 🏆 Acknowledgments

- Built following Domain-Driven Design principles
- Inspired by real-world cricket scoring systems
- Production-ready architecture patterns
