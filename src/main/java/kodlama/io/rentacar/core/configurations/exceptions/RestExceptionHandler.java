package kodlama.io.rentacar.core.configurations.exceptions;


import jakarta.validation.ValidationException;
import kodlama.io.rentacar.common.constants.ExceptionTypes;
import kodlama.io.rentacar.core.exceptions.ExceptionResult;
import kodlama.io.rentacar.core.exceptions.business.BusinessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST) // 400
    public ExceptionResult<Object> handleMethodArgumentNotValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> validationErrors = new HashMap<>();

        for (FieldError fieldError : exception.getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ExceptionResult<>(
                ExceptionTypes.Exception.Validation,
                null,
                validationErrors
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // 422
    public ExceptionResult<Object> handleValidationException(ValidationException exception) {
        return new ExceptionResult<>(ExceptionTypes.Exception.Validation, null, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT) // 409
    public ExceptionResult<Object> handleDataIntegrityException(DataIntegrityViolationException exception) {
        return new ExceptionResult<>(ExceptionTypes.Exception.DataIntegrityViolation, null, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY) // 422
    public ExceptionResult<Object> handleBusinessException(BusinessException businessException) {
        return new ExceptionResult<>(
                ExceptionTypes.Exception.Business,
                businessException.getErrorCode().getCode(),
                businessException.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR) // 500
    public ExceptionResult<Object> handleBusinessException(RuntimeException runtimeException) {
        return new ExceptionResult<>(
                ExceptionTypes.Exception.Runtime,
                null,
                runtimeException.getMessage()
        );
    }
}
