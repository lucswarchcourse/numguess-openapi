package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.net.URI;
import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * A hypermedia link following HAL conventions
 */

@Schema(name = "Link", description = "A hypermedia link following HAL conventions")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-10T20:40:10.675029-06:00[America/Chicago]", comments = "Generator version: 7.17.0")
public class Link implements LinksValue {

  private URI href;

  private Boolean templated = false;

  private @Nullable String type;

  /**
   * HTTP method to use (for action links)
   */
  public enum MethodEnum {
    GET("GET"),
    
    POST("POST"),
    
    PUT("PUT"),
    
    PATCH("PATCH"),
    
    DELETE("DELETE");

    private final String value;

    MethodEnum(String value) {
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
    public static MethodEnum fromValue(String value) {
      for (MethodEnum b : MethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable MethodEnum method;

  private @Nullable String title;

  public Link() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Link(URI href) {
    this.href = href;
  }

  public Link href(URI href) {
    this.href = href;
    return this;
  }

  /**
   * The URI of the linked resource
   * @return href
   */
  @NotNull @Valid 
  @Schema(name = "href", description = "The URI of the linked resource", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("href")
  public URI getHref() {
    return href;
  }

  public void setHref(URI href) {
    this.href = href;
  }

  public Link templated(Boolean templated) {
    this.templated = templated;
    return this;
  }

  /**
   * Whether the href is a URI template
   * @return templated
   */
  
  @Schema(name = "templated", description = "Whether the href is a URI template", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("templated")
  public Boolean getTemplated() {
    return templated;
  }

  public void setTemplated(Boolean templated) {
    this.templated = templated;
  }

  public Link type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Media type hint for the target resource
   * @return type
   */
  
  @Schema(name = "type", description = "Media type hint for the target resource", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public Link method(@Nullable MethodEnum method) {
    this.method = method;
    return this;
  }

  /**
   * HTTP method to use (for action links)
   * @return method
   */
  
  @Schema(name = "method", description = "HTTP method to use (for action links)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("method")
  public @Nullable MethodEnum getMethod() {
    return method;
  }

  public void setMethod(@Nullable MethodEnum method) {
    this.method = method;
  }

  public Link title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Human-readable title for the link
   * @return title
   */
  
  @Schema(name = "title", description = "Human-readable title for the link", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Link link = (Link) o;
    return Objects.equals(this.href, link.href) &&
        Objects.equals(this.templated, link.templated) &&
        Objects.equals(this.type, link.type) &&
        Objects.equals(this.method, link.method) &&
        Objects.equals(this.title, link.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(href, templated, type, method, title);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Link {\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    templated: ").append(toIndentedString(templated)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    method: ").append(toIndentedString(method)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
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

