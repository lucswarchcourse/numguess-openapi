package edu.luc.cs.numguess.util;

import edu.luc.cs.numguess.domain.Game;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;

/**
 * Component for building HTML representations using Thymeleaf templates.
 * Converts API resources into browser-renderable HTML with working HATEOAS forms.
 *
 * Benefits of template-based approach:
 * - HTML templates are maintainable and separated from Java code
 * - IDE support for HTML/CSS (syntax highlighting, validation, autocomplete)
 * - Design tools can work directly with templates without Java knowledge
 * - Clean separation of presentation logic from business logic
 * - Dynamic variable substitution via Thymeleaf instead of string concatenation
 * - Template reuse and inheritance capabilities for consistent styling
 */
@Component
public class HtmlRepresentationBuilder {

    private final TemplateEngine templateEngine;

    public HtmlRepresentationBuilder(final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Generates HTML for the games collection page.
     * Includes a form to create a new game.
     *
     * @return HTML string
     */
    public String buildGamesCollectionHtml() {
        final var context = new Context();
        return templateEngine.process("games-collection", context);
    }

    /**
     * Generates HTML for a game in progress.
     * Displays current game state with a form to submit the next guess.
     * Includes feedback message based on the last guess outcome.
     *
     * @param uuid the game UUID
     * @param game the game object with current state
     * @return HTML string
     */
    public String buildGameActiveHtml(final UUID uuid, final Game game) {
        final var feedbackMessage = buildFeedbackMessage(game.getLastGuessOutcome());

        final var context = new Context();
        context.setVariable("uuid", uuid);
        context.setVariable("numGuesses", game.getNumGuesses());
        context.setVariable("feedbackMessage", feedbackMessage);

        return templateEngine.process("game-active", context);
    }

    /**
     * Generates HTML for a completed game (won).
     * Displays the result and offers options to play again.
     *
     * @param uuid the game UUID
     * @param numGuesses the number of guesses taken
     * @return HTML string
     */
    public String buildGameCompleteHtml(final UUID uuid, final int numGuesses) {
        final var context = new Context();
        context.setVariable("numGuesses", numGuesses);

        return templateEngine.process("game-complete", context);
    }

    /**
     * Builds a feedback message based on the last guess outcome.
     * Returns a human-readable message with emoji for visual feedback.
     *
     * @param lastOutcome the outcome of the last guess, or null if no guesses made
     * @return feedback message string
     */
    private String buildFeedbackMessage(final Game.GuessOutcome lastOutcome) {
        if (lastOutcome == null) {
            return "Please submit your guess between 1 and 100.";
        }

        return switch (lastOutcome) {
            case TOO_LOW -> "📈 Your guess is too low. Try a higher number.";
            case TOO_HIGH -> "📉 Your guess is too high. Try a lower number.";
            case CORRECT -> "🎉 Correct! You won!";
        };
    }
}
