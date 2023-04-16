package kodlama.io.rentacar.core.utilities.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

@Getter
@Setter
public class ExceptionResult<T extends Exception> {
    private LocalDateTime timestamp;
    private String type;
    private Integer errorCode;
    private String errorMessage;

    public ExceptionResult(Class<T> type, Integer errorCode, String errorMessage) {
        this.timestamp = LocalDateTime.now();
        this.type = Arrays.stream(type.getSimpleName().split("(?=[A-Z])"))
                .map(word -> word.toUpperCase(Locale.ENGLISH))
                .collect(Collectors.joining("_"));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
