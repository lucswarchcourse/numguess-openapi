package org.openapitools.api;

import org.openapitools.model.Error;
import org.openapitools.model.GameState;
import org.openapitools.model.GuessResult;
import org.springframework.lang.Nullable;
import java.util.UUID;
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
 * A delegate to be called by the {@link GameApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-10T20:40:10.675029-06:00[America/Chicago]", comments = "Generator version: 7.17.0")
public interface GameApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * DELETE /games/{uuid} : Delete a game instance
     * Deletes the specified game instance identified by its UUID. This removes the game from the collection of active games.  Following REST principles, this operation is idempotent - deleting a non-existent game should return 404, but deleting the same game multiple times has the same effect as deleting it once. 
     *
     * @param uuid The unique identifier (UUID) of the game to delete (required)
     * @return Game deleted successfully (No Content) (status code 204)
     *         or Game not found (status code 404)
     *         or Internal server error (status code 500)
     * @see GameApi#gamesUuidDelete
     */
    default ResponseEntity<Void> gamesUuidDelete(UUID uuid) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"error\" : \"error\", \"status\" : 0 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
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
     * GET /games/{uuid} : Get game representation
     * Returns a form (HTML) or game state representation with hypermedia controls (JSON). The game allows guessing numbers between 1 and 100.  For JSON clients, provides full hypermedia controls including: - Link to submit a guess (if game is still active) - Link to delete the game - Navigation links to related resources - Current game state information  This enables client discoverability of available actions (Level 3 HATEOAS). 
     *
     * @param uuid The unique identifier (UUID) of the game (required)
     * @param accept Content type preference (optional, default to application/xhtml+xml)
     * @return Game representation with hypermedia controls (status code 200)
     *         or Game not found (status code 404)
     *         or Internal server error (status code 500)
     * @see GameApi#gamesUuidGet
     */
    default ResponseEntity<String> gamesUuidGet(UUID uuid,
        String accept) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"error\" : \"error\", \"status\" : 0 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
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
     * POST /games/{uuid} : Submit a guess
     * Submits a guess for the specified game. The guess is compared against the secret number, and a result is returned indicating whether the guess was correct, too high, or too low.  The response includes comprehensive hypermedia controls (Level 3 HATEOAS): - Current game state and guess result - Contextual action links based on game state:   * If game is still active: link to submit another guess   * If game is won: no submit-guess link (demonstrates state-driven affordances) - Navigation links to create new games or return to collection - Number of guesses made so far - Best score tracking across all games  The hypermedia controls enable clients to navigate the application state without hardcoding URLs or business logic about when actions are available. 
     *
     * @param uuid The unique identifier (UUID) of the game (required)
     * @param guess The guessed number (must be between 1 and 100) (required)
     * @param accept Content type preference (optional, default to application/xhtml+xml)
     * @return Guess processed successfully with hypermedia controls (status code 201)
     *         or Game not found (status code 404)
     *         or Bad request (invalid guess format or value) (status code 400)
     *         or Internal server error (status code 500)
     * @see GameApi#gamesUuidPost
     */
    default ResponseEntity<String> gamesUuidPost(UUID uuid,
        Integer guess,
        String accept) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"error\" : \"error\", \"status\" : 0 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"error\" : \"error\", \"status\" : 0 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"error\" : \"error\", \"status\" : 0 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
