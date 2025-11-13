# Claude Code Development Guidelines

This document outlines the code style, architecture, and best practices for this project.

## Java Modernization Standards

This project uses **Java 25** with modern language features and APIs. All code must follow these standards:

### 1. Final Correctness

**Rule:** All formal method parameters and local variables must be declared `final`.

**Rationale:**
- Prevents accidental reassignment
- Documents developer intent
- Enables compiler optimizations
- Makes code semantics clearer

**Examples:**

```java
// ✅ CORRECT: All parameters and variables are final
public String buildGameActiveHtml(final UUID uuid, final Game game) {
    final String feedbackMessage = buildFeedbackMessage(game.getLastGuessOutcome());
    final Context context = new Context();
    context.setVariable("uuid", uuid);
    context.setVariable("numGuesses", game.getNumGuesses());
    context.setVariable("feedbackMessage", feedbackMessage);
    return templateEngine.process("game-active", context);
}

// ❌ WRONG: Missing final keywords
public String buildGameActiveHtml(UUID uuid, Game game) {
    String feedbackMessage = buildFeedbackMessage(game.getLastGuessOutcome());
    Context context = new Context();
    // ...
}
```

**Exception:** Do NOT add `final` to:
- Instance/static class fields (adds noise without benefit in this context)
- Lambda parameters (implied final in lambdas)
- Exception caught variables (not applicable for final)

### 2. Local Type Inference with `var`

**Rule:** Use `var` for local variable declarations whenever the type is clear from context.

**Rationale:**
- Reduces verbosity and improves readability
- Type is still fully inferred by compiler
- Forces meaningful variable names
- Reduces coupling to specific types

**Examples:**

```java
// ✅ CORRECT: Type is obvious from right-hand side
var feedbackMessage = buildFeedbackMessage(game.getLastGuessOutcome());
var context = new Context();
var uuid1 = UUID.randomUUID();
var href = link.getHref().toString();
var game = gameService.createGame();

// ❌ WRONG: Type is NOT obvious
var x = buildFeedbackMessage(game.getLastGuessOutcome());  // 'x' is meaningless
var data = stream.filter(g -> g.isActive()).collect(toList());  // unclear what data is

// ✅ CORRECT: Complex types benefit from var
var games = IntStream.range(0, 10)
    .mapToObj(i -> gameService.createGame())
    .collect(Collectors.toList());
```

**When NOT to use `var`:**
- Method return types (use explicit types to document API contracts)
- Lambda parameters (can use `final var` for consistency if needed)
- Loop variables with complex initializers (clarity is better than brevity)

### 3. No Wildcard Imports

**Rule:** Never use wildcard imports (`import java.util.*;`). Import only the classes you need.

**Rationale:**
- Explicit imports make dependencies clear
- Avoids name collision surprises
- IDE can easily identify unused imports
- Easier to refactor and understand dependencies

**Examples:**

```java
// ✅ CORRECT: Explicit imports
import java.util.UUID;
import java.util.Optional;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

// ❌ WRONG: Wildcard imports
import java.util.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.*;
```

**How to fix:**
- Let your IDE auto-organize imports (IntelliJ: Code → Optimize Imports)
- Add imports on-demand as you code
- Use IDE shortcuts to add missing imports automatically

### 4. Java 21 Language Features

#### Records (for immutable data classes)

Use `record` instead of classes with only getters:

```java
// ✅ CORRECT: Use record for immutable data
public record GameSnapshot(UUID id, int numGuesses, boolean isComplete) {}

// ❌ WRONG: Old-style immutable class
public class GameSnapshot {
    private final UUID id;
    private final int numGuesses;
    private final boolean isComplete;

    public GameSnapshot(UUID id, int numGuesses, boolean isComplete) {
        this.id = id;
        this.numGuesses = numGuesses;
        this.isComplete = isComplete;
    }

    public UUID id() { return id; }
    public int numGuesses() { return numGuesses; }
    public boolean isComplete() { return isComplete; }
}
```

#### Pattern Matching in Switch Statements

Use sealed classes and pattern matching:

```java
// ✅ CORRECT: Pattern matching
var outcome = switch(guess) {
    case int g when g < secret -> GuessOutcome.TOO_LOW;
    case int g when g > secret -> GuessOutcome.TOO_HIGH;
    default -> GuessOutcome.CORRECT;
};

// Use pattern matching for instanceof
if (game instanceof Game g) {
    int secret = g.getSecretNumber();
}
```

