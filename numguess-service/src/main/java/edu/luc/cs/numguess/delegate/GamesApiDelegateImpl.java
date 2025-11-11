package edu.luc.cs.numguess.delegate;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.luc.cs.numguess.service.GameService;
import edu.luc.cs.numguess.util.HateoasLinkBuilder;
import edu.luc.cs.numguess.util.HtmlRepresentationBuilder;
import org.openapitools.api.GamesApiDelegate;
import org.openapitools.model.GameCreationResponse;
import org.openapitools.model.GameCreationResponseLinks;
import org.openapitools.model.GamesCollection;
import org.openapitools.model.GamesCollectionLinks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.URI;
import java.util.Optional;

/**
 * Implementation of GamesApiDelegate for collection-level operations.
 * Handles:
 * - GET /games - Returns games collection with hypermedia links
 * - POST /games - Creates a new game with full HATEOAS controls
 */
@Service
public class GamesApiDelegateImpl implements GamesApiDelegate {

    private final GameService gameService;
    private final HateoasLinkBuilder linkBuilder;
    private final ObjectMapper objectMapper;
    private NativeWebRequest request;

    @Autowired
    public GamesApiDelegateImpl(GameService gameService, HateoasLinkBuilder linkBuilder, ObjectMapper objectMapper) {
        this.gameService = gameService;
        this.linkBuilder = linkBuilder;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    /**
     * GET /games - Returns games collection representation.
     * Supports content negotiation via Accept header.
     * Returns HTML for browsers, JSON for API clients.
     *
     * @param accept the Accept header value
     * @return GamesCollection with hypermedia controls as JSON string, or HTML page
     */
    @Override
    public ResponseEntity<String> gamesGet(String accept) {
        try {
            // Content negotiation: check if browser is requesting HTML
            if (accept != null && (accept.contains("text/html") || accept.contains("application/xhtml+xml"))) {
                return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.TEXT_HTML)
                    .body(HtmlRepresentationBuilder.buildGamesCollectionHtml());
            }

            // Default to JSON response for API clients
            GamesCollection collection = new GamesCollection();
            collection.setMessage("Welcome to the Number Guessing Game! Create a new game to start playing.");
            collection.setTotalGames(gameService.getTotalGames());

            // Build hypermedia links
            GamesCollectionLinks links = new GamesCollectionLinks();
            links.setSelf(linkBuilder.buildGamesCollectionLink());
            links.setCreateGame(linkBuilder.buildCreateGameLink());
            links.setRoot(linkBuilder.buildApiRootLink());

            collection.setLinks(links);

            String jsonResponse = objectMapper.writeValueAsString(collection);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    /**
     * POST /games - Creates a new game.
     * For HTML clients (browsers), redirects to the game page.
     * For JSON clients (API), returns GameCreationResponse with full hypermedia controls.
     *
     * @param accept the Accept header value
     * @return GameCreationResponse with full hypermedia controls as JSON string, or redirect to game page
     */
    @Override
    public ResponseEntity<String> gamesPost(String accept) {
        try {
            // Create the game
            var game = gameService.createGame();

            // Content negotiation: check if browser is requesting HTML
            if (accept != null && (accept.contains("text/html") || accept.contains("application/xhtml+xml"))) {
                // For browsers, redirect to the game page which will display HTML
                return ResponseEntity.status(HttpStatus.SEE_OTHER)
                    .location(URI.create("/numguess/games/" + game.getId()))
                    .build();
            }

            // Default to JSON response for API clients
            GameCreationResponse response = new GameCreationResponse();
            response.setGameId(game.getId());
            response.setMessage("Game created successfully. Submit your first guess!");

            // Build hypermedia links - game is always active at creation
            GameCreationResponseLinks links = new GameCreationResponseLinks();
            links.setSelf(linkBuilder.buildSelfLink(game.getId()));
            links.setSubmitGuess(linkBuilder.buildSubmitGuessLink(game.getId()));
            links.setDelete(linkBuilder.buildDeleteGameLink(game.getId()));
            links.setGames(linkBuilder.buildGamesCollectionLink());

            response.setLinks(links);

            String jsonResponse = objectMapper.writeValueAsString(response);

            // Set Location header per REST conventions
            return ResponseEntity
                .created(URI.create("/games/" + game.getId()))
                .body(jsonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
