package dev.quantam.numblejson.annotations;


import dev.quantam.numblejson.interfaces.IJsonSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a custom serializer class to use for a field.
 *
 * @author quantam
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonSerialize {
    /**
     * The custom serializer class to use.
     */
    Class<? extends IJsonSerializer<?>> using();
}