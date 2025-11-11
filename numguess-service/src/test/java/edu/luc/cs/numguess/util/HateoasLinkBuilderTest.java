package edu.luc.cs.numguess.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openapitools.model.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for HateoasLinkBuilder.
 * Tests HATEOAS link generation for game operations.
 */
@SpringBootTest
@WebAppConfiguration
@DisplayName("HATEOAS Link Builder")
class HateoasLinkBuilderTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private HateoasLinkBuilder linkBuilder;
    private UUID testGameId;

    @BeforeEach
    void setUp() {
        linkBuilder = new HateoasLinkBuilder();
        testGameId = UUID.randomUUID();
    }

    @Nested
    @DisplayName("Self Link")
    class SelfLink {

        @Test
        @DisplayName("should build self link to game")
        void shouldBuildSelfLink() {
            Link link = linkBuilder.buildSelfLink(testGameId);

            assertNotNull(link);
            assertNotNull(link.getHref());
            assertEquals(Link.MethodEnum.GET, link.getMethod());
        }

        @Test
        @DisplayName("should include game UUID in href")
        void shouldIncludeGameUuidInHref() {
            Link link = linkBuilder.buildSelfLink(testGameId);

            String hrefString = link.getHref().toString();
            assertTrue(hrefString.contains(testGameId.toString()));
        }

        @Test
        @DisplayName("should have GET method")
        void shouldHaveGetMethod() {
            Link link = linkBuilder.buildSelfLink(testGameId);

            assertEquals(Link.MethodEnum.GET, link.getMethod());
        }

        @Test
        @DisplayName("should have JSON content type")
        void shouldHaveJsonContentType() {
            Link link = linkBuilder.buildSelfLink(testGameId);

            assertEquals("application/json", link.getType());
        }

        @Test
        @DisplayName("should not have title")
        void shouldNotHaveTitle() {
            Link link = linkBuilder.buildSelfLink(testGameId);

            assertNull(link.getTitle());
        }
    }

    @Nested
    @DisplayName("Games Collection Link")
    class GamesCollectionLink {

        @Test
        @DisplayName("should build games collection link")
        void shouldBuildGamesCollectionLink() {
            Link link = linkBuilder.buildGamesCollectionLink();

            assertNotNull(link);
            assertNotNull(link.getHref());
        }

        @Test
        @DisplayName("should point to /games endpoint")
        void shouldPointToGamesEndpoint() {
            Link link = linkBuilder.buildGamesCollectionLink();

            String href = link.getHref().toString();
            assertTrue(href.contains("/games"));
            assertTrue(href.endsWith("/games"));
        }

        @Test
        @DisplayName("should have GET method")
        void shouldHaveGetMethod() {
            Link link = linkBuilder.buildGamesCollectionLink();

            assertEquals(Link.MethodEnum.GET, link.getMethod());
        }

        @Test
        @DisplayName("should have descriptive title")
        void shouldHaveDescriptiveTitle() {
            Link link = linkBuilder.buildGamesCollectionLink();

            assertNotNull(link.getTitle());
            assertEquals("Games collection", link.getTitle());
        }

        @Test
        @DisplayName("should have JSON content type")
        void shouldHaveJsonContentType() {
            Link link = linkBuilder.buildGamesCollectionLink();

            assertEquals("application/json", link.getType());
        }
    }

    @Nested
    @DisplayName("API Root Link")
    class ApiRootLink {

        @Test
        @DisplayName("should build API root link")
        void shouldBuildApiRootLink() {
            Link link = linkBuilder.buildApiRootLink();

            assertNotNull(link);
            assertNotNull(link.getHref());
        }

        @Test
        @DisplayName("should point to API root /")
        void shouldPointToRoot() {
            Link link = linkBuilder.buildApiRootLink();

            String href = link.getHref().toString();
            assertTrue(href.endsWith("/"));
        }

        @Test
        @DisplayName("should have GET method")
        void shouldHaveGetMethod() {
            Link link = linkBuilder.buildApiRootLink();

            assertEquals(Link.MethodEnum.GET, link.getMethod());
        }

        @Test
        @DisplayName("should have descriptive title")
        void shouldHaveTitle() {
            Link link = linkBuilder.buildApiRootLink();

            assertEquals("API root", link.getTitle());
        }
    }

    @Nested
    @DisplayName("Submit Guess Link")
    class SubmitGuessLink {

        @Test
        @DisplayName("should build submit guess link")
        void shouldBuildSubmitGuessLink() {
            Link link = linkBuilder.buildSubmitGuessLink(testGameId);

            assertNotNull(link);
            assertNotNull(link.getHref());
        }

        @Test
        @DisplayName("should point to game endpoint")
        void shouldPointToGameEndpoint() {
            Link link = linkBuilder.buildSubmitGuessLink(testGameId);

            String href = link.getHref().toString();
            assertTrue(href.contains("/games/" + testGameId));
        }

        @Test
        @DisplayName("should have POST method")
        void shouldHavePostMethod() {
            Link link = linkBuilder.buildSubmitGuessLink(testGameId);

            assertEquals(Link.MethodEnum.POST, link.getMethod());
        }

        @Test
        @DisplayName("should have form content type")
        void shouldHaveFormContentType() {
            Link link = linkBuilder.buildSubmitGuessLink(testGameId);

            assertEquals("application/x-www-form-urlencoded", link.getType());
        }

        @Test
        @DisplayName("should have descriptive title")
        void shouldHaveDescriptiveTitle() {
            Link link = linkBuilder.buildSubmitGuessLink(testGameId);

            assertEquals("Submit a guess", link.getTitle());
        }
    }

    @Nested
    @DisplayName("Delete Game Link")
    class DeleteGameLink {

        @Test
        @DisplayName("should build delete game link")
        void shouldBuildDeleteGameLink() {
            Link link = linkBuilder.buildDeleteGameLink(testGameId);

            assertNotNull(link);
            assertNotNull(link.getHref());
        }

        @Test
        @DisplayName("should point to game endpoint")
        void shouldPointToGameEndpoint() {
            Link link = linkBuilder.buildDeleteGameLink(testGameId);

            String href = link.getHref().toString();
            assertTrue(href.contains("/games/" + testGameId));
        }

        @Test
        @DisplayName("should have DELETE method")
        void shouldHaveDeleteMethod() {
            Link link = linkBuilder.buildDeleteGameLink(testGameId);

            assertEquals(Link.MethodEnum.DELETE, link.getMethod());
        }

        @Test
        @DisplayName("should have descriptive title")
        void shouldHaveDescriptiveTitle() {
            Link link = linkBuilder.buildDeleteGameLink(testGameId);

            assertEquals("Delete this game", link.getTitle());
        }
    }

    @Nested
    @DisplayName("New Game Link")
    class NewGameLink {

        @Test
        @DisplayName("should build new game link")
        void shouldBuildNewGameLink() {
            Link link = linkBuilder.buildNewGameLink();

            assertNotNull(link);
            assertNotNull(link.getHref());
        }

        @Test
        @DisplayName("should point to games collection")
        void shouldPointToGamesCollection() {
            Link link = linkBuilder.buildNewGameLink();

            String href = link.getHref().toString();
            assertTrue(href.endsWith("/games"));
        }

        @Test
        @DisplayName("should have POST method")
        void shouldHavePostMethod() {
            Link link = linkBuilder.buildNewGameLink();

            assertEquals(Link.MethodEnum.POST, link.getMethod());
        }

        @Test
        @DisplayName("should have JSON content type")
        void shouldHaveJsonContentType() {
            Link link = linkBuilder.buildNewGameLink();

            assertEquals("application/json", link.getType());
        }

        @Test
        @DisplayName("should have descriptive title")
        void shouldHaveDescriptiveTitle() {
            Link link = linkBuilder.buildNewGameLink();

            assertEquals("Create a new game", link.getTitle());
        }
    }

    @Nested
    @DisplayName("Create Game Link (Deprecated)")
    class CreateGameLinkDeprecated {

        @Test
        @DisplayName("should be alias for buildNewGameLink")
        void shouldBeAliasForNewGameLink() {
            Link newGameLink = linkBuilder.buildNewGameLink();
            Link createGameLink = linkBuilder.buildCreateGameLink();

            assertEquals(newGameLink.getHref(), createGameLink.getHref());
            assertEquals(newGameLink.getMethod(), createGameLink.getMethod());
            assertEquals(newGameLink.getType(), createGameLink.getType());
            assertEquals(newGameLink.getTitle(), createGameLink.getTitle());
        }
    }

    @Nested
    @DisplayName("Link Consistency")
    class LinkConsistency {

        @Test
        @DisplayName("all links should have non-null hrefs")
        void allLinksShouldHaveHrefs() {
            assertNotNull(linkBuilder.buildSelfLink(testGameId).getHref());
            assertNotNull(linkBuilder.buildGamesCollectionLink().getHref());
            assertNotNull(linkBuilder.buildApiRootLink().getHref());
            assertNotNull(linkBuilder.buildSubmitGuessLink(testGameId).getHref());
            assertNotNull(linkBuilder.buildDeleteGameLink(testGameId).getHref());
            assertNotNull(linkBuilder.buildNewGameLink().getHref());
        }

        @Test
        @DisplayName("all links should have methods")
        void allLinksShouldHaveMethods() {
            assertNotNull(linkBuilder.buildSelfLink(testGameId).getMethod());
            assertNotNull(linkBuilder.buildGamesCollectionLink().getMethod());
            assertNotNull(linkBuilder.buildApiRootLink().getMethod());
            assertNotNull(linkBuilder.buildSubmitGuessLink(testGameId).getMethod());
            assertNotNull(linkBuilder.buildDeleteGameLink(testGameId).getMethod());
            assertNotNull(linkBuilder.buildNewGameLink().getMethod());
        }

        @Test
        @DisplayName("game-specific links should use correct UUID")
        void gameSpecificLinksShouldUseCorrectUuid() {
            UUID uuid1 = UUID.randomUUID();
            UUID uuid2 = UUID.randomUUID();

            String href1 = linkBuilder.buildSelfLink(uuid1).getHref().toString();
            String href2 = linkBuilder.buildSelfLink(uuid2).getHref().toString();

            assertTrue(href1.contains(uuid1.toString()));
            assertTrue(href2.contains(uuid2.toString()));
            assertFalse(href1.equals(href2));
        }
    }
}
