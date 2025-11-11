package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.openapitools.model.Link;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GameCreationResponseLinks
 */

@JsonTypeName("GameCreationResponse__links")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-10T20:40:10.675029-06:00[America/Chicago]", comments = "Generator version: 7.17.0")
public class GameCreationResponseLinks {

  private Link self;

  private Link submitGuess;

  private Link delete;

  private Link games;

  public GameCreationResponseLinks() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameCreationResponseLinks(Link self, Link submitGuess, Link delete, Link games) {
    this.self = self;
    this.submitGuess = submitGuess;
    this.delete = delete;
    this.games = games;
  }

  public GameCreationResponseLinks self(Link self) {
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

  public GameCreationResponseLinks submitGuess(Link submitGuess) {
    this.submitGuess = submitGuess;
    return this;
  }

  /**
   * Action link to submit a guess
   * @return submitGuess
   */
  @NotNull @Valid 
  @Schema(name = "submit-guess", description = "Action link to submit a guess", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("submit-guess")
  public Link getSubmitGuess() {
    return submitGuess;
  }

  public void setSubmitGuess(Link submitGuess) {
    this.submitGuess = submitGuess;
  }

  public GameCreationResponseLinks delete(Link delete) {
    this.delete = delete;
    return this;
  }

  /**
   * Action link to delete this game
   * @return delete
   */
  @NotNull @Valid 
  @Schema(name = "delete", description = "Action link to delete this game", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("delete")
  public Link getDelete() {
    return delete;
  }

  public void setDelete(Link delete) {
    this.delete = delete;
  }

  public GameCreationResponseLinks games(Link games) {
    this.games = games;
    return this;
  }

  /**
   * Link back to games collection
   * @return games
   */
  @NotNull @Valid 
  @Schema(name = "games", description = "Link back to games collection", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("games")
  public Link getGames() {
    return games;
  }

  public void setGames(Link games) {
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
    GameCreationResponseLinks gameCreationResponseLinks = (GameCreationResponseLinks) o;
    return Objects.equals(this.self, gameCreationResponseLinks.self) &&
        Objects.equals(this.submitGuess, gameCreationResponseLinks.submitGuess) &&
        Objects.equals(this.delete, gameCreationResponseLinks.delete) &&
        Objects.equals(this.games, gameCreationResponseLinks.games);
  }

  @Override
  public int hashCode() {
    return Objects.hash(self, submitGuess, delete, games);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameCreationResponseLinks {\n");
    sb.append("    self: ").append(toIndentedString(self)).append("\n");
    sb.append("    submitGuess: ").append(toIndentedString(submitGuess)).append("\n");
    sb.append("    delete: ").append(toIndentedString(delete)).append("\n");
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

