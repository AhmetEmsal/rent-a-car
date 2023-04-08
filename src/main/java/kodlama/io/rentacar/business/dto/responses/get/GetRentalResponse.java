package kodlama.io.rentacar.business.dto.responses.get;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class GetRentalResponse {
    private int id;
    private int carId;
    private double dailyPrice;
    private int rentedForDays;
    private final double totalPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
