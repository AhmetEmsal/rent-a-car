package kodlama.io.rentacar.business.dto.responses.create;

import kodlama.io.rentacar.entities.Car;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMaintenanceResponse {
    private int id;
    private Car car;
    private Date dueDate;
    private Date updateAt;
    private Date createdAt;
}
