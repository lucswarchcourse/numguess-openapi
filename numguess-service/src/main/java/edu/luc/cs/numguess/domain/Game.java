package edu.luc.cs.numguess.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Internal domain model representing a game instance.
 * Separate from the API model classes in org.openapitools.model.
 */
public class Game {
    private final UUID id;
    private final int secretNumber;
    private final List<Integer> guesses;
    private boolean active;
    private final LocalDateTime createdAt;

    /**
     * Creates a new game with a random secret number between 1 and 100.
     *
     * @param id the UUID for this game
     */
    public Game(UUID id) {
        this.id = id;
        this.secretNumber = new Random().nextInt(100) + 1; // 1-100 inclusive
        this.guesses = new ArrayList<>();
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public int getSecretNumber() {
        return secretNumber;
    }

    public List<Integer> getGuesses() {
        return new ArrayList<>(guesses);
    }

    public int getNumGuesses() {
        return guesses.size();
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the outcome of the last guess, or null if no guesses have been made.
     *
     * @return the outcome of the most recent guess (CORRECT, TOO_LOW, TOO_HIGH), or null
     */
    public GuessOutcome getLastGuessOutcome() {
        if (guesses.isEmpty()) {
            return null;
        }
        int lastGuess = guesses.get(guesses.size() - 1);
        if (lastGuess == secretNumber) {
            return GuessOutcome.CORRECT;
        } else if (lastGuess < secretNumber) {
            return GuessOutcome.TOO_LOW;
        } else {
            return GuessOutcome.TOO_HIGH;
        }
    }

    /**
     * Submits a guess and returns the outcome.
     * Marks the game as inactive if the guess is correct.
     *
     * @param guess the guessed number
     * @return the outcome (CORRECT, TOO_LOW, or TOO_HIGH)
     */
    public GuessOutcome submitGuess(int guess) {
        guesses.add(guess);

        if (guess == secretNumber) {
            active = false;
            return GuessOutcome.CORRECT;
        } else if (guess < secretNumber) {
            return GuessOutcome.TOO_LOW;
        } else {
            return GuessOutcome.TOO_HIGH;
        }
    }

    /**
     * Outcome of a guess submission.
     */
    public enum GuessOutcome {
        CORRECT("correct"),
        TOO_LOW("too_low"),
        TOO_HIGH("too_high");

        private final String apiValue;

        GuessOutcome(String apiValue) {
            this.apiValue = apiValue;
        }

        public String getApiValue() {
            return apiValue;
        }
    }
}
