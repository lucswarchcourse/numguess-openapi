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
 * GamesCollectionLinks
 */

@JsonTypeName("GamesCollection__links")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-10T20:40:10.675029-06:00[America/Chicago]", comments = "Generator version: 7.17.0")
public class GamesCollectionLinks {

  private Link self;

  private Link createGame;

  private @Nullable Link root;

  public GamesCollectionLinks() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GamesCollectionLinks(Link self, Link createGame) {
    this.self = self;
    this.createGame = createGame;
  }

  public GamesCollectionLinks self(Link self) {
    this.self = self;
    return this;
  }

  /**
   * Get self
   * @return self
   */
  @NotNull @Valid 
  @Schema(name = "self", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("self")
  public Link getSelf() {
    return self;
  }

  public void setSelf(Link self) {
    this.self = self;
  }

  public GamesCollectionLinks createGame(Link createGame) {
    this.createGame = createGame;
    return this;
  }

  /**
   * Link to create a new game
   * @return createGame
   */
  @NotNull @Valid 
  @Schema(name = "create-game", description = "Link to create a new game", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("create-game")
  public Link getCreateGame() {
    return createGame;
  }

  public void setCreateGame(Link createGame) {
    this.createGame = createGame;
  }

  public GamesCollectionLinks root(@Nullable Link root) {
    this.root = root;
    return this;
  }

  /**
   * Link back to API root
   * @return root
   */
  @Valid 
  @Schema(name = "root", description = "Link back to API root", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("root")
  public @Nullable Link getRoot() {
    return root;
  }

  public void setRoot(@Nullable Link root) {
    this.root = root;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GamesCollectionLinks gamesCollectionLinks = (GamesCollectionLinks) o;
    return Objects.equals(this.self, gamesCollectionLinks.self) &&
        Objects.equals(this.createGame, gamesCollectionLinks.createGame) &&
        Objects.equals(this.root, gamesCollectionLinks.root);
  }

  @Override
  public int hashCode() {
    return Objects.hash(self, createGame, root);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GamesCollectionLinks {\n");
    sb.append("    self: ").append(toIndentedString(self)).append("\n");
    sb.append("    createGame: ").append(toIndentedString(createGame)).append("\n");
    sb.append("    root: ").append(toIndentedString(root)).append("\n");
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

