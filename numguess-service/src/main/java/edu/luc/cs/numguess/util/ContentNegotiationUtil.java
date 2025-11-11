package edu.luc.cs.numguess.util;

/**
 * Utility class for content negotiation logic.
 * Centralizes the determination of requested content type to eliminate duplication
 * across multiple delegate classes.
 *
 * Benefits:
 * - DRY: Single source of truth for content negotiation rules
 * - Consistency: All endpoints use the same content negotiation logic
 * - Testability: Content negotiation logic can be tested independently
 * - Maintainability: Changes to content type detection apply globally
 */
public class ContentNegotiationUtil {

    private ContentNegotiationUtil() {
        // Utility class, non-instantiable
    }

    /**
     * Determines if the client is requesting HTML content based on the Accept header.
     * Returns true for both text/html and application/xhtml+xml media types.
     *
     * @param acceptHeader the value of the Accept HTTP header, may be null
     * @return true if HTML content is requested, false otherwise
     */
    public static boolean isHtmlRequest(final String acceptHeader) {
        if (acceptHeader == null) {
            return false;
        }
        return acceptHeader.contains("text/html") || acceptHeader.contains("application/xhtml+xml");
    }

    /**
     * Determines if the client is requesting JSON content based on the Accept header.
     * This is used as the default if HTML is not explicitly requested.
     *
     * @param acceptHeader the value of the Accept HTTP header, may be null
     * @return true if JSON content is explicitly requested or acceptHeader is null/doesn't request HTML
     */
    public static boolean isJsonRequest(final String acceptHeader) {
        return !isHtmlRequest(acceptHeader);
    }
}
