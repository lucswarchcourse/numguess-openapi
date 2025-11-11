package edu.luc.cs.numguess.service;

import edu.luc.cs.numguess.domain.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for the GameService.
 * Tests game lifecycle management and best score tracking.
 */
@DisplayName("Game Service")
class GameServiceTest {

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
    }

    @Nested
    @DisplayName("Game Creation and Retrieval")
    class GameCreationAndRetrieval {

        @Test
        @DisplayName("should create a new game")
        void shouldCreateGame() {
            Game game = gameService.createGame();

            assertNotNull(game);
            assertNotNull(game.getId());
        }

        @Test
        @DisplayName("should generate unique UUIDs for each game")
        void shouldGenerateUniqueGameIds() {
            Game game1 = gameService.createGame();
            Game game2 = gameService.createGame();

            assertNotEquals(game1.getId(), game2.getId());
        }

        @Test
        @DisplayName("should retrieve created game by UUID")
        void shouldRetrieveGameByUUID() {
            Game createdGame = gameService.createGame();
            UUID gameId = createdGame.getId();

            Optional<Game> retrievedGame = gameService.getGame(gameId);

            assertTrue(retrievedGame.isPresent());
            assertEquals(gameId, retrievedGame.get().getId());
        }

        @Test
        @DisplayName("should return empty Optional for non-existent game")
        void shouldReturnEmptyForNonExistentGame() {
            UUID nonExistentId = UUID.randomUUID();

            Optional<Game> game = gameService.getGame(nonExistentId);

            assertTrue(game.isEmpty());
        }

        @Test
        @DisplayName("should return same game instance on repeated retrieval")
        void shouldReturnSameGameInstance() {
            Game createdGame = gameService.createGame();
            UUID gameId = createdGame.getId();

            Game retrieved1 = gameService.getGame(gameId).orElseThrow();
            Game retrieved2 = gameService.getGame(gameId).orElseThrow();

            assertSame(retrieved1, retrieved2);
        }
    }

    @Nested
    @DisplayName("Game Deletion")
    class GameDeletion {

        @Test
        @DisplayName("should delete existing game")
        void shouldDeleteGame() {
            Game game = gameService.createGame();
            UUID gameId = game.getId();

            assertTrue(gameService.getGame(gameId).isPresent());

            gameService.deleteGame(gameId);

            assertTrue(gameService.getGame(gameId).isEmpty());
        }

        @Test
        @DisplayName("should not fail when deleting non-existent game")
        void shouldNotFailDeletingNonExistentGame() {
            UUID nonExistentId = UUID.randomUUID();

            // Should not throw
            assertDoesNotThrow(() -> gameService.deleteGame(nonExistentId));
        }

        @Test
        @DisplayName("should decrease total games count after deletion")
        void shouldDecreaseTotalGamesAfterDeletion() {
            Game game = gameService.createGame();
            int countBefore = gameService.getTotalGames();

            gameService.deleteGame(game.getId());
            int countAfter = gameService.getTotalGames();

            assertEquals(countBefore - 1, countAfter);
        }
    }

    @Nested
    @DisplayName("Game Collection Management")
    class GameCollectionManagement {

        @Test
        @DisplayName("should start with zero games")
        void shouldStartWithZeroGames() {
            assertEquals(0, gameService.getTotalGames());
        }

        @Test
        @DisplayName("should increase total games count on creation")
        void shouldIncreaseTotalGamesOnCreation() {
            assertEquals(0, gameService.getTotalGames());

            gameService.createGame();
            assertEquals(1, gameService.getTotalGames());

            gameService.createGame();
            assertEquals(2, gameService.getTotalGames());
        }

        @Test
        @DisplayName("should track multiple games independently")
        void shouldTrackMultipleGames() {
            Game game1 = gameService.createGame();
            Game game2 = gameService.createGame();
            Game game3 = gameService.createGame();

            assertEquals(3, gameService.getTotalGames());

            gameService.deleteGame(game1.getId());

            assertEquals(2, gameService.getTotalGames());
            assertTrue(gameService.getGame(game2.getId()).isPresent());
            assertTrue(gameService.getGame(game3.getId()).isPresent());
        }
    }

    @Nested
    @DisplayName("Best Score Tracking")
    class BestScoreTracking {

        @Test
        @DisplayName("should initialize best score as zero")
        void shouldInitializeBestScoreAsZero() {
            assertEquals(0, gameService.getBestScore());
        }

        @Test
        @DisplayName("should update best score to first completed game")
        void shouldUpdateBestScoreToFirstScore() {
            assertTrue(gameService.updateBestScore(10));
            assertEquals(10, gameService.getBestScore());
        }

        @Test
        @DisplayName("should update best score when new score is lower")
        void shouldUpdateBestScoreOnLowerScore() {
            gameService.updateBestScore(10);
            assertEquals(10, gameService.getBestScore());

            assertTrue(gameService.updateBestScore(5));
            assertEquals(5, gameService.getBestScore());
        }

        @Test
        @DisplayName("should not update best score when new score is higher")
        void shouldNotUpdateBestScoreOnHigherScore() {
            gameService.updateBestScore(5);
            assertEquals(5, gameService.getBestScore());

            assertFalse(gameService.updateBestScore(10));
            assertEquals(5, gameService.getBestScore());
        }

        @Test
        @DisplayName("should return false when updating with same score")
        void shouldReturnFalseOnSameScore() {
            gameService.updateBestScore(5);

            assertFalse(gameService.updateBestScore(5));
            assertEquals(5, gameService.getBestScore());
        }

        @Test
        @DisplayName("should handle multiple score updates correctly")
        void shouldHandleMultipleScoreUpdates() {
            assertTrue(gameService.updateBestScore(20));   // First score
            assertEquals(20, gameService.getBestScore());

            assertFalse(gameService.updateBestScore(25));  // Higher score
            assertEquals(20, gameService.getBestScore());

            assertTrue(gameService.updateBestScore(15));   // Lower score
            assertEquals(15, gameService.getBestScore());

            assertFalse(gameService.updateBestScore(15));  // Same score
            assertEquals(15, gameService.getBestScore());

            assertTrue(gameService.updateBestScore(1));    // Even lower
            assertEquals(1, gameService.getBestScore());
        }

        @Test
        @DisplayName("should persist best score across multiple games")
        void shouldPersistBestScoreAcrossGames() {
            gameService.createGame();
            gameService.updateBestScore(10);

            gameService.createGame();
            assertFalse(gameService.updateBestScore(15));

            gameService.createGame();
            assertTrue(gameService.updateBestScore(5));

            assertEquals(5, gameService.getBestScore());
        }
    }

    @Nested
    @DisplayName("Concurrency Considerations")
    class ConcurrencyConsiderations {

        @Test
        @DisplayName("should handle concurrent best score updates safely")
        void shouldHandleConcurrentScoreUpdates() throws InterruptedException {
            // Simulate concurrent updates
            Thread t1 = new Thread(() -> gameService.updateBestScore(15));
            Thread t2 = new Thread(() -> gameService.updateBestScore(10));
            Thread t3 = new Thread(() -> gameService.updateBestScore(20));

            t1.start();
            t2.start();
            t3.start();

            t1.join();
            t2.join();
            t3.join();

            // Best score should be the minimum
            assertEquals(10, gameService.getBestScore());
        }

        @Test
        @DisplayName("should handle concurrent game creation safely")
        void shouldHandleConcurrentGameCreation() throws InterruptedException {
            int threadCount = 5;
            Thread[] threads = new Thread[threadCount];

            for (int i = 0; i < threadCount; i++) {
                threads[i] = new Thread(gameService::createGame);
                threads[i].start();
            }

            for (Thread t : threads) {
                t.join();
            }

            assertEquals(threadCount, gameService.getTotalGames());
        }
    }
}
