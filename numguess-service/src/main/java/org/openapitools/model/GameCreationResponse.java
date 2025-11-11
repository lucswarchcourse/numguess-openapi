package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import java.util.UUID;
import org.openapitools.model.GameCreationResponseLinks;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Response containing hypermedia controls for newly created game (HATEOAS Level 3)
 */

@Schema(name = "GameCreationResponse", description = "Response containing hypermedia controls for newly created game (HATEOAS Level 3)")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-10T20:40:10.675029-06:00[America/Chicago]", comments = "Generator version: 7.17.0")
public class GameCreationResponse {

  private UUID gameId;

  @Deprecated
  private @Nullable URI href;

  private @Nullable String message;

  private GameCreationResponseLinks links;

  public GameCreationResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameCreationResponse(UUID gameId, GameCreationResponseLinks links) {
    this.gameId = gameId;
    this.links = links;
  }

  public GameCreationResponse gameId(UUID gameId) {
    this.gameId = gameId;
    return this;
  }

  /**
   * The unique identifier of the newly created game
   * @return gameId
   */
  @NotNull @Valid 
  @Schema(name = "gameId", example = "550e8400-e29b-41d4-a716-446655440000", description = "The unique identifier of the newly created game", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("gameId")
  public UUID getGameId() {
    return gameId;
  }

  public void setGameId(UUID gameId) {
    this.gameId = gameId;
  }

  public GameCreationResponse href(@Nullable URI href) {
    this.href = href;
    return this;
  }

  /**
   * URI of the newly created game resource (deprecated - use _links.self instead)
   * @return href
   * @deprecated
   */
  @Valid 
  @Schema(name = "href", example = "http://localhost:3000/numguess/games/550e8400-e29b-41d4-a716-446655440000", description = "URI of the newly created game resource (deprecated - use _links.self instead)", deprecated = true, requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("href")
  @Deprecated
  public @Nullable URI getHref() {
    return href;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public void setHref(@Nullable URI href) {
    this.href = href;
  }

  public GameCreationResponse message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Success message
   * @return message
   */
  
  @Schema(name = "message", example = "Game created successfully. Submit your first guess!", description = "Success message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public GameCreationResponse links(GameCreationResponseLinks links) {
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
  public GameCreationResponseLinks getLinks() {
    return links;
  }

  public void setLinks(GameCreationResponseLinks links) {
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
    GameCreationResponse gameCreationResponse = (GameCreationResponse) o;
    return Objects.equals(this.gameId, gameCreationResponse.gameId) &&
        Objects.equals(this.href, gameCreationResponse.href) &&
        Objects.equals(this.message, gameCreationResponse.message) &&
        Objects.equals(this.links, gameCreationResponse.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gameId, href, message, links);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameCreationResponse {\n");
    sb.append("    gameId: ").append(toIndentedString(gameId)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
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

