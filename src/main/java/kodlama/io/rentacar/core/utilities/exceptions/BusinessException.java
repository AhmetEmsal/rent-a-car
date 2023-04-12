package kodlama.io.rentacar.core.utilities.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException {
    private BusinessErrorCodes errorCode;
    public BusinessException(BusinessErrorCodes errorCode, String text){
        super(text);
        this.errorCode = errorCode;
    }
}