#### Sealed Classes

Use sealed classes to restrict inheritance:

```java
// ✅ CORRECT: Sealed class restricts hierarchy
public sealed interface GuessOutcome permits TOO_LOW, TOO_HIGH, CORRECT {}

public record TOO_LOW() implements GuessOutcome {}
public record TOO_HIGH() implements GuessOutcome {}
public record CORRECT() implements GuessOutcome {}
```

#### Text Blocks

Use text blocks for multi-line strings:

```java
// ✅ CORRECT: Text block for multi-line HTML
var html = """
    <!DOCTYPE html>
    <html>
        <body>Welcome</body>
    </html>
    """;

// ❌ WRONG: Concatenated strings
var html = "<!DOCTYPE html>" +
    "<html>" +
    "<body>Welcome</body>" +
    "</html>";
```

#### Virtual Threads (Project Loom)

Use virtual threads for concurrent operations:

```java
// ✅ CORRECT: Virtual thread for concurrent task
var threadFactory = Thread.ofVirtual().factory();
try (var executor = Executors.newThreadPerTaskExecutor(threadFactory)) {
    executor.submit(() -> gameService.createGame());
}
```

### 5. Immutability and Defensive Programming

**Rule:** Prefer immutable types and defensive copying.

**Examples:**

```java
// ✅ CORRECT: Immutable collections
final var games = Collections.unmodifiableMap(new HashMap<>(gameMap));
final var scores = List.of(1, 2, 3);  // Java 9+ immutable list

// ❌ WRONG: Mutable collections exposed
public List<Game> getAllGames() {
    return gameList;  // Direct exposure, can be modified
}

// ✅ CORRECT: Defensive copy
public List<Game> getAllGames() {
    return Collections.unmodifiableList(new ArrayList<>(gameList));
}
```

### 6. Null Handling with Optional

**Rule:** Use `Optional` instead of null for optional values.

**Examples:**

```java
// ✅ CORRECT: Use Optional
final var game = gameService.getGame(gameId);
if (game.isPresent()) {
    game.ifPresent(g -> processGame(g));
}

// ❌ WRONG: Nullable references
public Game getGame(UUID id) {
    return gameMap.get(id);  // Returns null
}

// ✅ CORRECT: Return Optional
public Optional<Game> getGame(final UUID id) {
    return Optional.ofNullable(gameMap.get(id));
}
```

### 7. Dependency Injection with Spring

**Rule:** Always use constructor injection with final fields.

**Examples:**

```java
// ✅ CORRECT: Constructor injection with final field
@Component
public class HtmlRepresentationBuilder {
    private final TemplateEngine templateEngine;

    public HtmlRepresentationBuilder(final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
}

// ❌ WRONG: Field injection (not final, harder to test)
@Component
public class HtmlRepresentationBuilder {
    @Autowired
    private TemplateEngine templateEngine;
}
```

---

## Code Organization

### Project Structure

```
numguess-service/
├── src/main/java/edu/luc/cs/numguess/
│   ├── OpenApiGeneratorApplication.java     # Spring Boot entry point
│   ├── api/                                  # Generated OpenAPI delegates
│   │   ├── GameApi.java
│   │   ├── GamesApi.java
│   │   └── impl/                             # Custom implementations
│   │       ├── GameApiDelegateImpl.java
│   │       └── GamesApiDelegateImpl.java
│   ├── domain/                               # Business logic
│   │   └── Game.java
│   ├── service/                              # Service layer
│   │   └── GameService.java
│   └── util/                                 # Utilities
│       ├── HateoasLinkBuilder.java
│       ├── HtmlRepresentationBuilder.java
│       ├── ErrorResponseBuilder.java
│       └── ContentNegotiationUtil.java
├── src/main/resources/
│   ├── static/css/                           # CSS files
│   │   ├── variables.css
│   │   ├── common.css
│   │   └── game.css
│   ├── templates/                            # Thymeleaf templates
│   │   ├── games-collection.html
│   │   ├── game-active.html
│   │   └── game-complete.html
│   ├── application.properties
│   └── openapi.yaml
└── src/test/java/edu/luc/cs/numguess/
    ├── domain/
    │   └── GameTest.java
    ├── service/
    │   └── GameServiceTest.java
    └── util/
        ├── HateoasLinkBuilderTest.java
        ├── HtmlRepresentationBuilderTest.java
        └── ContentNegotiationUtilTest.java
```

### File Naming Conventions

