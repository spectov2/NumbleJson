package dev.quantam.numblejson.engine;

import dev.quantam.numblejson.annotations.*;
import dev.quantam.numblejson.exceptions.JsonDeserializationException;
import dev.quantam.numblejson.interfaces.IJsonDeserializer;
import dev.quantam.numblejson.tokenize.JsonTokenizer;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Main class for deserializing JSON strings into Java objects.
 *
 * @author quantam
 * @version 1.0
 */
public class JsonDeserializer {

    /**
     * Deserializes a JSON string into an object of the specified class.
     *
     * @param json  the JSON string to deserialize
     * @param clazz the class type to deserialize into
     * @param <T>   the type of the resulting object
     * @return an instance of the specified class populated with data from the JSON string
     * @throws JsonDeserializationException if an error occurs during deserialization
     */
    public static <T> T deserialize(String json, Class<T> clazz) throws JsonDeserializationException {
        try {
            JsonTokenizer tokenizer = new JsonTokenizer(json);
            Object result = deserializeValue(tokenizer, clazz);
            return clazz.cast(result);
        } catch (Exception e) {
            throw new JsonDeserializationException("Error during deserialization", e);
        }
    }

    /**
     * Deserializes a JSON value into an object of the specified type.
     *
     * @param tokenizer the JSON tokenizer instance
     * @param type      the target type to deserialize into
     * @return an object of the specified type populated with data from the JSON value
     * @throws Exception if an error occurs during deserialization
     */
    private static Object deserializeValue(JsonTokenizer tokenizer, Class<?> type) throws Exception {
        String token = tokenizer.nextToken();
        if ("null".equals(token)) return null;
        if ("true".equals(token)) return true;
        if ("false".equals(token)) return false;
        if (token.startsWith("\"")) return token.substring(1, token.length() - 1);
        if (Character.isDigit(token.charAt(0)) || token.charAt(0) == '-') {
            if (type == int.class || type == Integer.class) return Integer.parseInt(token);
            if (type == long.class || type == Long.class) return Long.parseLong(token);
            if (type == float.class || type == Float.class) return Float.parseFloat(token);
            if (type == double.class || type == Double.class) return Double.parseDouble(token);
            // Default to Integer if type is not explicitly handled
            return Integer.parseInt(token);
        }
        if ("[".equals(token)) return deserializeList(tokenizer);
        if ("{".equals(token)) return deserializeObject(tokenizer, type);
        throw new JsonDeserializationException("Unexpected token: " + token);
    }

    /**
     * Deserializes a JSON array into a Java List.
     *
     * @param tokenizer the JSON tokenizer instance
     * @return a List containing elements deserialized from the JSON array
     * @throws Exception if an error occurs during deserialization
     */
    private static List<?> deserializeList(JsonTokenizer tokenizer) throws Exception {
        List<Object> list = new ArrayList<>();
        while (!"]".equals(tokenizer.peek())) {
            list.add(deserializeValue(tokenizer, Object.class));
            if (",".equals(tokenizer.peek())) tokenizer.nextToken();
        }
        tokenizer.nextToken(); // consume ']'
        return list;
    }

    /**
     * Deserializes a JSON object into a Java object of the specified type.
     *
     * @param tokenizer the JSON tokenizer instance
     * @param type      the target type to deserialize into
     * @return an instance of the specified type populated with data from the JSON object
     * @throws Exception if an error occurs during deserialization
     */
    private static Object deserializeObject(JsonTokenizer tokenizer, Class<?> type) throws Exception {
        if (Map.class.isAssignableFrom(type)) {
            return deserializeMap(tokenizer);
        }

        Object obj = type.getDeclaredConstructor().newInstance();
        while (!"}".equals(tokenizer.peek())) {
            String fieldName = tokenizer.nextToken();
            fieldName = fieldName.substring(1, fieldName.length() - 1); // remove quotes
            tokenizer.nextToken(); // consume ':'

            Field field = findField(type, fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(obj, deserializeValue(tokenizer, field));
            } else {
                // Skip unknown fields
                deserializeValue(tokenizer, Object.class);
            }

            if (",".equals(tokenizer.peek())) tokenizer.nextToken();
        }
        tokenizer.nextToken(); // consume '}'
        return obj;
    }

    /**
     * Deserializes a JSON map into a Java Map.
     *
     * @param tokenizer the JSON tokenizer instance
     * @return a Map containing key-value pairs deserialized from the JSON map
     * @throws Exception if an error occurs during deserialization
     */
    private static Map<String, Object> deserializeMap(JsonTokenizer tokenizer) throws Exception {
        Map<String, Object> map = new HashMap<>();
        while (!"}".equals(tokenizer.peek())) {
            String key = tokenizer.nextToken();
            key = key.substring(1, key.length() - 1); // remove quotes
            tokenizer.nextToken(); // consume ':'
            Object value = deserializeValue(tokenizer, Object.class);
            map.put(key, value);
            if (",".equals(tokenizer.peek())) tokenizer.nextToken();
        }
        tokenizer.nextToken(); // consume '}'
        return map;
    }

    /**
     * Deserializes a JSON value into an object field value, considering field annotations.
     *
     * @param tokenizer the JSON tokenizer instance
     * @param field     the field to deserialize into
     * @return the deserialized value for the field
     * @throws Exception if an error occurs during deserialization
     */
    private static Object deserializeValue(JsonTokenizer tokenizer, Field field) throws Exception {
        if (field.isAnnotationPresent(JsonDeserialize.class)) {
            JsonDeserialize annotation = field.getAnnotation(JsonDeserialize.class);
            IJsonDeserializer<?> deserializer = annotation.using().getDeclaredConstructor().newInstance();
            return deserializer.deserialize(tokenizer.nextToken());
        }

        if (field.isAnnotationPresent(JsonDateFormat.class)) {
            return deserializeDate(tokenizer.nextToken(), field.getAnnotation(JsonDateFormat.class).value());
        }

        return deserializeValue(tokenizer, field.getType());
    }

    /**
     * Finds a field in a class based on its name, considering JSON property annotations and aliases.
     *
     * @param type      the class type to search for the field
     * @param fieldName the name of the field to find
     * @return the Field object representing the found field, or {@code null} if not found
     */
    private static Field findField(Class<?> type, String fieldName) {
        for (Field field : type.getDeclaredFields()) {
            if (field.isAnnotationPresent(JsonProperty.class)) {
                JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
                if (jsonProperty.value().equals(fieldName)) {
                    return field;
                }
            }
            if (field.isAnnotationPresent(JsonAlias.class)) {
                JsonAlias jsonAlias = field.getAnnotation(JsonAlias.class);
                for (String alias : jsonAlias.value()) {
                    if (alias.equals(fieldName)) {
                        return field;
                    }
                }
            }
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    /**
     * Deserializes a JSON date string into a Java Date object based on a specified date format.
     *
     * @param value  the JSON date string to deserialize
     * @param format the date format string
     * @return a Date object representing the deserialized date
     * @throws ParseException if the JSON date string cannot be parsed according to the format
     */
    private static Date deserializeDate(String value, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(value.replace("\"", ""));
    }
}
