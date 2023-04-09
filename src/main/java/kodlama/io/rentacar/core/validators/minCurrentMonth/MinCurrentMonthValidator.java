package kodlama.io.rentacar.core.validators.minCurrentMonth;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class MinCurrentMonthValidator implements ConstraintValidator<MinCurrentMonth, Integer> {
    @Override
    public void initialize(MinCurrentMonth minYear) {}

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null values are handled by @NotNull
        }
        int currentMonth =  LocalDate.now().getMonthValue();
        return value >= currentMonth;
    }
}