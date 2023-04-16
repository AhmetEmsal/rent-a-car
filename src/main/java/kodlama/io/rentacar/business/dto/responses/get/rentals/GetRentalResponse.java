package kodlama.io.rentacar.business.dto.responses.get.rentals;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class GetRentalResponse {
    private final double totalPrice;
    private int id;
    private int carId;
    private double dailyPrice;
    private int rentedForDays;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
