package dev.quantam.numblejson.interfaces;


import dev.quantam.numblejson.exceptions.JsonDeserializationException;

/**
 * Interface for custom JSON deserializers.
 *
 * @param <T> The type of object this deserializer can handle.
 *
 * @author quantam
 * @version 1.0
 */
public interface IJsonDeserializer<T> {
    /**
     * Deserialize the given JSON string to an object.
     *
     * @param json The JSON string to deserialize.
     * @return The deserialized object.
     * @throws JsonDeserializationException If an error occurs during deserialization.
     */
    T deserialize(String json) throws JsonDeserializationException;
}