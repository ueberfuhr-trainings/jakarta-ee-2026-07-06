package de.schulung.jakartaee.todos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Prüft, dass ein Datum höchstens {@code value} Einheiten ({@code unit}) in der
 * Zukunft liegt (Standard: 3 Monate). {@code null} gilt als gültig – die
 * Pflichtprüfung übernimmt {@code @NotNull}. Über {@link #value()} und
 * {@link #unit()} ist die Obergrenze pro Verwendung konfigurierbar, z. B.
 * {@code @MaximumFuture(value = 6, unit = ChronoUnit.YEARS)}.
 */
@Constraint(validatedBy = MaximumFutureValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface MaximumFuture {

    String message() default "Die Frist darf höchstens {value} {unit} in der Zukunft liegen.";

    long value();

    ChronoUnit unit() default ChronoUnit.MONTHS;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