- **Classes:** PascalCase (e.g., `GameService`, `HtmlRepresentationBuilder`)
- **Methods:** camelCase (e.g., `createGame`, `buildGameActiveHtml`)
- **Constants:** UPPER_SNAKE_CASE (e.g., `MAX_GUESS_COUNT`)
- **Packages:** lowercase.dot.separated (e.g., `edu.luc.cs.numguess.util`)

---

## Testing Standards

### Test Organization

Use nested test classes for organization:

```java
@DisplayName("Game Service")
class GameServiceTest {

    @Nested
    @DisplayName("Game Creation and Retrieval")
    class GameCreationAndRetrieval {
        @Test
        @DisplayName("should create a new game")
        void shouldCreateGame() {
            // Test implementation
        }
    }
}
```

### Test Naming

- Test class: `<Class>Test` (e.g., `GameServiceTest`)
- Test method: `should<ExpectedBehavior>` (e.g., `shouldCreateGame`)
- Display names: Descriptive English phrases

### Assertions

- Use JUnit 5 assertions from `org.junit.jupiter.api.Assertions`
- Prefer `assertTrue`/`assertFalse` for boolean conditions
- Use `assertNotNull` for null checks
- Use `assertEquals` for value comparisons
- Use `assertThrows` for exception testing

---

## SOLID Principles and Design

### Single Responsibility Principle

Each class should have one reason to change:

```java
// ✅ CORRECT: Each class has a single responsibility
class HtmlRepresentationBuilder {        // Responsibility: HTML rendering
    // ... render HTML templates
}

class GameService {                       // Responsibility: Game lifecycle management
    // ... manage games
}

class ContentNegotiationUtil {           // Responsibility: Content type detection
    // ... detect Accept header
}
```

### Dependency Inversion Principle

Depend on abstractions, not concrete implementations:

```java
// ✅ CORRECT: Depend on TemplateEngine (abstraction)
public HtmlRepresentationBuilder(final TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
}

// ❌ WRONG: Depend on concrete Thymeleaf class
public HtmlRepresentationBuilder(final ThymeleafEngine engine) {
    // ...
}
```

### Don't Repeat Yourself (DRY)

Extract common logic into reusable utilities:

```java
// ✅ CORRECT: Content negotiation logic in one place
public static boolean isHtmlRequest(final String accept) {
    return accept != null &&
        (accept.contains("text/html") || accept.contains("application/xhtml+xml"));
}

// ✅ CORRECT: Error responses centralized
public ErrorResponse notFound(final String message) {
    var error = new Error();
    error.setError(message);
    error.setStatus(404);
    return error;
}
```

---

## Performance Considerations

### Static Resource Reuse

- Use static `final` for expensive objects that don't change:
  ```java
  private static final Random RANDOM = new Random();
  private static final ObjectMapper MAPPER = new ObjectMapper();
  ```

### Caching

- Static CSS files leverage browser caching
- CSS variables reduce need for dynamic styling
- HTTP cache headers should be configured in Spring

### Immutability

- Immutable objects are thread-safe
- Enable compiler optimizations
- Reduce memory footprint

---

## Documentation

### Javadoc Guidelines

```java
/**
 * Builds an HTML representation of the active game state.
 *
 * @param uuid the unique identifier of the game
 * @param game the game instance with current state
 * @return the rendered HTML as a String
 * @throws IllegalArgumentException if uuid or game is null
 */
public String buildGameActiveHtml(final UUID uuid, final Game game) {
    // Implementation
}
```

### Comments

- Prefer self-documenting code over comments
- Comments should explain "why", not "what"
- Keep comments concise and up-to-date

```java
// ✅ CORRECT: Explains intent
// Use static Random to avoid seed collision with rapid game creation
private static final Random RANDOM = new Random();

// ❌ WRONG: Obvious from code
// Create a new random object
final var random = new Random();
```

---

## Continuous Improvement

### Code Review Checklist

- [ ] All method parameters and local variables are `final`
- [ ] Using `var` for local type inference where appropriate
- [ ] No wildcard imports
- [ ] Following SOLID principles
- [ ] Proper error handling with exceptions
- [ ] Tests cover happy path and edge cases
- [ ] Documentation is clear and current
- [ ] Performance considerations addressed

### Future Java Features to Watch

- Pattern matching for records (Java 21+)
- Virtual threads for concurrent code
- Structured concurrency APIs
- Foreign Function & Memory API

---

