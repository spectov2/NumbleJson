package dev.quantam.numblejson.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines one or more alternative names for a field during deserialization.
 *
 * @author quantam
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonAlias {
    /**
     * Alternative names for the field.
     */
    String[] value();
}