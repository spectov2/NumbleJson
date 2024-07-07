package dev.quantam.numblejson.exceptions;

/**
 * Exception thrown when an error occurs during JSON deserialization.
 *
 * @author quantam
 * @version 1.0
 */
public class JsonDeserializationException extends Exception {

    /**
     * Constructs a {@code JsonDeserializationException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     */
    public JsonDeserializationException(String message) {
        super(message);
    }

    /**
     * Constructs a {@code JsonDeserializationException} with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method)
     */
    public JsonDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
