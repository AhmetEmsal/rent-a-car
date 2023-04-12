package kodlama.io.rentacar.business.rules;

import kodlama.io.rentacar.common.rules.BaseBusinessRules;
import kodlama.io.rentacar.entities.Car;
import kodlama.io.rentacar.repository.CarRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class CarBusinessRules extends BaseBusinessRules<Car, CarRepository> {
    public CarBusinessRules(CarRepository repository){
        super("Car", repository);
    }
}
