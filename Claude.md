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

## References

- [Java 21 Language Features](https://www.oracle.com/java/technologies/javase/21-relnotes.html)
- [Records in Java](https://docs.oracle.com/javase/17/language/records.html)
- [Local Variable Type Inference](https://openjdk.java.net/jeps/286)
- [Sealed Classes](https://openjdk.java.net/jeps/409)
- [Pattern Matching](https://openjdk.java.net/jeps/427)
- [SOLID Principles](https://www.baeldung.com/solid-principles)
- Spring Framework Best Practices