## Continuous Integration and Delivery (CI/CD)

This project uses **GitHub Actions** for automated build, test, and deployment pipelines.

### Workflow Overview

#### 1. **CI Workflow** (`.github/workflows/ci.yml`)
Runs on every push and pull request to `main` and `develop` branches.

**What it does:**
- Compiles code with Maven (`mvn clean compile`)
- Runs full test suite (130 tests)
- Generates JaCoCo code coverage reports
- Uploads test results and coverage to artifacts
- Publishes test report as GitHub check

**Trigger:** Push to main/develop, Pull requests
**Status:** Required for merge

#### 2. **Code Quality Workflow** (`.github/workflows/code-quality.yml`)
Runs automated code analysis tools on every commit.

**Tools and Analysis:**

| Tool | Purpose | Configuration |
|------|---------|---------------|
| **SpotBugs** | Detect potential bugs | Effort: Max, Threshold: Medium |
| **PMD** | Code quality issues | Quickstart ruleset |
| **Checkstyle** | Code style enforcement | Google code style (10.14.2) |
| **Spotless** | Code formatting | Google Java Format (1.23.0) |
| **SonarCloud** | Deep static analysis | Requires SONAR_TOKEN secret |

**Artifacts Generated:**
- SpotBugs report (`spotbugsXml.xml`)
- PMD report (`pmd.xml`)
- Checkstyle report (`checkstyle-result.xml`)
- SonarCloud dashboard (if configured)

**Configuration Notes:**
- All quality checks run with `continue-on-error: true` to avoid blocking PRs
- Reports are uploaded as GitHub artifacts for review
- SonarCloud integration requires `SONAR_TOKEN` secret in GitHub repository settings

#### 3. **Dependency Check Workflow** (`.github/workflows/dependency-check.yml`)
Monitors dependencies for security vulnerabilities and updates.

**What it checks:**

| Check | Frequency | Purpose |
|-------|-----------|---------|
| **OWASP Dependency Check** | On push | Scans for known vulnerabilities |
| **Maven Vulnerability Check** | On push | Uses Maven plugin for CVE detection |
| **License Compliance** | On push | Generates THIRD-PARTY license report |
| **Dependency Updates** | Daily (2 AM UTC) | Checks for available updates |

**Reports Generated:**
- OWASP Dependency Check JSON report
- Maven Dependency Check HTML report
- License compliance report (THIRD-PARTY.txt)
- Dependency and plugin update recommendations

#### 4. **Release Workflow** (`.github/workflows/release.yml`)
Automates release process when tags are pushed.

**Triggers:** Pushing a tag matching `v*` (e.g., `v1.0.0`)

**What it does:**
1. **Build & Test:** Runs full Maven build with tests
2. **Create GitHub Release:**
   - Extracts version from tag
   - Uploads compiled JAR file
   - Creates release notes in GitHub
3. **Build Docker Image:** (Optional)
   - Builds Docker container
   - Pushes to GitHub Container Registry (ghcr.io)
   - Tags with version and `latest`
4. **Publish Documentation:**
   - Generates Javadoc
   - Deploys to GitHub Pages

**Manual Trigger:**
Use `workflow_dispatch` to trigger manually with custom version:
```bash
gh workflow run release.yml -f version=1.0.0
```

### Setup Instructions

#### Enable SonarCloud Integration
1. Create account at https://sonarcloud.io
2. Add SONAR_TOKEN to GitHub repository secrets:
   - Go to Settings → Secrets and variables → Actions
   - Add secret: `SONAR_TOKEN` (from SonarCloud account)
   - Create `sonar-project.properties` in root:
     ```properties
     sonar.projectKey=numguess-openapi
     sonar.organization=your-org-key
     ```

#### Enable Docker Publishing
1. Ensure GitHub token has `write:packages` permission
2. Workflows automatically push to `ghcr.io/${{ github.repository }}`
3. Create Dockerfile in `numguess-service/` for custom image builds

#### Enable GitHub Pages for Documentation
1. Go to Settings → Pages
2. Set source to "GitHub Actions"
3. Release workflow automatically deploys Javadoc

### Maven Plugins for CI/CD

All CI/CD functionality is powered by Maven plugins configured in `pom.xml`:

