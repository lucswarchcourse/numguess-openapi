package org.openapitools.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SpringDocConfiguration {

    @Bean(name = "org.openapitools.configuration.SpringDocConfiguration.apiInfo")
    OpenAPI apiInfo() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Number Guessing Game API")
                                .description("A RESTful API for a number guessing game.  The game allows players to: - Create new game instances - Submit guesses for a number between 1 and 100 - Track the number of guesses and best scores  ## Richardson Maturity Model Level 3 (HATEOAS)  This API implements **Level 3 of the Richardson Maturity Model** by providing comprehensive hypermedia controls in all JSON responses. Clients can navigate the entire API by following links, without hardcoding URLs or business logic about available actions.  ### Hypermedia Format  The API uses **HAL (Hypertext Application Language)** conventions with `_links` objects containing link relations. Supported media types: - `application/json` - JSON with embedded hypermedia controls - `application/hal+json` - Explicit HAL format - `application/xhtml+xml` - HTML forms (for browser interaction)  ### Standard Link Relations  All responses include a `_links` object with the following standard relations:  - **self**: Link to the current resource - **games**: Link to the games collection - **create-game**: Action link to create a new game (POST) - **submit-guess**: Action link to submit a guess (POST) - only present when game is active - **new-game**: Action link to create a new game - **delete**: Action link to delete the current game (DELETE) - **root**: Link back to API root  ### State-Driven Affordances  Links are **contextual** and change based on application state: - Active games include `submit-guess` link - Completed games omit `submit-guess` link (game is won) - This enables clients to discover available actions without domain knowledge  ### Client Benefits  - **Discoverability**: Start at root (`/`) and follow links to navigate the API - **Decoupling**: No hardcoded URLs - server controls resource URIs - **Evolvability**: Server can change URLs without breaking clients - **Self-documentation**: Links include `method`, `type`, and `title` hints  ### Example Client Flow  ``` 1. GET /                    → Discover _links.games 2. GET /games               → Discover _links.create-game 3. POST /games              → Receive _links.self and _links.submit-guess 4. POST /games/{uuid}       → Follow _links based on result    - If game active: use _links.submit-guess to continue    - If game won: use _links.new-game to play again ``` ")
                                .contact(
                                        new Contact()
                                                .name("Loyola University Chicago COMP 373/473 Software Architecture Course")
                                                .url("https://github.com/lucswarchcourse/numguess-openapi")
                                )
                                .version("1.0.0")
                )
        ;
    }
}