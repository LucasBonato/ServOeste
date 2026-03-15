default:
  @just --list

# Load environment variables from .env if present
set dotenv-load := true

app-port := "8080"

# Run the Spring Boot application (local dev)
[group("app")]
run:
  ./mvnw spring-boot:run

# Build the project (skips tests)
[group("app")]
build:
  ./mvnw -DskipTests package

# Run all tests (unit + integration + architecture)
[group("test")]
test:
  ./mvnw test

# Run only architecture tests (ArchUnit)
[group("test")]
test-arch:
  ./mvnw -Dtest=CleanArchitectureTest test

# Run tests for application services only
[group("test")]
test-app:
  ./mvnw -Dtest='*ServiceTest' test

# Run tests for domain entities only
[group("test")]
test-domain:
  ./mvnw -Dtest='*DomainTest,*EntityTest' test

# Run the full Maven verify lifecycle
[group("lifecycle")]
verify:
  ./mvnw verify

# Run lint/format if you have format plugins configured in pom.xml
# (safe default: just re-run compile to surface style issues)
[group("lifecycle")]
lint:
  ./mvnw compile

# Start full docker stack (MySQL + app + Aspire dashboard)
[group("container")]
docker-up:
  docker compose up --build

# Stop docker stack
[group("container")]
docker-down:
  docker compose down

# Tail application logs from docker container
[group("container")]
docker-logs:
  docker logs -f serv-oeste-server

# Open Swagger UI in the browser (Unix-like environments)
[group("app")]
swagger:
  xdg-open "http://localhost:{{app-port}}/swagger" || start "http://localhost:{{app-port}}/swagger" || open "http://localhost:{{app-port}}/swagger"

