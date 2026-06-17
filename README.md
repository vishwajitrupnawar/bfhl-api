# BFHL API & Health Checker

Production-ready REST API built with **Java 17 + Spring Boot 3.2** following clean layered architecture and SOLID principles.

---

## 🚀 Live URL

> **`<YOUR_DEPLOYED_URL>`** — _Replace with your Render / Railway / AWS URL after deployment._

---

## 📦 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Build Tool | Maven |
| Validation | Jakarta Bean Validation |
| Logging | SLF4J + Logback |
| Testing | JUnit 5 + Mockito + MockMvc |
| Coverage | JaCoCo |
| Containerisation | Docker (multi-stage build) |
| CI | GitHub Actions |

---

## 🌐 API Endpoints

### 1. `GET /health`

Lightweight endpoint to check if the service is running. Responds in `<50ms`.

#### Request

```http
GET /health HTTP/1.1
```

#### Response (200 OK)

```json
{
  "status": "UP"
}
```

---

### 2. `POST /bfhl`

Processes an array of mixed data (numbers, alphabets, alphanumeric, special characters) and returns comprehensive analytics.

#### Request

```http
POST /bfhl HTTP/1.1
Content-Type: application/json
X-Request-Id: my-unique-id          # optional – auto-generated if absent
```

```json
{
  "data": ["A", "1", "A1B2", "$", "25.5", "-10", "Test123", "", null]
}
```

#### Response (200 OK)

```json
{
  "is_success": true,
  "request_id": "my-unique-id",
  "odd_numbers": [1, -10],
  "even_numbers": [2, 25.5],
  "sum": "8.5",
  "largest_number": 25.5,
  "smallest_number": -10,
  "sorted_numbers": [-10, 1, 2, 3, 25.5, 123],
  "alphabets": ["A", "B", "T", "E", "S"],
  "alphabet_count": 8,
  "alphabet_frequency": { "A": 2, "B": 1, "E": 1, "S": 1, "T": 2 },
  "vowel_count": 3,
  "consonant_count": 5,
  "longest_alphabetic_value": "Test",
  "shortest_alphabetic_value": "A",
  "special_characters": ["$", "."],
  "special_character_count": 2,
  "unique_element_count": 7,
  "contains_duplicates": false,
  "processing_time_ms": 3,
  "summary": {
    "total_elements_received": 9,
    "valid_elements_processed": 7,
    "invalid_elements_ignored": 2
  }
}
```

#### Error Response (400 Bad Request)

```json
{
  "is_success": false,
  "status": 400,
  "error": "Bad Request",
  "message": "data: Data array must not be empty",
  "timestamp": "2026-06-17T10:00:00Z"
}
```

---

## 🏗️ Project Structure

```
src/main/java/com/example/bfhl/
├── BfhlApplication.java            # Spring Boot entry point
├── controller/
│   ├── BfhlController.java         # REST controller (BFHL data processor)
│   └── HealthController.java       # REST controller (Health checker)
├── service/
│   ├── BfhlService.java            # Service interfaces
│   ├── BfhlServiceImpl.java
│   ├── HealthService.java
│   └── HealthServiceImpl.java
├── dto/
│   ├── BfhlRequestDto.java         # DTOs
│   ├── BfhlResponseDto.java
│   └── HealthResponseDto.java
├── util/
│   └── DataProcessor.java          # Stateless utility
└── exception/
    ├── BadRequestException.java     # Exceptions & global handler
    └── GlobalExceptionHandler.java
```

---

## 🏃 Run Locally

### Prerequisites

- **Java 17+** (`java -version`)
- **Maven 3.8+** (`mvn -version`)

### Steps

```bash
# Clone the repo
git clone https://github.com/<your-username>/bfhl-api.git
cd bfhl-api

# Build
mvn clean package

# Run
java -jar target/bfhl-api-0.0.1-SNAPSHOT.jar

# Or use Maven directly
mvn spring-boot:run
```

The API will start on **http://localhost:8080**.

### Quick Test

#### Health Check
```bash
curl -X GET http://localhost:8080/health
```

#### Data Processing
```bash
curl -X POST http://localhost:8080/bfhl \
  -H "Content-Type: application/json" \
  -H "X-Request-Id: test-001" \
  -d '{"data": ["A", "1", "A1B2", "$", "25.5", "-10", "Test123", "", null]}'
```

---

## 🧪 Testing

```bash
# Run all tests (including Unit and Integration tests)
mvn test

# Generate JaCoCo coverage report
mvn verify
```

---

## 🐳 Docker

```bash
# Build image
docker build -t bfhl-api .

# Run container
docker run -p 8080:8080 bfhl-api
```

---

## 🚢 Deployment

Detailed deployment steps for Render, Railway, and AWS are outlined in the instructions below:

- **Render**: Connect GitHub repository, select Docker runtime, and map ports.
- **Railway**: Connect GitHub repository, configure environment variable `SERVER_PORT=$PORT`.
- **AWS**: Push Docker image to ECR, deploy with App Runner.
