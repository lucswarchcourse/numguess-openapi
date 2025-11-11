package edu.luc.cs.numguess.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for the Game domain model.
 * Tests game creation, state transitions, and guess outcomes.
 */
@DisplayName("Game Domain Model")
class GameTest {

    private UUID testGameId;
    private Game game;

    @BeforeEach
    void setUp() {
        testGameId = UUID.randomUUID();
        game = new Game(testGameId);
    }

    @Nested
    @DisplayName("Game Creation")
    class GameCreation {

        @Test
        @DisplayName("should create game with assigned UUID")
        void shouldCreateGameWithUUID() {
            assertEquals(testGameId, game.getId());
        }

        @Test
        @DisplayName("should initialize game as active")
        void shouldInitializeAsActive() {
            assertTrue(game.isActive());
        }

        @Test
        @DisplayName("should start with zero guesses")
        void shouldStartWithZeroGuesses() {
            assertEquals(0, game.getNumGuesses());
        }

        @Test
        @DisplayName("should have secret number between 1 and 100")
        void shouldHaveValidSecretNumber() {
            int secret = game.getSecretNumber();
            assertTrue(secret >= 1 && secret <= 100, "Secret number should be between 1 and 100, got: " + secret);
        }

        @Test
        @DisplayName("should have non-null creation timestamp")
        void shouldHaveCreationTimestamp() {
            assertNotNull(game.getCreatedAt());
        }

        @Test
        @DisplayName("should return defensive copy of guesses")
        void shouldReturnDefensiveCopyOfGuesses() {
            var guesses1 = game.getGuesses();
            var guesses2 = game.getGuesses();

            assertNotSame(guesses1, guesses2, "Should return different list instances");
            assertEquals(guesses1, guesses2, "But with same contents");
        }
    }

    @Nested
    @DisplayName("Guess Submission")
    class GuessSubmission {

        @Test
        @DisplayName("should accept valid guess between 1 and 100")
        void shouldAcceptValidGuess() {
            Game.GuessOutcome outcome = game.submitGuess(50);

            assertNotNull(outcome);
            assertEquals(1, game.getNumGuesses());
        }

        @Test
        @DisplayName("should return correct outcome when guess equals secret")
        void shouldReturnCorrectOutcome() {
            int secret = game.getSecretNumber();
            Game.GuessOutcome outcome = game.submitGuess(secret);

            assertEquals(Game.GuessOutcome.CORRECT, outcome);
        }

        @Test
        @DisplayName("should return too low when guess is less than secret")
        void shouldReturnTooLowOutcome() {
            int secret = game.getSecretNumber();
            int guess = Math.max(1, secret - 1);

            Game.GuessOutcome outcome = game.submitGuess(guess);

            if (guess < secret) {
                assertEquals(Game.GuessOutcome.TOO_LOW, outcome);
            }
        }

        @Test
        @DisplayName("should return too high when guess is greater than secret")
        void shouldReturnTooHighOutcome() {
            int secret = game.getSecretNumber();
            int guess = Math.min(100, secret + 1);

            Game.GuessOutcome outcome = game.submitGuess(guess);

            if (guess > secret) {
                assertEquals(Game.GuessOutcome.TOO_HIGH, outcome);
            }
        }

        @Test
        @DisplayName("should increment guess count after submission")
        void shouldIncrementGuessCount() {
            assertEquals(0, game.getNumGuesses());

            game.submitGuess(50);
            assertEquals(1, game.getNumGuesses());

            game.submitGuess(60);
            assertEquals(2, game.getNumGuesses());
        }

        @Test
        @DisplayName("should track all submitted guesses")
        void shouldTrackAllGuesses() {
            game.submitGuess(10);
            game.submitGuess(50);
            game.submitGuess(75);

            var guesses = game.getGuesses();
            assertEquals(3, guesses.size());
            assertTrue(guesses.contains(10));
            assertTrue(guesses.contains(50));
            assertTrue(guesses.contains(75));
        }
    }

    @Nested
    @DisplayName("Game State Transitions")
    class GameStateTransitions {

        @Test
        @DisplayName("should mark game inactive when correct guess submitted")
        void shouldMarkInactiveOnCorrectGuess() {
            int secret = game.getSecretNumber();

            assertTrue(game.isActive());
            game.submitGuess(secret);
            assertFalse(game.isActive());
        }

        @Test
        @DisplayName("should remain active after incorrect guess")
        void shouldRemainActiveAfterIncorrectGuess() {
            int secret = game.getSecretNumber();
            int wrongGuess = (secret == 50) ? 25 : 50;

            assertTrue(game.isActive());
            game.submitGuess(wrongGuess);
            assertTrue(game.isActive());
        }

        @Test
        @DisplayName("should allow multiple guesses until correct")
        void shouldAllowMultipleGuessesTilCorrect() {
            int secret = game.getSecretNumber();

            // Submit several wrong guesses
            for (int i = 1; i < secret && i < 100; i += 10) {
                game.submitGuess(i);
                assertTrue(game.isActive());
            }

            // Submit correct guess
            game.submitGuess(secret);
            assertFalse(game.isActive());
        }
    }

    @Nested
    @DisplayName("Last Guess Outcome")
    class LastGuessOutcome {

        @Test
        @DisplayName("should return null when no guesses made")
        void shouldReturnNullWithoutGuesses() {
            assertNull(game.getLastGuessOutcome());
        }

        @Test
        @DisplayName("should return outcome of most recent guess")
        void shouldReturnLastOutcome() {
            int secret = game.getSecretNumber();

            // Submit first guess
            Game.GuessOutcome outcome1 = game.submitGuess(1);
            assertEquals(outcome1, game.getLastGuessOutcome());

            // Submit second guess
            Game.GuessOutcome outcome2 = game.submitGuess(100);
            assertEquals(outcome2, game.getLastGuessOutcome());
        }

        @Test
        @DisplayName("should correctly identify correct guess as last outcome")
        void shouldIdentifyCorrectGuessAsLastOutcome() {
            int secret = game.getSecretNumber();

            game.submitGuess(50);
            game.submitGuess(secret);

            assertEquals(Game.GuessOutcome.CORRECT, game.getLastGuessOutcome());
        }
    }

    @Nested
    @DisplayName("GuessOutcome Enum")
    class GuessOutcomeEnum {

        @Test
        @DisplayName("CORRECT should have api value 'correct'")
        void correctOutcomeApiValue() {
            assertEquals("correct", Game.GuessOutcome.CORRECT.getApiValue());
        }

        @Test
        @DisplayName("TOO_LOW should have api value 'too_low'")
        void tooLowOutcomeApiValue() {
            assertEquals("too_low", Game.GuessOutcome.TOO_LOW.getApiValue());
        }

        @Test
        @DisplayName("TOO_HIGH should have api value 'too_high'")
        void tooHighOutcomeApiValue() {
            assertEquals("too_high", Game.GuessOutcome.TOO_HIGH.getApiValue());
        }

        @Test
        @DisplayName("should convert all outcomes to api values")
        void shouldConvertAllOutcomesToApiValues() {
            for (Game.GuessOutcome outcome : Game.GuessOutcome.values()) {
                assertNotNull(outcome.getApiValue());
                assertFalse(outcome.getApiValue().isEmpty());
            }
        }
    }
}
