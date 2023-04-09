package kodlama.io.rentacar.business.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kodlama.io.rentacar.core.validators.minCurrentMonth.MinCurrentMonth;
import kodlama.io.rentacar.core.validators.minCurrentYear.MinCurrentYear;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    @NotBlank(message = "Kart numarası boş bırakılamaz")
    @Length(min = 16, max = 16, message = "Kart numarası 16 hanedan oluşmak zorundadır.")
    private String cardNumber;

    @NotBlank
    @Length(min=5)
    private String cardHolder;

    @NotNull
    @MinCurrentYear //@Min(value = 2023)
    private int cardExpirationYear;

    @MinCurrentMonth //@Min(value = 1)
    @Min(value = 12)
    private int cardExpirationMonth;

    @NotBlank
    @Length(min = 3, max = 3)
    private String cardCvv;
}
