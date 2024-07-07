package dev.quantam.numblejson.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for specifying the JSON property name associated with a field.
 *
 * @author quantam
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonProperty {

    /**
     * Specifies the JSON property name.
     *
     * @return the JSON property name
     */
    String value();
}
