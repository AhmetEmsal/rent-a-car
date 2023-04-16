package kodlama.io.rentacar.core.validators.minCurrentDate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = MinCurrentDateValidator.class)
@Documented
public @interface MinCurrentDate {

    String message() default "The date must be in the future. Check the month and year values.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String yearFieldName();

    String monthFieldName();

}