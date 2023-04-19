package kodlama.io.rentacar.business.dto.requests.create;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import kodlama.io.rentacar.common.constants.Regex;
import kodlama.io.rentacar.entities.enums.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCarRequest {
    private int modelId;
    @Pattern(regexp = Regex.Plate, message = "Geçersiz bir plaka girdiniz.")
    private String plate;
    @Min(1998)
    @Max(2023)
    private int modelYear;
    @Min(value = 1)
    private double dailyPrice;
    private State state;

}
