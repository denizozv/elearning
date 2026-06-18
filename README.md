# E-Learning Platform Backend

A Spring Boot REST API for an online learning platform. Instructors publish paid or
free courses, users browse and purchase them through a cart/checkout flow, and leave
reviews. Courses are organized in a multi-level category tree and offered in a
specific language.

## Tech Stack

- Java 17
- Spring Boot 4.1 (Spring Web MVC, Spring Data JPA, Bean Validation)
- H2 Database (file-based / persistent)
- Lombok
- Spring Security Crypto (BCrypt password hashing only, no full security layer)
- springdoc-openapi (Swagger UI)
- Maven (via the bundled Maven Wrapper)

## Architecture

The project follows a layered architecture:

- `api/controllers` — REST controllers, request/response handling
- `business/abstracts` + `business/concretes` — service interfaces and their
  manager implementations (business logic)
- `business/rules` — entity-level validation and business rule checks
- `business/requests` / `business/responses` — request and response DTOs
- `business/mappers` — entity / DTO mapping
- `dataAccess/abstracts` — Spring Data JPA repositories
- `entities/concretes` + `entities/enums` — JPA entities and enums
- `core/config` — OpenAPI configuration
- `core/exceptions` — global exception handling and error responses
- `core/seed` — initial data seeding on startup

## Domain Modules

`Role`, `Language`, `Category` (self-referencing tree), `Course`, `User`, `Review`,
`Cart`, `CartItem`, `Order`, `OrderItem`, `Payment`.

Key flow: a `Cart` is temporary and mutable; at checkout it is converted into an
immutable `Order` + `OrderItem` (+ `Payment`), course prices are snapshotted, and
the cart is cleared.

For the full data model, fields, relationships and business rules, see
[projectbrain/requirements.md](projectbrain/requirements.md).

## Getting Started

### Prerequisites

- JDK 17 installed (`java -version`)
- No local Maven needed — the project ships with the Maven Wrapper.

### Run

From the project root:

Windows (PowerShell / CMD):

```powershell
.\mvnw.cmd spring-boot:run
```

Linux / macOS / Git Bash:

```bash
./mvnw spring-boot:run
```

Or build and run the jar:

```bash
./mvnw clean package
java -jar target/elearning-0.0.1-SNAPSHOT.jar
```

The application starts on http://localhost:8080. On first startup, sample data is
seeded automatically.

## API Documentation

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

Endpoints are grouped under `/api/v1/`: `roles`, `languages`, `categories`,
`users`, `courses`, `reviews`, `carts`, `orders`, `payments`.

## Database (H2 Console)

Data is persisted to disk at `./data/elearning.mv.db` and survives restarts. The
schema is auto-generated/updated from the entities (`ddl-auto=update`).

- Console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:file:./data/elearning`
- Driver: `org.h2.Driver`
- User: `sa`
- Password: *(empty)*

To reset the database, stop the app, delete the `data/` folder, and restart.

The `data/` folder and `*.mv.db` files are gitignored and not committed.

## Notes

- Passwords are stored as BCrypt hashes and are never returned in any response DTO.
- Validation is split between request DTOs (format/required rules via Bean
  Validation) and `business.rules` (existence, relationship and business logic).
- Errors are returned in a consistent JSON shape via a global exception handler.

For run details and a recommended testing flow, see
[projectbrain/how-to-run.md](projectbrain/how-to-run.md).
