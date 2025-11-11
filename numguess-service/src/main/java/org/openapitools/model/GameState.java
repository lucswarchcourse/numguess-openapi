package org.openapitools.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.annotation.Generated;

/**
 * Current state of a game with hypermedia controls
 */

@Schema(name = "GameState", description = "Current state of a game with hypermedia controls")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-10T20:40:10.675029-06:00[America/Chicago]", comments = "Generator version: 7.17.0")
public class GameState {

  private UUID gameId;

  private Integer numGuesses;

  private Boolean active;

  private @Nullable String message;

  private GameStateLinks links;

  public GameState() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameState(UUID gameId, Integer numGuesses, Boolean active, GameStateLinks links) {
    this.gameId = gameId;
    this.numGuesses = numGuesses;
    this.active = active;
    this.links = links;
  }

  public GameState gameId(UUID gameId) {
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

  public GameState numGuesses(Integer numGuesses) {
    this.numGuesses = numGuesses;
    return this;
  }

  /**
   * Number of guesses made so far
   * minimum: 0
   * @return numGuesses
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "numGuesses", example = "3", description = "Number of guesses made so far", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("numGuesses")
  public Integer getNumGuesses() {
    return numGuesses;
  }

  public void setNumGuesses(Integer numGuesses) {
    this.numGuesses = numGuesses;
  }

  public GameState active(Boolean active) {
    this.active = active;
    return this;
  }

  /**
   * Whether the game is still active (not won)
   * @return active
   */
  @NotNull 
  @Schema(name = "active", example = "true", description = "Whether the game is still active (not won)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("active")
  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public GameState message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Instructional message for the player
   * @return message
   */
  
  @Schema(name = "message", example = "Please submit your guess between 1 and 100.", description = "Instructional message for the player", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public GameState links(GameStateLinks links) {
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
  public GameStateLinks getLinks() {
    return links;
  }

  public void setLinks(GameStateLinks links) {
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
    GameState gameState = (GameState) o;
    return Objects.equals(this.gameId, gameState.gameId) &&
        Objects.equals(this.numGuesses, gameState.numGuesses) &&
        Objects.equals(this.active, gameState.active) &&
        Objects.equals(this.message, gameState.message) &&
        Objects.equals(this.links, gameState.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gameId, numGuesses, active, message, links);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameState {\n");
    sb.append("    gameId: ").append(toIndentedString(gameId)).append("\n");
    sb.append("    numGuesses: ").append(toIndentedString(numGuesses)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

