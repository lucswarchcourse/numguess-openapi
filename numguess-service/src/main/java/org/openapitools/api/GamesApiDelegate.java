package org.openapitools.api;

import org.openapitools.model.ApiRoot;
import org.openapitools.model.Error;
import org.openapitools.model.GameCreationResponse;
import org.openapitools.model.GamesCollection;
import org.springframework.lang.Nullable;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;

/**
 * A delegate to be called by the {@link GamesApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-10T20:40:10.675029-06:00[America/Chicago]", comments = "Generator version: 7.17.0")
public interface GamesApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /games : Get welcome page or games collection
     * Returns a welcome page (HTML) or games collection representation with hypermedia controls (JSON). For HTML clients, this displays a form to start a new game. For JSON clients, provides discoverable actions and navigation links (Level 3 HATEOAS). 
     *
     * @param accept Content type preference (optional, default to application/xhtml+xml)
     * @return Welcome page or games collection representation with hypermedia controls (status code 200)
     * @see GamesApi#gamesGet
     */
    default ResponseEntity<String> gamesGet(String accept) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * POST /games : Create a new game
     * Creates a new number guessing game instance and returns hypermedia controls for interaction. The game is initialized with a random number between 1 and 100. A UUID is generated to uniquely identify the new game instance.  Following Level 3 HATEOAS principles, the response includes comprehensive hypermedia links for all available actions: submitting guesses, deleting the game, or creating another game. 
     *
     * @param accept Content type preference (optional, default to application/xhtml+xml)
     * @return Game created successfully with hypermedia controls (status code 201)
     *         or Internal server error (status code 500)
     * @see GamesApi#gamesPost
     */
    default ResponseEntity<String> gamesPost(String accept) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"error\" : \"error\", \"status\" : 0 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * GET / : Get API root
     * Returns the API root resource with hypermedia controls (Level 3 HATEOAS). This endpoint serves as the entry point to the API, providing discoverable links to all top-level resources.  For HTML clients, may return HTTP 303 See Other redirect to &#x60;/games&#x60;. For JSON clients, returns hypermedia representation with navigational links. 
     *
     * @param accept Content type preference (optional, default to application/json)
     * @return API root representation with hypermedia controls (status code 200)
     *         or See Other - Redirect to /games (for HTML clients) (status code 303)
     * @see GamesApi#rootGet
     */
    default ResponseEntity<ApiRoot> rootGet(String accept) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/hal+json"))) {
                    String exampleString = "Custom MIME type example not yet supported: application/hal+json";
                    ApiUtil.setExampleResponse(request, "application/hal+json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"_links\" : { \"games\" : { \"templated\" : false, \"method\" : \"GET\", \"href\" : \"https://openapi-generator.tech\", \"type\" : \"type\", \"title\" : \"title\" }, \"self\" : { \"templated\" : false, \"method\" : \"GET\", \"href\" : \"https://openapi-generator.tech\", \"type\" : \"type\", \"title\" : \"title\" } }, \"message\" : \"Welcome to the Number Guessing Game API\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
