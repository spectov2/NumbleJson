package dev.quantam.numblejson.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the date format to use when serializing/deserializing Date fields.
 *
 * @author quantam
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonDateFormat {
    /**
     * The date format pattern to use (e.g., "yyyy-MM-dd HH:mm:ss").
     */
    String value();
}