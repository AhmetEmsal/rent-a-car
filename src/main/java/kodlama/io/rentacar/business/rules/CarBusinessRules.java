package kodlama.io.rentacar.business.rules;

import kodlama.io.rentacar.common.constants.Messages;
import kodlama.io.rentacar.core.rules.BaseBusinessRules;
import kodlama.io.rentacar.entities.Car;
import kodlama.io.rentacar.repository.bases.vehicle.CarRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class CarBusinessRules extends BaseBusinessRules<Car, CarRepository> {
    public CarBusinessRules(CarRepository repository) {
        super(Messages.Car.NotExists, repository);
    }
}
