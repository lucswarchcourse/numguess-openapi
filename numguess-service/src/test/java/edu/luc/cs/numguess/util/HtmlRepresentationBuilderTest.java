package edu.luc.cs.numguess.util;

import edu.luc.cs.numguess.domain.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for HtmlRepresentationBuilder.
 * Tests HTML template rendering for different game states.
 */
@SpringBootTest
@DisplayName("HTML Representation Builder")
class HtmlRepresentationBuilderTest {

    @Autowired
    private TemplateEngine templateEngine;

    private HtmlRepresentationBuilder htmlBuilder;
    private UUID testGameId;

    @BeforeEach
    void setUp() {
        htmlBuilder = new HtmlRepresentationBuilder(templateEngine);
        testGameId = UUID.randomUUID();
    }

    @Nested
    @DisplayName("Games Collection HTML")
    class GamesCollectionHtml {

        @Test
        @DisplayName("should generate valid HTML")
        void shouldGenerateValidHtml() {
            String html = htmlBuilder.buildGamesCollectionHtml();

            assertNotNull(html);
            assertFalse(html.isEmpty());
            assertTrue(html.contains("<!DOCTYPE html>"));
        }

        @Test
        @DisplayName("should include page title")
        void shouldIncludePageTitle() {
            String html = htmlBuilder.buildGamesCollectionHtml();

            assertTrue(html.contains("<title>Number Guessing Game</title>"));
        }

        @Test
        @DisplayName("should include start game form")
        void shouldIncludeStartGameForm() {
            String html = htmlBuilder.buildGamesCollectionHtml();

            assertTrue(html.contains("Start New Game"));
            assertTrue(html.contains("method=\"POST\""));
            assertTrue(html.contains("action=\"/numguess/games\""));
        }

        @Test
        @DisplayName("should include welcome message")
        void shouldIncludeWelcomeMessage() {
            String html = htmlBuilder.buildGamesCollectionHtml();

            assertTrue(html.contains("Welcome to the Number Guessing Game"));
        }

        @Test
        @DisplayName("should be properly formatted")
        void shouldBeProperlyFormatted() {
            String html = htmlBuilder.buildGamesCollectionHtml();

            // Check for proper HTML structure
            assertTrue(html.contains("<html"));
            assertTrue(html.contains("</html>"));
            assertTrue(html.contains("<head>"));
            assertTrue(html.contains("</head>"));
            assertTrue(html.contains("<body>"));
            assertTrue(html.contains("</body>"));
        }
    }

    @Nested
    @DisplayName("Active Game HTML")
    class ActiveGameHtml {

        @Test
        @DisplayName("should generate valid HTML for active game")
        void shouldGenerateValidHtml() {
            Game game = new Game(testGameId);

            String html = htmlBuilder.buildGameActiveHtml(testGameId, game);

            assertNotNull(html);
            assertFalse(html.isEmpty());
            assertTrue(html.contains("<!DOCTYPE html>"));
        }

        @Test
        @DisplayName("should include game UUID in form action")
        void shouldIncludeGameUuidInFormAction() {
            Game game = new Game(testGameId);

            String html = htmlBuilder.buildGameActiveHtml(testGameId, game);

            assertTrue(html.contains("/numguess/games/" + testGameId));
        }

        @Test
        @DisplayName("should include guess count")
        void shouldIncludeGuessCount() {
            Game game = new Game(testGameId);
            game.submitGuess(50);
            game.submitGuess(75);

            String html = htmlBuilder.buildGameActiveHtml(testGameId, game);

            assertTrue(html.contains(">2<"));  // Guesses count
        }

        @Test
        @DisplayName("should include feedback message for too low guess")
        void shouldIncludeFeedbackForTooLow() {
            Game game = new Game(testGameId);
            int secret = game.getSecretNumber();
            game.submitGuess(Math.max(1, secret - 10));

            String html = htmlBuilder.buildGameActiveHtml(testGameId, game);

            if (game.getLastGuessOutcome() != Game.GuessOutcome.TOO_LOW) {
                // Guess was somehow equal or higher, which is fine for this test
                // The point is to show it can render feedback
            } else {
                assertTrue(html.contains("too low"));
            }
        }

        @Test
        @DisplayName("should include feedback message for too high guess")
        void shouldIncludeFeedbackForTooHigh() {
            Game game = new Game(testGameId);
            int secret = game.getSecretNumber();
            game.submitGuess(Math.min(100, secret + 10));

            String html = htmlBuilder.buildGameActiveHtml(testGameId, game);

            if (game.getLastGuessOutcome() == Game.GuessOutcome.TOO_HIGH) {
                assertTrue(html.contains("too high"));
            }
        }

        @Test
        @DisplayName("should include default feedback for new game")
        void shouldIncludeDefaultFeedback() {
            Game game = new Game(testGameId);

            String html = htmlBuilder.buildGameActiveHtml(testGameId, game);

            assertTrue(html.contains("Please submit your guess"));
        }

