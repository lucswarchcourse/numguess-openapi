package edu.luc.cs.numguess.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test suite for ContentNegotiationUtil.
 * Tests content type detection logic for HTML vs JSON responses.
 */
@DisplayName("Content Negotiation Utility")
class ContentNegotiationUtilTest {

    @Nested
    @DisplayName("HTML Request Detection")
    class HtmlRequestDetection {

        @Test
        @DisplayName("should detect text/html as HTML request")
        void shouldDetectTextHtml() {
            assertTrue(ContentNegotiationUtil.isHtmlRequest("text/html"));
        }

        @Test
        @DisplayName("should detect application/xhtml+xml as HTML request")
        void shouldDetectApplicationXhtmlXml() {
            assertTrue(ContentNegotiationUtil.isHtmlRequest("application/xhtml+xml"));
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "text/html, application/xhtml+xml, application/xml;q=0.9, image/webp, */*;q=0.8",
            "text/html;q=0.9, */*;q=0.8",
            "application/xhtml+xml, application/xml;q=0.9",
            "*/*;q=0.8, text/html"
        })
        @DisplayName("should detect HTML in complex Accept headers")
        void shouldDetectHtmlInComplexHeaders(String acceptHeader) {
            assertTrue(ContentNegotiationUtil.isHtmlRequest(acceptHeader));
        }

        @Test
        @DisplayName("should return false for null Accept header")
        void shouldReturnFalseForNull() {
            assertFalse(ContentNegotiationUtil.isHtmlRequest(null));
        }

        @Test
        @DisplayName("should return false for empty string")
        void shouldReturnFalseForEmpty() {
            assertFalse(ContentNegotiationUtil.isHtmlRequest(""));
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "application/json",
            "application/json; charset=utf-8",
            "application/hal+json",
            "application/json, application/hal+json",
            "*/*"
        })
        @DisplayName("should return false for non-HTML Accept headers")
        void shouldReturnFalseForNonHtml(String acceptHeader) {
            assertFalse(ContentNegotiationUtil.isHtmlRequest(acceptHeader));
        }
    }

    @Nested
    @DisplayName("JSON Request Detection")
    class JsonRequestDetection {

        @Test
        @DisplayName("should detect application/json as JSON request")
        void shouldDetectApplicationJson() {
            assertTrue(ContentNegotiationUtil.isJsonRequest("application/json"));
        }

        @Test
        @DisplayName("should treat null as JSON request (default)")
        void shouldTreatNullAsJsonRequest() {
            assertTrue(ContentNegotiationUtil.isJsonRequest(null));
        }

        @Test
        @DisplayName("should treat empty string as JSON request (default)")
        void shouldTreatEmptyAsJsonRequest() {
            assertTrue(ContentNegotiationUtil.isJsonRequest(""));
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "text/html",
            "application/xhtml+xml",
            "text/html, application/json"
        })
        @DisplayName("should return false for HTML Accept headers")
        void shouldReturnFalseForHtmlHeaders(String acceptHeader) {
            assertFalse(ContentNegotiationUtil.isJsonRequest(acceptHeader));
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "application/json",
            "application/hal+json",
            "*/*",
            "application/xml"
        })
        @DisplayName("should return true for non-HTML Accept headers")
        void shouldReturnTrueForNonHtmlHeaders(String acceptHeader) {
            assertTrue(ContentNegotiationUtil.isJsonRequest(acceptHeader));
        }
    }

    @Nested
    @DisplayName("Content Negotiation Logic")
    class ContentNegotiationLogic {

        @Test
        @DisplayName("should be mutually exclusive: HTML XOR JSON")
        void shouldBeMutuallyExclusive() {
            String[] testHeaders = {
                "text/html",
                "application/json",
                "application/xhtml+xml",
                null,
                ""
            };

            for (String header : testHeaders) {
                boolean isHtml = ContentNegotiationUtil.isHtmlRequest(header);
                boolean isJson = ContentNegotiationUtil.isJsonRequest(header);

                // Either HTML or JSON, but not both
                assertTrue(isHtml ^ isJson, "Request should be either HTML or JSON, not both for: " + header);
            }
        }

        @Test
        @DisplayName("should handle case-sensitive media types correctly")
        void shouldHandleCaseSensitiveMediaTypes() {
            // Standard is lowercase, but testing robustness
            assertTrue(ContentNegotiationUtil.isHtmlRequest("text/html"));
            assertFalse(ContentNegotiationUtil.isHtmlRequest("TEXT/HTML")); // Case-sensitive
        }

        @Test
        @DisplayName("should handle whitespace in Accept header")
        void shouldHandleWhitespaceInAcceptHeader() {
            assertTrue(ContentNegotiationUtil.isHtmlRequest("text/html, application/xhtml+xml"));
            assertTrue(ContentNegotiationUtil.isHtmlRequest("text/html,application/xhtml+xml"));
        }
    }

    @Nested
    @DisplayName("Browser Scenarios")
    class BrowserScenarios {

        @Test
        @DisplayName("should handle typical Chrome Accept header")
        void shouldHandleChromeAcceptHeader() {
            String chromeHeader = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8";
            assertTrue(ContentNegotiationUtil.isHtmlRequest(chromeHeader));
        }

        @Test
        @DisplayName("should handle typical Firefox Accept header")
        void shouldHandleFirefoxAcceptHeader() {
            String firefoxHeader = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
            assertTrue(ContentNegotiationUtil.isHtmlRequest(firefoxHeader));
        }

        @Test
        @DisplayName("should handle typical API client Accept header")
        void shouldHandleApiClientAcceptHeader() {
            String apiHeader = "application/json";
            assertFalse(ContentNegotiationUtil.isHtmlRequest(apiHeader));
            assertTrue(ContentNegotiationUtil.isJsonRequest(apiHeader));
        }

        @Test
        @DisplayName("should handle curl default Accept header")
        void shouldHandleCurlDefaultAcceptHeader() {
            String curlDefault = "*/*";
            assertFalse(ContentNegotiationUtil.isHtmlRequest(curlDefault));
            assertTrue(ContentNegotiationUtil.isJsonRequest(curlDefault));
        }
    }
}
