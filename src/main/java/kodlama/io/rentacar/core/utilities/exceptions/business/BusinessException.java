package kodlama.io.rentacar.core.utilities.exceptions.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException {
    private BusinessErrorCode errorCode;

    public BusinessException(BusinessErrorCode errorCode, String text) {
        super(text);
        this.errorCode = errorCode;
    }

}
