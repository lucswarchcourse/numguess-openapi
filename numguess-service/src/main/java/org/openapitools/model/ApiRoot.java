package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.model.ApiRootLinks;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * API root resource with hypermedia controls
 */

@Schema(name = "ApiRoot", description = "API root resource with hypermedia controls")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-10T20:40:10.675029-06:00[America/Chicago]", comments = "Generator version: 7.17.0")
public class ApiRoot {

  private @Nullable String message;

  private ApiRootLinks links;

  public ApiRoot() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ApiRoot(ApiRootLinks links) {
    this.links = links;
  }

  public ApiRoot message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Welcome message
   * @return message
   */
  
  @Schema(name = "message", example = "Welcome to the Number Guessing Game API", description = "Welcome message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public ApiRoot links(ApiRootLinks links) {
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
  public ApiRootLinks getLinks() {
    return links;
  }

  public void setLinks(ApiRootLinks links) {
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
    ApiRoot apiRoot = (ApiRoot) o;
    return Objects.equals(this.message, apiRoot.message) &&
        Objects.equals(this.links, apiRoot.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, links);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiRoot {\n");
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

