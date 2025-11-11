package org.openapitools.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.annotation.Generated;

/**
 * GameStateLinks
 */

@JsonTypeName("GameState__links")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-10T20:40:10.675029-06:00[America/Chicago]", comments = "Generator version: 7.17.0")
public class GameStateLinks {

  private Link self;

  private @Nullable Link submitGuess;

  private @Nullable Link delete;

  private @Nullable Link newGame;

  private @Nullable Link games;

  public GameStateLinks() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameStateLinks(Link self) {
    this.self = self;
  }

  public GameStateLinks self(Link self) {
    this.self = self;
    return this;
  }

  /**
   * Link to this game resource
   * @return self
   */
  @NotNull @Valid 
  @Schema(name = "self", description = "Link to this game resource", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("self")
  public Link getSelf() {
    return self;
  }

  public void setSelf(Link self) {
    this.self = self;
  }

  public GameStateLinks submitGuess(@Nullable Link submitGuess) {
    this.submitGuess = submitGuess;
    return this;
  }

  /**
   * Action link to submit a guess (only present if game is active)
   * @return submitGuess
   */
  @Valid 
  @Schema(name = "submit-guess", description = "Action link to submit a guess (only present if game is active)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("submit-guess")
  public @Nullable Link getSubmitGuess() {
    return submitGuess;
  }

  public void setSubmitGuess(@Nullable Link submitGuess) {
    this.submitGuess = submitGuess;
  }

  public GameStateLinks delete(@Nullable Link delete) {
    this.delete = delete;
    return this;
  }

  /**
   * Action link to delete this game
   * @return delete
   */
  @Valid 
  @Schema(name = "delete", description = "Action link to delete this game", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("delete")
  public @Nullable Link getDelete() {
    return delete;
  }

  public void setDelete(@Nullable Link delete) {
    this.delete = delete;
  }

  public GameStateLinks newGame(@Nullable Link newGame) {
    this.newGame = newGame;
    return this;
  }

  /**
   * Action link to create a new game
   * @return newGame
   */
  @Valid 
  @Schema(name = "new-game", description = "Action link to create a new game", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new-game")
  public @Nullable Link getNewGame() {
    return newGame;
  }

  public void setNewGame(@Nullable Link newGame) {
    this.newGame = newGame;
  }

  public GameStateLinks games(@Nullable Link games) {
    this.games = games;
    return this;
  }

  /**
   * Link back to games collection
   * @return games
   */
  @Valid 
  @Schema(name = "games", description = "Link back to games collection", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("games")
  public @Nullable Link getGames() {
    return games;
  }

  public void setGames(@Nullable Link games) {
    this.games = games;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameStateLinks gameStateLinks = (GameStateLinks) o;
    return Objects.equals(this.self, gameStateLinks.self) &&
        Objects.equals(this.submitGuess, gameStateLinks.submitGuess) &&
        Objects.equals(this.delete, gameStateLinks.delete) &&
        Objects.equals(this.newGame, gameStateLinks.newGame) &&
        Objects.equals(this.games, gameStateLinks.games);
  }

  @Override
  public int hashCode() {
    return Objects.hash(self, submitGuess, delete, newGame, games);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameStateLinks {\n");
    sb.append("    self: ").append(toIndentedString(self)).append("\n");
    sb.append("    submitGuess: ").append(toIndentedString(submitGuess)).append("\n");
    sb.append("    delete: ").append(toIndentedString(delete)).append("\n");
    sb.append("    newGame: ").append(toIndentedString(newGame)).append("\n");
    sb.append("    games: ").append(toIndentedString(games)).append("\n");
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

