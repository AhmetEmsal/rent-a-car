package kodlama.io.rentacar.business.rules;

import kodlama.io.rentacar.common.rules.BaseBusinessRules;
import kodlama.io.rentacar.core.utilities.exceptions.BusinessErrorCodes;
import kodlama.io.rentacar.core.utilities.exceptions.BusinessException;
import kodlama.io.rentacar.entities.Car;
import kodlama.io.rentacar.entities.Maintenance;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.MaintenanceRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class MaintenanceBusinessRules extends BaseBusinessRules<Maintenance, MaintenanceRepository> {
    public MaintenanceBusinessRules(MaintenanceRepository repository){
        super("Maintenance", repository);
    }

    public void checkIfCarCannotSendToMaintenance(Car car) throws BusinessException {
        boolean carCanBeSentToMaintenance = car.getState().equals(State.AVAILABLE);
        if(carCanBeSentToMaintenance) return;
        throw new BusinessException(BusinessErrorCodes.UnsuitableState, "Car cannot be sent to maintenance due to it's state: " + car.getState());
    }

    public void checkIfCarIsNotUnderMaintenance(int carId){
        if(getRepository().existsByCarIdAndIsCompletedIsFalse(carId)) return;
        throw new BusinessException(BusinessErrorCodes.UnsuitableState, "A car that is under maintenance and has an ID of "+ carId+ " could not be found.");
    }
}
