package uk.co.jacobmetcalf.travelblog.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.immutables.value.Value;

/**
 * Common immutable style elements
 */
@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS) // Make it class retention for incremental compilation
@Value.Style(
    allowedClasspathAnnotations=Override.class // Stops use of javax.annotation
)
public @interface ImmutableStyle {}

