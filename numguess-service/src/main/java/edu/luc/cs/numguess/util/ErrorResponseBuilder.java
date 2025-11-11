package edu.luc.cs.numguess.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openapitools.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Utility component for building consistent error responses.
 * Centralizes error creation and serialization logic to eliminate duplication
 * across multiple delegate classes.
 *
 * Benefits:
 * - DRY: Single source of truth for error response creation
 * - Consistency: All errors follow the same format and structure
 * - Maintainability: Changes to error format apply globally
 * - Testability: Error response logic can be tested independently
 */
@Component
public class ErrorResponseBuilder {

    private final ObjectMapper objectMapper;

    public ErrorResponseBuilder(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Builds a 404 Not Found error response.
     *
     * @param message the error message
     * @return ResponseEntity with 404 status and error JSON body
     */
    public ResponseEntity<String> notFound(final String message) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, message);
    }

    /**
     * Builds a 400 Bad Request error response.
     *
     * @param message the error message
     * @return ResponseEntity with 400 status and error JSON body
     */
    public ResponseEntity<String> badRequest(final String message) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * Builds a 500 Internal Server Error response.
     *
     * @param message the error message
     * @return ResponseEntity with 500 status and error JSON body
     */
    public ResponseEntity<String> internalServerError(final String message) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    /**
     * Builds an error response with the specified HTTP status.
     *
     * @param status the HTTP status code
     * @param message the error message
     * @return ResponseEntity with the specified status and error JSON body
     */
    public ResponseEntity<String> buildErrorResponse(final HttpStatus status, final String message) {
        final var error = new Error();
        error.setError(message);
        error.setStatus(status.value());

        try {
            final var jsonError = objectMapper.writeValueAsString(error);
            return ResponseEntity.status(status).body(jsonError);
        } catch (Exception e) {
            // Fallback if JSON serialization fails
            return ResponseEntity.status(status).body("{\"error\":\"" + message + "\",\"status\":" + status.value() + "}");
        }
    }
}
