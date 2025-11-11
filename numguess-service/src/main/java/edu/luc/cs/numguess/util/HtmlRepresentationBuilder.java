package edu.luc.cs.numguess.util;

import edu.luc.cs.numguess.domain.Game;

import java.util.UUID;

/**
 * Utility for building HTML representations with embedded HATEOAS forms.
 * Converts API resources into browser-renderable HTML with working hyperlinks and forms.
 */
public class HtmlRepresentationBuilder {

    /**
     * Generates HTML for the games collection page.
     * Includes a form to create a new game.
     *
     * @return HTML string
     */
    public static String buildGamesCollectionHtml() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Number Guessing Game</title>
                <style>
                    * {
                        margin: 0;
                        padding: 0;
                        box-sizing: border-box;
                    }
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        min-height: 100vh;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        padding: 20px;
                    }
                    .container {
                        background: white;
                        border-radius: 12px;
                        box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
                        padding: 40px;
                        max-width: 500px;
                        width: 100%;
                    }
                    h1 {
                        color: #333;
                        margin-bottom: 10px;
                        text-align: center;
                    }
                    .subtitle {
                        color: #666;
                        text-align: center;
                        margin-bottom: 30px;
                        font-size: 14px;
                    }
                    .welcome {
                        text-align: center;
                        margin-bottom: 30px;
                    }
                    .welcome p {
                        color: #666;
                        margin-bottom: 20px;
                        line-height: 1.6;
                    }
                    button {
                        width: 100%;
                        padding: 12px 24px;
                        background: #667eea;
                        color: white;
                        border: none;
                        border-radius: 6px;
                        font-size: 16px;
                        font-weight: 600;
                        cursor: pointer;
                        transition: all 0.3s;
                    }
                    button:hover {
                        background: #5568d3;
                        transform: translateY(-2px);
                        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>🎮 Number Guessing Game</h1>
                    <p class="subtitle">Guess the number between 1 and 100</p>
                    <div class="welcome">
                        <p>Welcome to the Number Guessing Game! Can you guess the secret number between 1 and 100?</p>
                        <form method="POST" action="/numguess/games">
                            <button type="submit">Start New Game</button>
                        </form>
                    </div>
                </div>
            </body>
            </html>
            """;
    }

    /**
     * Generates HTML for a game in progress.
     * Displays current game state with a form to submit the next guess.
     *
     * @param uuid the game UUID
     * @param game the game object with current state
     * @return HTML string
     */
    public static String buildGameActiveHtml(UUID uuid, Game game) {
        return String.format("""
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Number Guessing Game - Active</title>
                <style>
                    * {
                        margin: 0;
                        padding: 0;
                        box-sizing: border-box;
                    }
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        min-height: 100vh;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        padding: 20px;
                    }
                    .container {
                        background: white;
                        border-radius: 12px;
                        box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
                        padding: 40px;
                        max-width: 500px;
                        width: 100%;
                    }
                    h1 {
                        color: #333;
                        margin-bottom: 10px;
                        text-align: center;
                    }
                    .subtitle {
                        color: #666;
                        text-align: center;
                        margin-bottom: 30px;
                        font-size: 14px;
                    }
                    .game-status {
                        background: #f5f5f5;
                        border-radius: 8px;
                        padding: 20px;
                        margin-bottom: 20px;
                        text-align: center;
                    }
                    .game-message {
                        color: #333;
                        font-size: 16px;
                        margin-bottom: 15px;
                        line-height: 1.5;
                    }
                    .game-stats {
                        display: grid;
                        grid-template-columns: 1fr 1fr;
                        gap: 15px;
                        margin-top: 15px;
                    }
                    .stat {
                        background: white;
                        padding: 12px;
                        border-radius: 6px;
                        border: 1px solid #e0e0e0;
                    }
                    .stat-label {
                        color: #999;
                        font-size: 12px;
                        text-transform: uppercase;
                        margin-bottom: 5px;
                    }
                    .stat-value {
                        color: #667eea;
                        font-size: 24px;
                        font-weight: bold;
                    }
                    .input-group {
                        display: flex;
                        gap: 10px;
                        margin-bottom: 20px;
                    }
                    input[type="number"] {
                        flex: 1;
                        padding: 12px;
                        border: 2px solid #e0e0e0;
                        border-radius: 6px;
                        font-size: 16px;
                        transition: border-color 0.3s;
                    }
                    input[type="number"]:focus {
                        outline: none;
                        border-color: #667eea;
                    }
                    button {
                        padding: 12px 24px;
                        border: none;
                        border-radius: 6px;
                        font-size: 16px;
                        font-weight: 600;
                        cursor: pointer;
                        transition: all 0.3s;
                    }
                    .btn-primary {
                        background: #667eea;
                        color: white;
                        flex: 1;
                    }
                    .btn-primary:hover {
                        background: #5568d3;
                        transform: translateY(-2px);
                        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
                    }
                    .btn-secondary {
                        background: #f0f0f0;
                        color: #333;
                        width: 100%;
                        margin-top: 10px;
                    }
                    .btn-secondary:hover {
                        background: #e0e0e0;
                    }
                    .link-group {
                        display: flex;
                        gap: 10px;
                        margin-top: 20px;
                        flex-direction: column;
                    }
                    .link-group form {
                        flex: 1;
                    }
                    .link-group button {
                        width: 100%;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>🎮 Number Guessing Game</h1>
                    <p class="subtitle">Guess the number between 1 and 100</p>

                    <div class="game-status">
                        <div class="game-message">Please submit your guess between 1 and 100.</div>
                        <div class="game-stats">
                            <div class="stat">
                                <div class="stat-label">Guesses</div>
                                <div class="stat-value">%d</div>
                            </div>
                        </div>
                    </div>

                    <form method="POST" action="/numguess/games/%s" class="input-group">
                        <input type="number" name="guess" min="1" max="100" placeholder="Enter your guess..." required>
                        <button type="submit" class="btn-primary">Guess</button>
                    </form>

                    <div class="link-group">
                        <form method="POST" action="/numguess/games" style="margin: 0;">
                            <button type="submit" class="btn-secondary">🎮 New Game</button>
                        </form>
                        <form method="GET" action="/numguess/games" style="margin: 0;">
                            <button type="submit" class="btn-secondary">🏠 Back to Games</button>
                        </form>
                    </div>
                </div>
            </body>
            </html>
            """, game.getNumGuesses(), uuid);
    }

    /**
     * Generates HTML for a completed game (won).
     * Displays the result and offers options to play again.
     *
     * @param uuid the game UUID
     * @param numGuesses the number of guesses taken
     * @return HTML string
     */
    public static String buildGameCompleteHtml(UUID uuid, int numGuesses) {
        return String.format("""
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Number Guessing Game - Complete</title>
                <style>
                    * {
                        margin: 0;
                        padding: 0;
                        box-sizing: border-box;
                    }
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        min-height: 100vh;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        padding: 20px;
                    }
                    .container {
                        background: white;
                        border-radius: 12px;
                        box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
                        padding: 40px;
                        max-width: 500px;
                        width: 100%;
                    }
                    h1 {
                        color: #333;
                        margin-bottom: 10px;
                        text-align: center;
                    }
                    .subtitle {
                        color: #666;
                        text-align: center;
                        margin-bottom: 30px;
                        font-size: 14px;
                    }
                    .game-complete {
                        text-align: center;
                        margin-bottom: 30px;
                        background: #d4edda;
                        border: 1px solid #c3e6cb;
                        border-radius: 8px;
                        padding: 20px;
                        color: #155724;
                    }
                    .game-complete p {
                        font-size: 18px;
                        margin-bottom: 10px;
                        line-height: 1.6;
                    }
                    .game-stats {
                        display: grid;
                        grid-template-columns: 1fr;
                        gap: 15px;
                        margin-top: 15px;
                    }
                    .stat {
                        background: white;
                        padding: 12px;
                        border-radius: 6px;
                        border: 1px solid #c3e6cb;
                    }
                    .stat-label {
                        color: #666;
                        font-size: 12px;
                        text-transform: uppercase;
                        margin-bottom: 5px;
                    }
                    .stat-value {
                        color: #155724;
                        font-size: 24px;
                        font-weight: bold;
                    }
                    button {
                        width: 100%;
                        padding: 12px 24px;
                        border: none;
                        border-radius: 6px;
                        font-size: 16px;
                        font-weight: 600;
                        cursor: pointer;
                        transition: all 0.3s;
                        margin-bottom: 10px;
                    }
                    .btn-primary {
                        background: #667eea;
                        color: white;
                    }
                    .btn-primary:hover {
                        background: #5568d3;
                        transform: translateY(-2px);
                        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
                    }
                    .btn-secondary {
                        background: #f0f0f0;
                        color: #333;
                    }
                    .btn-secondary:hover {
                        background: #e0e0e0;
                    }
                    .link-group {
                        display: flex;
                        flex-direction: column;
                        gap: 10px;
                        margin-top: 20px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>🎮 Number Guessing Game</h1>
                    <p class="subtitle">Guess the number between 1 and 100</p>

                    <div class="game-complete">
                        <p>🎉 Congratulations!</p>
                        <p>You guessed the correct number in <strong>%d</strong> tries!</p>
                        <div class="game-stats">
                            <div class="stat">
                                <div class="stat-label">Total Guesses</div>
                                <div class="stat-value">%d</div>
                            </div>
                        </div>
                    </div>

                    <div class="link-group">
                        <form method="POST" action="/numguess/games" style="margin: 0;">
                            <button type="submit" class="btn-primary">🎮 New Game</button>
                        </form>
                        <form method="GET" action="/numguess/games" style="margin: 0;">
                            <button type="submit" class="btn-secondary">🏠 Back to Games</button>
                        </form>
                    </div>
                </div>
            </body>
            </html>
            """, numGuesses, numGuesses);
    }
}
