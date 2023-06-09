package kodlama.io.rentacar.business.dto.requests.get;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllCarsRequest {
    private boolean includeMaintenance;
}