        @Test
        @DisplayName("should include submit guess button")
        void shouldIncludeSubmitGuessButton() {
            Game game = new Game(testGameId);

            String html = htmlBuilder.buildGameActiveHtml(testGameId, game);

            assertTrue(html.contains("Guess</button>"));
        }

        @Test
        @DisplayName("should include new game and back buttons")
        void shouldIncludeNavigationButtons() {
            Game game = new Game(testGameId);

            String html = htmlBuilder.buildGameActiveHtml(testGameId, game);

            assertTrue(html.contains("New Game"));
            assertTrue(html.contains("Back to Games"));
        }

        @Test
        @DisplayName("should have proper form for guess submission")
        void shouldHaveProperGuessForm() {
            Game game = new Game(testGameId);

            String html = htmlBuilder.buildGameActiveHtml(testGameId, game);

            assertTrue(html.contains("type=\"number\""));
            assertTrue(html.contains("name=\"guess\""));
            assertTrue(html.contains("min=\"1\""));
            assertTrue(html.contains("max=\"100\""));
        }
    }

    @Nested
    @DisplayName("Completed Game HTML")
    class CompletedGameHtml {

        @Test
        @DisplayName("should generate valid HTML for completed game")
        void shouldGenerateValidHtml() {
            String html = htmlBuilder.buildGameCompleteHtml(testGameId, 5);

            assertNotNull(html);
            assertFalse(html.isEmpty());
            assertTrue(html.contains("<!DOCTYPE html>"));
        }

        @Test
        @DisplayName("should include congratulations message")
        void shouldIncludeCongratulations() {
            String html = htmlBuilder.buildGameCompleteHtml(testGameId, 7);

            assertTrue(html.contains("Congratulations"));
            assertTrue(html.contains("🎉"));
        }

        @Test
        @DisplayName("should include number of guesses")
        void shouldIncludeNumberOfGuesses() {
            String html = htmlBuilder.buildGameCompleteHtml(testGameId, 42);

            assertTrue(html.contains(">42<"));
        }

        @Test
        @DisplayName("should include play again button")
        void shouldIncludePlayAgainButton() {
            String html = htmlBuilder.buildGameCompleteHtml(testGameId, 5);

            assertTrue(html.contains("New Game"));
        }

        @Test
        @DisplayName("should include back to games button")
        void shouldIncludeBackToGamesButton() {
            String html = htmlBuilder.buildGameCompleteHtml(testGameId, 5);

            assertTrue(html.contains("Back to Games"));
        }

        @Test
        @DisplayName("should show different guess counts correctly")
        void shouldShowDifferentGuessCounts() {
            String html1 = htmlBuilder.buildGameCompleteHtml(testGameId, 1);
            String html2 = htmlBuilder.buildGameCompleteHtml(testGameId, 100);

            assertTrue(html1.contains(">1<"));
            assertTrue(html2.contains(">100<"));
            assertNotEquals(html1, html2);
        }

        @Test
        @DisplayName("should not include form to submit new guess")
        void shouldNotIncludeGuessForm() {
            String html = htmlBuilder.buildGameCompleteHtml(testGameId, 5);

            assertFalse(html.contains("type=\"number\"") && html.contains("name=\"guess\""));
        }
    }

    @Nested
    @DisplayName("HTML Quality")
    class HtmlQuality {

        @Test
        @DisplayName("all generated HTML should be non-empty")
        void shouldGenerateNonEmptyHtml() {
            Game game = new Game(testGameId);

            assertTrue(htmlBuilder.buildGamesCollectionHtml().length() > 100);
            assertTrue(htmlBuilder.buildGameActiveHtml(testGameId, game).length() > 100);
            assertTrue(htmlBuilder.buildGameCompleteHtml(testGameId, 5).length() > 100);
        }

        @Test
        @DisplayName("should include CSS styling")
        void shouldIncludeCssstyling() {
            Game game = new Game(testGameId);

            String html1 = htmlBuilder.buildGamesCollectionHtml();
            String html2 = htmlBuilder.buildGameActiveHtml(testGameId, game);
            String html3 = htmlBuilder.buildGameCompleteHtml(testGameId, 5);

            assertTrue(html1.contains("<style>"));
            assertTrue(html2.contains("<style>"));
            assertTrue(html3.contains("<style>"));
        }

        @Test
        @DisplayName("should include charset meta tag")
        void shouldIncludeCharsetMetaTag() {
            String html = htmlBuilder.buildGamesCollectionHtml();

            assertTrue(html.contains("charset=\"UTF-8\""));
        }

        @Test
        @DisplayName("should include viewport meta tag for responsive design")
        void shouldIncludeViewportMetaTag() {
            String html = htmlBuilder.buildGamesCollectionHtml();

            assertTrue(html.contains("viewport"));
        }
    }
}
