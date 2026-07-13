package de.schulung.jakartaee.todos.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
        				LocalDate
        					.now()
        					.plus(
    							constraintAnnotation.value(), 
    							constraintAnnotation.unit()
        					)
        			);
    }

}
