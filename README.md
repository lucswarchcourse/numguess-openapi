# Number Guessing Game - API First

## Background

This project started from a very old example built with the Restlet library. The [original code](https://github.com/LoyolaChicagoCode/numguess-restlet-spring-java) was primarily an educational artifact for introducing REST and HATEOAS concepts.

## How we recovered the API spec

We used Claude to reverse‑engineer the OpenAPI description from the legacy Restlet example. The resulting specification lives in `openapi.yaml` at the repo root and now reflects:

- A root (`/`) entrypoint with guidance to link or redirect to `/games`
- A games collection at `/games` that issues UUIDs on creation
- Game instance operations at `/games/{uuid}` (including guess submission and deletion)

## API‑first approach

We’re trying to take an API‑first approach:

- Treat `openapi.yaml` as the single source of truth for the service contract
- Evolve behavior in the spec first; generate and/or implement code from it
- Keep clients, docs, and tests aligned via code generation and examples derived from the spec

The API documentation is generated using `redocly` and available [here](doc/redoc-static.html); you can prefix the documentation page with `https://htmlpreview.github.io/?` for it to render properly.

## Implementation Strategy

We're using OpenAPI Generator to create a Spring Boot server stub directly from `openapi.yaml`, then implementing the business logic in custom delegate classes. This approach keeps generated and manual code cleanly separated:

- **Generated Code** (read-only, regenerable): Interfaces, model classes, and base structure in `org.openapitools.*` packages
- **Manual Implementation** (maintained by developers): Business logic and delegates in `edu.luc.cs.numguess.*` packages
- **Separation by Package**: Clear boundaries make it obvious which code is auto-generated vs. hand-written

### Regenerating the Spring Boot Service

If you modify `openapi.yaml` and need to regenerate the service boilerplate:

```bash
# Install the OpenAPI Generator CLI (if not already installed)
npm install -g @openapitools/openapi-generator-cli

# Regenerate the Spring Boot service from the OpenAPI spec
openapi-generator-cli generate \
  -i openapi.yaml \
  -g spring-boot \
  -o numguess-service \
  -c openapitools.json
```

The `openapitools.json` configuration file controls generator behavior. After regeneration, the service will be ready to build and run.

### Project Structure

- `openapi.yaml` - API specification (source of truth)
- `openapitools.json` - OpenAPI Generator configuration
- `numguess-service/` - Spring Boot application
  - `src/main/java/org/openapitools/` - Generated code (interfaces, models, controllers)
  - `src/main/java/edu/luc/cs/numguess/` - Custom implementation code
    - `delegate/` - Implementations of generated `*ApiDelegate` interfaces
    - `domain/` - Business domain model classes (e.g., `Game.java`)
    - `service/` - Business logic and service layer (e.g., `GameService.java`)
    - `util/` - Utility classes (e.g., `HateoasLinkBuilder.java`)

### Building and Running

```bash
cd numguess-service

# Build the project
./mvnw clean package

# Run the Spring Boot application
./mvnw spring-boot:run

# Access the Swagger UI
# http://localhost:8080/swagger-ui.html
```

## Status

The API specification is complete with Level 3 HATEOAS support (state-driven affordances). The Spring Boot implementation includes:
- Core domain model with game logic
- Service layer for game management
- Delegate implementations with HATEOAS link assembly
- Full error handling and validation

