# Hotel Property View API

REST API for storing, searching, and aggregating hotel information.

## Technology Stack

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Hibernate ORM
- Liquibase
- PostgreSQL
- H2 Database for the development profile and regular tests
- MapStruct
- Lombok
- Springdoc OpenAPI / Swagger UI
- Maven
- JUnit 5, Mockito, and Testcontainers
- Docker Compose

## Links

The application runs on port `8092`.

- API base URL: `http://localhost:8092/property-view`
- Swagger UI: `http://localhost:8092/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8092/v3/api-docs`
- H2 Console, available only with the `dev` profile: `http://localhost:8092/h2-console`

## Profiles

Common settings are defined in `src/main/resources/application.yaml`.

```yaml
spring:
  profiles:
    default: dev
server:
  port: 8092
```

If no profile is specified explicitly, the application starts with the `dev` profile.

### DEVELOPMENT

Profile configuration: `src/main/resources/application-dev.yaml`.

Behavior:

- Uses the in-memory H2 database at `jdbc:h2:mem:hoteldb`.
- Creates the database schema.
- Loads demo data from `db/changelog/test-data`.
- Enables SQL logging through `spring.jpa.show-sql=true`.
- Enables the H2 Console.

Startup:

```powershell
mvn spring-boot:run
```

### PRODUCTION

Profile configuration: `src/main/resources/application-prod.yaml`.

Behavior:

- Uses PostgreSQL at `jdbc:postgresql://localhost:5432/hotels`.
- Creates the database schema.
- Does not load demo data.
- Disables the H2 Console.
- Uses `ddl-auto=validate`, so Hibernate validates the schema without creating it.

Start PostgreSQL before running the application with the `prod` profile:

```powershell
docker compose up -d postgres
```

Start the application with the `prod` profile:

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=prod"
```

## Tests

The regular test suite uses H2:

```powershell
mvn test
```
