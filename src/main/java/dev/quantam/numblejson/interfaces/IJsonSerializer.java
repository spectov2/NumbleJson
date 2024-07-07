package dev.quantam.numblejson.interfaces;

import dev.quantam.numblejson.exceptions.JsonSerializationException;

/**
 * Interface for custom JSON serializers.
 *
 * @param <T> The type of object this serializer can handle.
 *
 * @author quantam
 * @version 1.0
 */
public interface IJsonSerializer<T> {
    /**
     * Serialize the given object to a JSON string.
     *
     * @param value The object to serialize.
     * @return A JSON string representation of the object.
     * @throws JsonSerializationException If an error occurs during serialization.
     */
    String serialize(T value) throws JsonSerializationException;
}