| Plugin | Version | Purpose |
|--------|---------|---------|
| JaCoCo | 0.8.11 | Code coverage reporting |
| SpotBugs | 4.8.5.1 | Bug detection |
| PMD | 3.22.0 | Code quality analysis |
| Checkstyle | 3.3.1 | Code style enforcement |
| Spotless | 2.42.0 | Code formatting (Google Java Format) |
| License Maven | 2.4.0 | License compliance |
| Versions Maven | 2.17.0 | Dependency update checks |
| Dependency Check | 9.2.0 | OWASP vulnerability scanning |
| SonarCloud | 3.11.0.2033 | Deep static analysis |

**Run locally:**
```bash
cd numguess-service

# Code coverage
mvn clean test jacoco:report

# Static analysis
mvn spotbugs:check
mvn pmd:check
mvn checkstyle:check

# Code formatting (check only)
mvn spotless:check

# Apply formatting (auto-fix)
mvn spotless:apply

# Dependency analysis
mvn dependency:tree
mvn org.owasp:dependency-check-maven:check
mvn license:aggregate-add-third-party
mvn versions:display-dependency-updates

# SonarCloud (requires SONAR_TOKEN)
mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar
```

### GitHub Secrets Required

For full CI/CD functionality, configure these secrets in GitHub repository settings:

| Secret | Used By | How to Obtain |
|--------|---------|---------------|
| `GITHUB_TOKEN` | All workflows | Automatic (GitHub-provided) |
| `SONAR_TOKEN` | SonarCloud | https://sonarcloud.io (optional) |

### Viewing Results

**Test Results:** GitHub Actions → CI workflow → "Publish Test Report" step
**Coverage:** See `target/site/jacoco/index.html` after local test run
**Quality Reports:** GitHub Actions artifacts tab
**SonarCloud:** Dashboard at https://sonarcloud.io (if configured)
**Releases:** GitHub Releases page

### Best Practices

1. **Before pushing code:**
   ```bash
   mvn clean test spotless:check spotbugs:check
   ```

2. **Before committing:**
   - Fix formatting: `mvn spotless:apply`
   - Run tests: `mvn test`
   - Check for bugs: `mvn spotbugs:check`

3. **For pull requests:**
   - Ensure all CI checks pass
   - Review SonarCloud quality gate (if configured)
   - Aim for code coverage ≥ 85%

4. **For releases:**
   - Create annotated tag: `git tag -a v1.0.0 -m "Release 1.0.0"`
   - Push tag: `git push origin v1.0.0`
   - GitHub Actions automatically creates release

---

## Azure Deployment

This project is fully configured for deployment to Microsoft Azure using multiple deployment methods.

### Quick Start

```bash
# Deploy with one command
./deploy-to-azure.sh numguess-api
```

### Deployment Methods

| Method | Time | Cost | Best For |
|--------|------|------|----------|
| **Quick Script** | 5-10 min | Free-B1 | Learning, demos |
| **Maven Plugin** | 5-10 min | Free-B1 | Java developers |
| **Docker** | 15-20 min | B1-S1 | Production, scalability |
| **Infrastructure as Code** | 10-15 min | Flexible | Enterprise, compliance |

### Key Files

**Deployment:**
- `Dockerfile` - Multi-stage container build (50 lines)
- `.dockerignore` - Docker optimization
- `deploy-to-azure.sh` - Automated deployment script (executable)
- `azure-app-service.json` - ARM template for infrastructure
- `pom.xml` - Azure Web Apps Maven plugin configured

**Configuration:**
- `application.properties` - Azure-ready with PORT environment variable
- Health check endpoint: `/actuator/health`
- Java runtime: Java 25 (both App Service and Docker)
- Auto-scaling: Configurable tier (F1 free to S3 premium)

### Prerequisites

- Azure subscription (free tier available)
- Azure CLI 2.50+ (`az --version`)
- Maven 3.6+ (`mvn --version`)
- Docker (optional, for container deployments)

### Deployment Step-by-Step

**1. Login to Azure**
```bash
az login
```

**2. Run Deployment**
```bash
# Uses defaults: Free tier, eastus region
./deploy-to-azure.sh numguess-api

# Custom configuration
./deploy-to-azure.sh numguess-api my-resource-group eastus B1
```

**3. Access Application**
```bash
https://numguess-api.azurewebsites.net
```

### What Gets Deployed

**Infrastructure (Azure):**
- Resource Group (logical container)
- App Service Plan (compute resources, Linux)
- App Service (web application host)
- Managed Identity (secure authentication)
- Health checks configured
- Logging enabled

**Application (Spring Boot):**
- JAR packaged as standalone application
- Spring Boot default port 8080
- Actuator health endpoint for monitoring
- Production profile active

