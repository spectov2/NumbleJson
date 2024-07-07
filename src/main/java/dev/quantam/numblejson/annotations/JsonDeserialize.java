package dev.quantam.numblejson.annotations;

import dev.quantam.numblejson.interfaces.IJsonDeserializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a custom deserializer class to use for a field.
 *
 * @author quantam
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonDeserialize {
    /**
     * The custom deserializer class to use.
     */
    Class<? extends IJsonDeserializer<?>> using();
}