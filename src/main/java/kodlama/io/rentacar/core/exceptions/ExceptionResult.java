package kodlama.io.rentacar.core.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExceptionResult<T> {
    private LocalDateTime timestamp;
    private String type;
    private Integer errorCode;
    private T message;

    public ExceptionResult(String type, Integer errorCode, T message) {
        this.timestamp = LocalDateTime.now();
        this.type = type;
        /*this.type = Arrays.stream(type.getSimpleName().split("(?=[A-Z])"))
                .map(word -> word.toUpperCase(Locale.ENGLISH))
                .collect(Collectors.joining("_"));*/
        this.errorCode = errorCode;
        this.message = message;
    }
}
