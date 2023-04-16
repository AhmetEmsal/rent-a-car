package kodlama.io.rentacar.core.configurations.exceptions;


import kodlama.io.rentacar.core.utilities.exceptions.ExceptionResult;
import kodlama.io.rentacar.core.utilities.exceptions.business.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY) // 422
    public ExceptionResult<BusinessException> handleBusinessException(BusinessException businessException) {
        return new ExceptionResult<>(
                BusinessException.class,
                businessException.getErrorCode().getCode(),
                businessException.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR) // 500
    public ExceptionResult<RuntimeException> handleBusinessException(RuntimeException runtimeException) {
        return new ExceptionResult<>(
                RuntimeException.class,
                null,
                runtimeException.getMessage()
        );
    }
}
