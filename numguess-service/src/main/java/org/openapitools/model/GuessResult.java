package org.openapitools.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.UUID;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.annotation.Generated;

/**
 * Result of a guess submission with hypermedia controls
 */

@Schema(name = "GuessResult", description = "Result of a guess submission with hypermedia controls")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-10T20:40:10.675029-06:00[America/Chicago]", comments = "Generator version: 7.17.0")
public class GuessResult {

  private UUID gameId;

  private Integer guess;

  /**
   * The outcome of the guess
   */
  public enum ResultEnum {
    CORRECT("correct"),
    
    TOO_HIGH("too_high"),
    
    TOO_LOW("too_low");

    private final String value;

    ResultEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ResultEnum fromValue(String value) {
      for (ResultEnum b : ResultEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ResultEnum result;

  @Deprecated
  private @Nullable Integer comparison;

  private @Nullable String message;

  private Integer numGuesses;

  private Integer bestScore;

  private Boolean newBestScore;

  private GuessResultLinks links;

  public GuessResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GuessResult(UUID gameId, Integer guess, ResultEnum result, Integer numGuesses, Integer bestScore, Boolean newBestScore, GuessResultLinks links) {
    this.gameId = gameId;
    this.guess = guess;
    this.result = result;
    this.numGuesses = numGuesses;
    this.bestScore = bestScore;
    this.newBestScore = newBestScore;
    this.links = links;
  }

  public GuessResult gameId(UUID gameId) {
    this.gameId = gameId;
    return this;
  }

  /**
   * The unique identifier of this game
   * @return gameId
   */
  @NotNull @Valid 
  @Schema(name = "gameId", example = "550e8400-e29b-41d4-a716-446655440000", description = "The unique identifier of this game", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("gameId")
  public UUID getGameId() {
    return gameId;
  }

  public void setGameId(UUID gameId) {
    this.gameId = gameId;
  }

  public GuessResult guess(Integer guess) {
    this.guess = guess;
    return this;
  }

  /**
   * The guessed number that was submitted
   * @return guess
   */
  @NotNull 
  @Schema(name = "guess", example = "42", description = "The guessed number that was submitted", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("guess")
  public Integer getGuess() {
    return guess;
  }

  public void setGuess(Integer guess) {
    this.guess = guess;
  }

  public GuessResult result(ResultEnum result) {
    this.result = result;
    return this;
  }

  /**
   * The outcome of the guess
   * @return result
   */
  @NotNull 
  @Schema(name = "result", example = "correct", description = "The outcome of the guess", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("result")
  public ResultEnum getResult() {
    return result;
  }

  public void setResult(ResultEnum result) {
    this.result = result;
  }

  public GuessResult comparison(@Nullable Integer comparison) {
    this.comparison = comparison;
    return this;
  }

  /**
   * Comparison result (deprecated - use 'result' instead): - 0: guess is correct - positive value: guess is too high (difference from correct answer) - negative value: guess is too low (difference from correct answer) 
   * @return comparison
   * @deprecated
   */
  
  @Schema(name = "comparison", example = "0", description = "Comparison result (deprecated - use 'result' instead): - 0: guess is correct - positive value: guess is too high (difference from correct answer) - negative value: guess is too low (difference from correct answer) ", deprecated = true, requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("comparison")
  @Deprecated
  public @Nullable Integer getComparison() {
    return comparison;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public void setComparison(@Nullable Integer comparison) {
    this.comparison = comparison;
  }

  public GuessResult message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Human-readable feedback on the guess
   * @return message
   */
  
  @Schema(name = "message", example = "Congratulations! You guessed the correct number in 7 tries!", description = "Human-readable feedback on the guess", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public GuessResult numGuesses(Integer numGuesses) {
    this.numGuesses = numGuesses;
    return this;
  }

  /**
   * Total number of guesses made in this game so far
   * minimum: 1
   * @return numGuesses
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "numGuesses", example = "7", description = "Total number of guesses made in this game so far", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("numGuesses")
  public Integer getNumGuesses() {
    return numGuesses;
  }

  public void setNumGuesses(Integer numGuesses) {
    this.numGuesses = numGuesses;
  }

  public GuessResult bestScore(Integer bestScore) {
    this.bestScore = bestScore;
    return this;
  }

  /**
   * Best score (lowest number of guesses) across all games
   * minimum: 1
   * @return bestScore
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "bestScore", example = "5", description = "Best score (lowest number of guesses) across all games", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("bestScore")
  public Integer getBestScore() {
    return bestScore;
  }

  public void setBestScore(Integer bestScore) {
    this.bestScore = bestScore;
  }

  public GuessResult newBestScore(Boolean newBestScore) {
    this.newBestScore = newBestScore;
    return this;
  }

  /**
   * Whether this guess resulted in a new best score (only true when guess is correct and sets a new record)
   * @return newBestScore
   */
  @NotNull 
  @Schema(name = "newBestScore", example = "true", description = "Whether this guess resulted in a new best score (only true when guess is correct and sets a new record)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("newBestScore")
  public Boolean getNewBestScore() {
    return newBestScore;
  }

  public void setNewBestScore(Boolean newBestScore) {
    this.newBestScore = newBestScore;
  }

  public GuessResult links(GuessResultLinks links) {
    this.links = links;
    return this;
  }

  /**
   * Get links
   * @return links
   */
  @NotNull @Valid 
  @Schema(name = "_links", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("_links")
  public GuessResultLinks getLinks() {
    return links;
  }

  public void setLinks(GuessResultLinks links) {
    this.links = links;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuessResult guessResult = (GuessResult) o;
    return Objects.equals(this.gameId, guessResult.gameId) &&
        Objects.equals(this.guess, guessResult.guess) &&
        Objects.equals(this.result, guessResult.result) &&
        Objects.equals(this.comparison, guessResult.comparison) &&
        Objects.equals(this.message, guessResult.message) &&
        Objects.equals(this.numGuesses, guessResult.numGuesses) &&
        Objects.equals(this.bestScore, guessResult.bestScore) &&
        Objects.equals(this.newBestScore, guessResult.newBestScore) &&
        Objects.equals(this.links, guessResult.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gameId, guess, result, comparison, message, numGuesses, bestScore, newBestScore, links);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuessResult {\n");
    sb.append("    gameId: ").append(toIndentedString(gameId)).append("\n");
    sb.append("    guess: ").append(toIndentedString(guess)).append("\n");
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
    sb.append("    comparison: ").append(toIndentedString(comparison)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    numGuesses: ").append(toIndentedString(numGuesses)).append("\n");
    sb.append("    bestScore: ").append(toIndentedString(bestScore)).append("\n");
    sb.append("    newBestScore: ").append(toIndentedString(newBestScore)).append("\n");
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

