package kodlama.io.rentacar.business.rules;

import kodlama.io.rentacar.common.rules.BaseBusinessRules;
import kodlama.io.rentacar.core.utilities.exceptions.BusinessErrorCodes;
import kodlama.io.rentacar.core.utilities.exceptions.BusinessException;
import kodlama.io.rentacar.entities.Car;
import kodlama.io.rentacar.entities.Rental;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.RentalRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class RentalBusinessRules extends BaseBusinessRules<Rental, RentalRepository> {
    public RentalBusinessRules(RentalRepository repository){
        super("Rental", repository);
    }

    public void checkIfCarCannotSendToRental(Car car) throws BusinessException {
        boolean carCanBeSentToRental = car.getState().equals(State.AVAILABLE);
        if(carCanBeSentToRental) return;
        throw new BusinessException(BusinessErrorCodes.UnsuitableState, "Car cannot be sent to rental due to it's state: " + car.getState());
    }
    public void checkIfCarIsNotUnderRental(int carId){
        if(getRepository().existsByCarIdAndRentalIsContinue(carId)) return;
        throw new BusinessException(BusinessErrorCodes.UnsuitableState, "A car that is under rental and has an ID of "+ carId+ " could not be found.");
    }
}
