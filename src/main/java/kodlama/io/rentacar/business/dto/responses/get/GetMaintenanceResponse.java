package kodlama.io.rentacar.business.dto.responses.get;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetMaintenanceResponse {
    private int id;
    private int carId;
    private Date dueDate;
    private Date updateAt;
    private Date createdAt;

}
