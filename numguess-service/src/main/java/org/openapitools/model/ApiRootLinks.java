package org.openapitools.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.annotation.Generated;

/**
 * ApiRootLinks
 */

@JsonTypeName("ApiRoot__links")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-10T20:40:10.675029-06:00[America/Chicago]", comments = "Generator version: 7.17.0")
public class ApiRootLinks {

  private Link self;

  private Link games;

  public ApiRootLinks() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ApiRootLinks(Link self, Link games) {
    this.self = self;
    this.games = games;
  }

  public ApiRootLinks self(Link self) {
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

  public ApiRootLinks games(Link games) {
    this.games = games;
    return this;
  }

  /**
   * Get games
   * @return games
   */
  @NotNull @Valid 
  @Schema(name = "games", requiredMode = Schema.RequiredMode.REQUIRED)
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
    ApiRootLinks apiRootLinks = (ApiRootLinks) o;
    return Objects.equals(this.self, apiRootLinks.self) &&
        Objects.equals(this.games, apiRootLinks.games);
  }

  @Override
  public int hashCode() {
    return Objects.hash(self, games);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiRootLinks {\n");
    sb.append("    self: ").append(toIndentedString(self)).append("\n");
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

