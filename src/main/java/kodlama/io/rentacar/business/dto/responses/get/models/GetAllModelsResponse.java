package kodlama.io.rentacar.business.dto.responses.get.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllModelsResponse {
    private int id;
    private String name;
    private String brandName;
}
