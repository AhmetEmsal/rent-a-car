package kodlama.io.rentacar.business.dto.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kodlama.io.rentacar.core.validators.futureMonthAndYear.FutureMonthAndYear;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FutureMonthAndYear(yearFieldName = "cardExpirationYear", monthFieldName = "cardExpirationMonth", message = "Kart geçerlilik tarihi geçmiş bir tarih olamaz.")
public class PaymentRequest {
    @NotBlank(message = "Kart numarası boş bırakılamaz")
    @Length(min = 16, max = 16, message = "Kart numarası 16 hanedan oluşmak zorundadır.")
    private String cardNumber;

    @NotBlank
    @Length(min = 5)
    private String cardHolder;

    @NotNull
    private int cardExpirationYear;

    @Max(value = 12)
    private int cardExpirationMonth;

    @NotBlank
    @Length(min = 3, max = 3)
    private String cardCvv;
}