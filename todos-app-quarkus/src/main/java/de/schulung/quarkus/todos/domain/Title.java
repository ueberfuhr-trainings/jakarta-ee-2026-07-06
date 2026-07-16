package de.schulung.quarkus.todos.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Composite Constraint für einen Titel: 3–100 Zeichen und Beginn mit einem
 * Großbuchstaben. Bündelt vorhandene Constraints und braucht daher keine
 * eigene Validator-Implementierung ({@code validatedBy = {}}).
 */
@Size(min = 3, max = 100)
@Pattern(regexp = "^\\p{Lu}.*")
@Constraint(validatedBy = {})
@ReportAsSingleViolation
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Title {

    String message() default "Der Titel muss 3–100 Zeichen lang sein und mit einem Großbuchstaben beginnen.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
