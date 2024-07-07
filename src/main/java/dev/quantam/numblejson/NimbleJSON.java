package dev.quantam.numblejson;


import dev.quantam.numblejson.engine.JsonDeserializer;
import dev.quantam.numblejson.engine.JsonSerializer;
import dev.quantam.numblejson.exceptions.JsonDeserializationException;
import dev.quantam.numblejson.exceptions.JsonSerializationException;

/**
 * NimbleJSON: A lightweight, fast JSON serialization and deserialization library.
 * This class provides methods to convert Java objects to JSON strings and vice versa.
 *
 * @author quantam
 * @version 1.0
 */
public class NimbleJSON {

    private NimbleJSON() {
        // Private constructor to prevent instantiation
    }

    /**
     * Serializes an object to a JSON string.
     *
     * @param obj The object to serialize
     * @return A JSON string representation of the object
     * @throws JsonSerializationException If an error occurs during serialization
     */
    public static String serialize(Object obj) throws JsonSerializationException {
        return JsonSerializer.serialize(obj);
    }

    /**
     * Deserializes a JSON string to an object of the specified class.
     *
     * @param json The JSON string to deserialize
     * @param clazz The class of the object to deserialize to
     * @param <T> The type of the object to deserialize to
     * @return An instance of the specified class populated with data from the JSON string
     * @throws JsonDeserializationException If an error occurs during deserialization
     */
    public static <T> T deserialize(String json, Class<T> clazz) throws JsonDeserializationException {
        return JsonDeserializer.deserialize(json, clazz);
    }
}