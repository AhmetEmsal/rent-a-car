package kodlama.io.rentacar.core.validators.minCurrentYear;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = MinCurrentYearValidator.class)
@Documented
public @interface MinCurrentYear {

    String message() default "Year must be equal to or after the current year";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}