### Post-Deployment Commands

```bash
# View logs
az webapp log tail --resource-group numguess-rg --name numguess-api

# Check health
curl https://numguess-api.azurewebsites.net/actuator/health

# Access Swagger UI
https://numguess-api.azurewebsites.net/swagger-ui.html

# Stop application
az webapp stop --resource-group numguess-rg --name numguess-api

# Start application
az webapp start --resource-group numguess-rg --name numguess-api

# Delete resources (cleanup)
az group delete --name numguess-rg
```

### Configuration Options

**Pricing Tiers:**
- `F1` - Free (limited to 60 min/day)
- `B1` - Basic (~$12/month)
- `B2` - Standard (~$50/month)
- `S1` - Premium (~$100+/month)

**Regions:**
- `eastus` - Default
- `westus`, `westus2`, `centralus`, `northeurope`, etc.

**Environment Variables:**
- `PORT` - HTTP port (default 8080)
- `SPRING_PROFILES_ACTIVE` - Spring profile (default: production)
- `JAVA_OPTS` - JVM options (default: -Xmx512m -Xms256m)

### Docker Deployment

For container-based deployment:

```bash
# Build image
docker build -t numguess-api:1.0.0 ./numguess-service

# Run locally
docker run -p 8080:8080 numguess-api:1.0.0

# Push to Azure Container Registry
az acr create --resource-group numguess-rg --name myregistry --sku Basic
docker tag numguess-api:1.0.0 myregistry.azurecr.io/numguess-api:1.0.0
docker push myregistry.azurecr.io/numguess-api:1.0.0

# Deploy from registry
az webapp create --resource-group numguess-rg \
  --plan numguess-plan \
  --name numguess-api \
  --deployment-container-image-name myregistry.azurecr.io/numguess-api:1.0.0
```

### Monitoring

**Azure Portal:**
- CPU usage, memory usage, request metrics
- Real-time logs and diagnostic settings
- Application Insights integration

**CLI Commands:**
```bash
# CPU usage
az monitor metrics list --resource numguess-api \
  --resource-group numguess-rg --metric CpuPercentage

# Memory usage
az monitor metrics list --resource numguess-api \
  --resource-group numguess-rg --metric MemoryPercentage
```

### Cost Estimation (Monthly)

| Tier | Compute | Storage | Monthly Cost |
|------|---------|---------|--------------|
| F1 (Free) | Shared | Shared | $0 (limited) |
| B1 | 1.75GB RAM | 10GB | ~$12 |
| B2 | 3.5GB RAM | 50GB | ~$50 |
| S1 | 1.75GB RAM | 50GB | ~$50 |

### Troubleshooting

**Application won't start:**
```bash
az webapp log tail --resource-group numguess-rg --name numguess-api
```

**Port binding error:**
Ensure `server.port=${PORT:8080}` in `application.properties`

**Memory issues:**
```bash
# Scale up
az appservice plan update --name numguess-plan \
  --resource-group numguess-rg --sku B1

# Adjust JVM
az webapp config appsettings set \
  --resource-group numguess-rg --name numguess-api \
  --settings JAVA_OPTS="-Xmx1024m"
```

### Additional Resources

See [AZURE_DEPLOYMENT.md](AZURE_DEPLOYMENT.md) for:
- Detailed deployment guides for each method
- Advanced configuration options
- Continuous deployment setup
- Performance optimization
- Troubleshooting guide

### References

- [Azure Deployment Guide](AZURE_DEPLOYMENT.md)
- [Azure App Service Documentation](https://learn.microsoft.com/en-us/azure/app-service/)
- [Spring Boot on Azure](https://learn.microsoft.com/en-us/azure/developer/java/spring-framework/)
- [Azure CLI Reference](https://learn.microsoft.com/en-us/cli/azure/reference-index)
- [Java on Azure](https://learn.microsoft.com/en-us/azure/developer/java/)

---

## References

- [Java 21 Language Features](https://www.oracle.com/java/technologies/javase/21-relnotes.html)
- [Records in Java](https://docs.oracle.com/javase/17/language/records.html)
- [Local Variable Type Inference](https://openjdk.java.net/jeps/286)
- [Sealed Classes](https://openjdk.java.net/jeps/409)
- [Pattern Matching](https://openjdk.java.net/jeps/427)
- [SOLID Principles](https://www.baeldung.com/solid-principles)
- Spring Framework Best Practices
