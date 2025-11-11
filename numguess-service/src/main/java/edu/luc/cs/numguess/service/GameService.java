package edu.luc.cs.numguess.service;

import edu.luc.cs.numguess.domain.Game;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service layer for game business logic.
 * Manages game lifecycle, state, and global best score tracking.
 * Uses in-memory storage with thread-safe data structures.
 */
@Service
public class GameService {

    private final Map<UUID, Game> games = new ConcurrentHashMap<>();
    private final AtomicInteger bestScore = new AtomicInteger(Integer.MAX_VALUE);

    /**
     * Creates a new game and stores it in memory.
     *
     * @return the newly created Game
     */
    public Game createGame() {
        UUID id = UUID.randomUUID();
        Game game = new Game(id);
        games.put(id, game);
        return game;
    }

    /**
     * Retrieves a game by its UUID.
     *
     * @param id the game UUID
     * @return Optional containing the game if found
     */
    public Optional<Game> getGame(UUID id) {
        return Optional.ofNullable(games.get(id));
    }

    /**
     * Deletes a game from storage.
     *
     * @param id the game UUID
     */
    public void deleteGame(UUID id) {
        games.remove(id);
    }

    /**
     * Gets the total number of active games.
     *
     * @return count of games in storage
     */
    public int getTotalGames() {
        return games.size();
    }

    /**
     * Gets the current best score (lowest number of guesses).
     *
     * @return best score, or 0 if no games have been completed
     */
    public int getBestScore() {
        int current = bestScore.get();
        return current == Integer.MAX_VALUE ? 0 : current;
    }

    /**
     * Updates the best score if the provided score is better (lower).
     *
     * @param score the number of guesses in a completed game
     * @return true if this is a new best score, false otherwise
     */
    public boolean updateBestScore(int score) {
        int oldValue;
        int newValue;
        do {
            oldValue = bestScore.get();
            newValue = Math.min(oldValue, score);
        } while (!bestScore.compareAndSet(oldValue, newValue));

        return newValue == score && (oldValue == Integer.MAX_VALUE || score < oldValue);
    }
}
