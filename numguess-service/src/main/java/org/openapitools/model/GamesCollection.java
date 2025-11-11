package org.openapitools.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.annotation.Generated;

/**
 * Games collection resource with hypermedia controls
 */

@Schema(name = "GamesCollection", description = "Games collection resource with hypermedia controls")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-10T20:40:10.675029-06:00[America/Chicago]", comments = "Generator version: 7.17.0")
public class GamesCollection {

  private @Nullable String message;

  private @Nullable Integer totalGames;

  private GamesCollectionLinks links;

  public GamesCollection() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GamesCollection(GamesCollectionLinks links) {
    this.links = links;
  }

  public GamesCollection message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Welcome or informational message
   * @return message
   */
  
  @Schema(name = "message", example = "Welcome to the Number Guessing Game! Create a new game to start playing.", description = "Welcome or informational message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public GamesCollection totalGames(@Nullable Integer totalGames) {
    this.totalGames = totalGames;
    return this;
  }

  /**
   * Total number of active games (optional)
   * @return totalGames
   */
  
  @Schema(name = "totalGames", example = "5", description = "Total number of active games (optional)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalGames")
  public @Nullable Integer getTotalGames() {
    return totalGames;
  }

  public void setTotalGames(@Nullable Integer totalGames) {
    this.totalGames = totalGames;
  }

  public GamesCollection links(GamesCollectionLinks links) {
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
  public GamesCollectionLinks getLinks() {
    return links;
  }

  public void setLinks(GamesCollectionLinks links) {
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
    GamesCollection gamesCollection = (GamesCollection) o;
    return Objects.equals(this.message, gamesCollection.message) &&
        Objects.equals(this.totalGames, gamesCollection.totalGames) &&
        Objects.equals(this.links, gamesCollection.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, totalGames, links);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GamesCollection {\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    totalGames: ").append(toIndentedString(totalGames)).append("\n");
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

