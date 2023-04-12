package kodlama.io.rentacar;

import kodlama.io.rentacar.core.utilities.exceptions.BusinessException;
import kodlama.io.rentacar.core.utilities.exceptions.ExceptionResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@SpringBootApplication
@RestControllerAdvice
public class RentACarApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentACarApplication.class, args);
	}

	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ExceptionResponse handleBusinessException(BusinessException businessException){
		var response = new ExceptionResponse(
				businessException.getErrorCode().getCode(),
				businessException.getMessage()
		);
		return response;
	}

}
