package edu.luc.cs.numguess.util;

import org.openapitools.model.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * Utility component for building HATEOAS links.
 * Centralizes link construction logic and ensures consistent URL building
 * across the entire application, adapting to any deployment environment.
 */
@Component
public class HateoasLinkBuilder {

    /**
     * Extracts the base URL from the current request context.
     * This ensures links work correctly regardless of deployment environment
     * (localhost, production server, reverse proxy, etc.).
     *
     * @return the base URL including context path
     */
    private String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .build()
            .toUriString();
    }

    /**
     * Builds a self link pointing to a specific game resource.
     *
     * @param gameId the UUID of the game
     * @return Link for GET /games/{gameId}
     */
    public Link buildSelfLink(final UUID gameId) {
        return new Link()
            .href(URI.create(getBaseUrl() + "/games/" + gameId))
            .method(Link.MethodEnum.GET)
            .type("application/json");
    }

    /**
     * Builds a link to the games collection.
     *
     * @return Link for GET /games
     */
    public Link buildGamesCollectionLink() {
        return new Link()
            .href(URI.create(getBaseUrl() + "/games"))
            .method(Link.MethodEnum.GET)
            .type("application/json")
            .title("Games collection");
    }

    /**
     * Builds a link to the API root.
     *
     * @return Link for GET /
     */
    public Link buildApiRootLink() {
        return new Link()
            .href(URI.create(getBaseUrl() + "/"))
            .method(Link.MethodEnum.GET)
            .type("application/json")
            .title("API root");
    }

    /**
     * Builds an action link to submit a guess for a specific game.
     * Used for state-driven affordances - only included when game is active.
     *
     * @param gameId the UUID of the game
     * @return Link for POST /games/{gameId}
     */
    public Link buildSubmitGuessLink(final UUID gameId) {
        return new Link()
            .href(URI.create(getBaseUrl() + "/games/" + gameId))
            .method(Link.MethodEnum.POST)
            .type("application/x-www-form-urlencoded")
            .title("Submit a guess");
    }

    /**
     * Builds an action link to delete a specific game.
     *
     * @param gameId the UUID of the game
     * @return Link for DELETE /games/{gameId}
     */
    public Link buildDeleteGameLink(final UUID gameId) {
        return new Link()
            .href(URI.create(getBaseUrl() + "/games/" + gameId))
            .method(Link.MethodEnum.DELETE)
            .title("Delete this game");
    }

    /**
     * Builds an action link to create a new game.
     * Can be used to create the first game or to start a new game after winning.
     * Also used in the games collection context for "create-game" relation.
     *
     * @return Link for POST /games
     */
    public Link buildNewGameLink() {
        return new Link()
            .href(URI.create(getBaseUrl() + "/games"))
            .method(Link.MethodEnum.POST)
            .type("application/json")
            .title("Create a new game");
    }

    /**
     * Alias for {@link #buildNewGameLink()}.
     * Provided for semantic clarity when used in collection context.
     *
     * @return Link for POST /games (create-game relation)
     * @deprecated Use {@link #buildNewGameLink()} instead
     */
    @Deprecated(since = "1.1.0", forRemoval = true)
    public Link buildCreateGameLink() {
        return buildNewGameLink();
    }
}
