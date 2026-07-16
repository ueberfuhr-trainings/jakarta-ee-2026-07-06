package de.schulung.quarkus.todos.domain;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator für {@link MaximumFuture}: gültig, wenn der Wert {@code null} ist
 * oder höchstens die konfigurierte Zeitspanne in der Zukunft liegt.
 */
public class MaximumFutureValidator implements ConstraintValidator<MaximumFuture, LocalDate> {

    private MaximumFuture constraintAnnotation;

    @Override
    public void initialize(MaximumFuture constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null
                || !value.isAfter(
                        LocalDate.now().plus(
                                constraintAnnotation.value(),
                                constraintAnnotation.unit()));
    }

}
