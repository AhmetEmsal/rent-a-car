package kodlama.io.rentacar.core.validators.minCurrentDate;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.MonthDay;
import java.time.Year;

public class MinCurrentDateValidator implements ConstraintValidator<MinCurrentDate, Object> {
    private String yearFieldName;
    private String monthFieldName;

    @Override
    public void initialize(MinCurrentDate annotation) {
        this.yearFieldName = annotation.yearFieldName();
        this.monthFieldName = annotation.monthFieldName();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        int year = getFieldIntValueUsingGetter(object, yearFieldName);
        int month = getFieldIntValueUsingGetter(object, monthFieldName);

        int currentYear = Year.now().getValue();
        int currentMonth = MonthDay.now().getMonthValue();


        return year >= currentYear && (year > currentYear || month >= currentMonth);
    }

    private int getFieldIntValueUsingGetter(Object object, String fieldName) {
        try {
            // Convert field name to getter method name
            String getterMethodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

            // Get the Method object for the getter method
            Method getterMethod = object.getClass().getMethod(getterMethodName);

            // Invoke the getter method and return the value
            return (int) getterMethod.invoke(object);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("Error accessing getter method for field " + fieldName + " in " + object.getClass());
        }
    }
}