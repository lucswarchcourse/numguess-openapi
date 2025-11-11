package edu.luc.cs.numguess.delegate;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.luc.cs.numguess.domain.Game;
import edu.luc.cs.numguess.service.GameService;
import edu.luc.cs.numguess.util.HateoasLinkBuilder;
import org.openapitools.api.GameApiDelegate;
import org.openapitools.model.Error;
import org.openapitools.model.GameState;
import org.openapitools.model.GameStateLinks;
import org.openapitools.model.GuessResult;
import org.openapitools.model.GuessResultLinks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of GameApiDelegate for individual game operations.
 * Handles:
 * - GET /games/{uuid} - Returns current game state with conditional hypermedia
 * - POST /games/{uuid} - Submits a guess with state-driven affordances
 * - DELETE /games/{uuid} - Deletes a game
 */
@Service
public class GameApiDelegateImpl implements GameApiDelegate {

    private final GameService gameService;
    private final HateoasLinkBuilder linkBuilder;
    private final ObjectMapper objectMapper;
    private NativeWebRequest request;

    @Autowired
    public GameApiDelegateImpl(GameService gameService, HateoasLinkBuilder linkBuilder, ObjectMapper objectMapper) {
        this.gameService = gameService;
        this.linkBuilder = linkBuilder;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    /**
     * GET /games/{uuid} - Returns the current game state.
     * Includes state-driven affordances: submit-guess link only appears if game is active.
     *
     * @param uuid the game UUID
     * @param accept the Accept header value
     * @return GameState with hypermedia controls, or 404 if game not found
     */
    @Override
    public ResponseEntity<String> gamesUuidGet(UUID uuid, String accept) {
        try {
            Optional<Game> gameOpt = gameService.getGame(uuid);

            if (gameOpt.isEmpty()) {
                Error error = new Error();
                error.setError("Game not found");
                error.setStatus(404);
                String jsonError = objectMapper.writeValueAsString(error);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonError);
            }

            Game game = gameOpt.get();

            // Build response
            GameState gameState = new GameState();
            gameState.setGameId(uuid);
            gameState.setNumGuesses(game.getNumGuesses());
            gameState.setActive(game.isActive());
            gameState.setMessage(game.isActive()
                ? "Please submit your guess between 1 and 100."
                : "Game complete! You won.");

            // Build hypermedia links - STATE-DRIVEN AFFORDANCES
            GameStateLinks links = new GameStateLinks();
            links.setSelf(linkBuilder.buildSelfLink(uuid));
            links.setDelete(linkBuilder.buildDeleteGameLink(uuid));
            links.setNewGame(linkBuilder.buildNewGameLink());
            links.setGames(linkBuilder.buildGamesCollectionLink());

            // CRITICAL: Only include submit-guess link if game is active
            if (game.isActive()) {
                links.setSubmitGuess(linkBuilder.buildSubmitGuessLink(uuid));
            }

            gameState.setLinks(links);

            String jsonResponse = objectMapper.writeValueAsString(gameState);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }

    /**
     * POST /games/{uuid} - Submits a guess for the game.
     * Returns comprehensive state with state-driven affordances.
     *
     * @param uuid the game UUID
     * @param guess the guessed number (1-100)
     * @param accept the Accept header value
     * @return GuessResult with hypermedia, or 404/400 on error
     */
    @Override
    public ResponseEntity<String> gamesUuidPost(UUID uuid, Integer guess, String accept) {
        try {
            // Validate guess input
            if (guess == null || guess < 1 || guess > 100) {
                Error error = new Error();
                error.setError("Invalid guess: must be between 1 and 100");
                error.setStatus(400);
                String jsonError = objectMapper.writeValueAsString(error);
                return ResponseEntity.badRequest().body(jsonError);
            }

            // Get the game
            Optional<Game> gameOpt = gameService.getGame(uuid);
            if (gameOpt.isEmpty()) {
                Error error = new Error();
                error.setError("Game not found");
                error.setStatus(404);
                String jsonError = objectMapper.writeValueAsString(error);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonError);
            }

            Game game = gameOpt.get();

            // Submit the guess
            Game.GuessOutcome outcome = game.submitGuess(guess);

            // Build response
            GuessResult result = new GuessResult();
            result.setGameId(uuid);
            result.setGuess(guess);
            result.setNumGuesses(game.getNumGuesses());
            result.setBestScore(gameService.getBestScore());

            // Set result enum and message based on outcome
            switch (outcome) {
                case CORRECT:
                    result.setResult(GuessResult.ResultEnum.CORRECT);
                    result.setMessage("Congratulations! You guessed the correct number in " + game.getNumGuesses() + " tries!");
                    boolean newBestScore = gameService.updateBestScore(game.getNumGuesses());
                    result.setNewBestScore(newBestScore);
                    if (newBestScore) {
                        result.setBestScore(game.getNumGuesses());
                    }
                    break;
                case TOO_LOW:
                    result.setResult(GuessResult.ResultEnum.TOO_LOW);
                    result.setMessage("Your guess is too low. Try a higher number.");
                    result.setNewBestScore(false);
                    break;
                case TOO_HIGH:
                    result.setResult(GuessResult.ResultEnum.TOO_HIGH);
                    result.setMessage("Your guess is too high. Try a lower number.");
                    result.setNewBestScore(false);
                    break;
            }

            // Build hypermedia links - STATE-DRIVEN AFFORDANCES
            GuessResultLinks links = new GuessResultLinks();
            links.setSelf(linkBuilder.buildSelfLink(uuid));
            links.setDelete(linkBuilder.buildDeleteGameLink(uuid));
            links.setNewGame(linkBuilder.buildNewGameLink());
            links.setGames(linkBuilder.buildGamesCollectionLink());

            // CRITICAL: Only include submit-guess link if game is still active
            if (game.isActive()) {
                links.setSubmitGuess(linkBuilder.buildSubmitGuessLink(uuid));
            }

            result.setLinks(links);

            String jsonResponse = objectMapper.writeValueAsString(result);
            return ResponseEntity.status(HttpStatus.CREATED).body(jsonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }

    /**
     * DELETE /games/{uuid} - Deletes a game.
     *
     * @param uuid the game UUID
     * @return 204 No Content if successful, 404 if game not found
     */
    @Override
    public ResponseEntity<Void> gamesUuidDelete(UUID uuid) {
        Optional<Game> gameOpt = gameService.getGame(uuid);

        if (gameOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        gameService.deleteGame(uuid);
        return ResponseEntity.noContent().build();
    }
}
