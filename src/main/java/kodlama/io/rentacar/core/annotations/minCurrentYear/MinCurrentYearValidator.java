package kodlama.io.rentacar.core.annotations.minCurrentYear;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class MinCurrentYearValidator implements ConstraintValidator<MinCurrentYear, Integer> {
    @Override
    public void initialize(MinCurrentYear minYear) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null values are handled by @NotNull
        }
        int currentYear = Year.now().getValue();
        return value >= currentYear;
    }
}