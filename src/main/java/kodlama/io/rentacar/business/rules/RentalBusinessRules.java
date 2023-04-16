package kodlama.io.rentacar.business.rules;

import kodlama.io.rentacar.common.constants.Messages;
import kodlama.io.rentacar.core.rules.BaseBusinessRules;
import kodlama.io.rentacar.core.utilities.exceptions.business.BusinessErrorCode;
import kodlama.io.rentacar.core.utilities.exceptions.business.BusinessException;
import kodlama.io.rentacar.entities.Rental;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.functional.vehicle.RentalRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class RentalBusinessRules extends BaseBusinessRules<Rental, RentalRepository> {
    public RentalBusinessRules(RentalRepository repository) {
        super(Messages.Rental.NotExists, repository);
    }

    public void checkIfCarCanBeSentToRental(State state) throws BusinessException {
        switch (state) {

            case AVAILABLE -> {
                return;
            }
            case RENTED -> {
                throw new BusinessException(BusinessErrorCode.UnsuitableState, Messages.Rental.CarExists);
            }
            case MAINTENANCE -> {
                throw new BusinessException(BusinessErrorCode.UnsuitableState, Messages.Rental.CarIsMaintenanend);
            }
        }
    }

    public void checkIfCarIsUnderRental(int carId) {
        if (getRepository().existsByCarIdAndRentalIsContinue(carId)) return;
        throw new BusinessException(BusinessErrorCode.UnsuitableState, Messages.Rental.CarNotExists);
    }
}
