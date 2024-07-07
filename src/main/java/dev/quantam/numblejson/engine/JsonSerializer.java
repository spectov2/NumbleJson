package dev.quantam.numblejson.engine;

import dev.quantam.numblejson.annotations.*;
import dev.quantam.numblejson.exceptions.JsonSerializationException;
import dev.quantam.numblejson.interfaces.IJsonSerializer;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Main class for serializing Java objects into JSON strings.
 *
 * @author quantam
 * @version 1.0
 */
public class JsonSerializer {

    /**
     * Serializes an object into its JSON representation.
     *
     * @param obj the object to serialize
     * @return the JSON representation of the object
     * @throws JsonSerializationException if an error occurs during serialization
     */
    public static String serialize(Object obj) throws JsonSerializationException {
        try {
            if (obj == null) return "null";
            if (obj instanceof Number || obj instanceof Boolean) return obj.toString();
            if (obj instanceof String) return "\"" + escapeString((String) obj) + "\"";
            if (obj instanceof List) return serializeList((List<?>) obj);
            if (obj instanceof Map) return serializeMap((Map<?, ?>) obj);
            return serializeObject(obj);
        } catch (Exception e) {
            throw new JsonSerializationException("Error during serialization", e);
        }
    }

    /**
     * Serializes a list into its JSON array representation.
     *
     * @param list the list to serialize
     * @return the JSON array representation of the list
     * @throws JsonSerializationException if an error occurs during serialization
     */
    private static String serializeList(List<?> list) throws JsonSerializationException {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(serialize(list.get(i)));
        }
        return sb.append("]").toString();
    }

    /**
     * Serializes a map into its JSON object representation.
     *
     * @param map the map to serialize
     * @return the JSON object representation of the map
     * @throws JsonSerializationException if an error occurs during serialization
     */
    private static String serializeMap(Map<?, ?> map) throws JsonSerializationException {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) sb.append(",");
            sb.append("\"").append(entry.getKey()).append("\":");
            sb.append(serialize(entry.getValue()));
            first = false;
        }
        return sb.append("}").toString();
    }

    /**
     * Serializes a Java object annotated with JSON annotations into its JSON object representation.
     *
     * @param obj the object to serialize
     * @return the JSON object representation of the object
     * @throws JsonSerializationException if an error occurs during serialization
     */
    private static String serializeObject(Object obj) throws JsonSerializationException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Field field : fields) {
            if (field.isAnnotationPresent(JsonIgnore.class)) {
                continue;
            }
            field.setAccessible(true);
            if (!first) sb.append(",");
            String fieldName = field.isAnnotationPresent(JsonProperty.class) ?
                    field.getAnnotation(JsonProperty.class).value() : field.getName();
            sb.append("\"").append(fieldName).append("\":");
            try {
                Object value = field.get(obj);
                if (shouldIncludeField(field, value)) {
                    if (field.isAnnotationPresent(JsonSerialize.class)) {
                        JsonSerialize annotation = field.getAnnotation(JsonSerialize.class);
                        @SuppressWarnings("unchecked")
                        IJsonSerializer<Object> serializer = (IJsonSerializer<Object>) annotation.using().getDeclaredConstructor().newInstance();
                        sb.append(serializer.serialize(value));
                    } else if (field.isAnnotationPresent(JsonDateFormat.class)) {
                        sb.append(serializeDate(value, field.getAnnotation(JsonDateFormat.class).value()));
                    } else {
                        sb.append(serialize(value));
                    }
                    first = false;
                }
            } catch (Exception e) {
                throw new JsonSerializationException("Error accessing field: " + field.getName(), e);
            }
        }
        return sb.append("}").toString();
    }

    /**
     * Checks if a field should be included in the JSON output based on its annotation and value.
     *
     * @param field the field to check
     * @param value the value of the field
     * @return {@code true} if the field should be included, otherwise {@code false}
     */
    private static boolean shouldIncludeField(Field field, Object value) {
        if (field.isAnnotationPresent(JsonInclude.class)) {
            JsonInclude.Include include = field.getAnnotation(JsonInclude.class).value();
            if (include == JsonInclude.Include.NON_NULL && value == null) {
                return false;
            }
            if (include == JsonInclude.Include.NON_EMPTY) {
                if (value == null) return false;
                if (value instanceof String && ((String) value).isEmpty()) return false;
                if (value instanceof Collection && ((Collection<?>) value).isEmpty()) return false;
                if (value instanceof Map && ((Map<?, ?>) value).isEmpty()) return false;
            }
        }
        return true;
    }

    /**
     * Serializes a date object into its JSON string representation based on a specified date format.
     *
     * @param value  the date object to serialize
     * @param format the date format string
     * @return the JSON string representation of the date
     */
    private static String serializeDate(Object value, String format) {
        if (value instanceof Date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return "\"" + dateFormat.format((Date) value) + "\"";
        }
        return "null";
    }

    /**
     * Escapes special characters in a string to ensure valid JSON representation.
     *
     * @param s the string to escape
     * @return the escaped string
     */
    private static String escapeString(String s) {
        return s.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\f", "\\f");
    }
}
