package kodlama.io.rentacar.business.rules;

import kodlama.io.rentacar.common.constants.Messages;
import kodlama.io.rentacar.core.rules.BaseBusinessRules;
import kodlama.io.rentacar.core.utilities.exceptions.business.BusinessErrorCode;
import kodlama.io.rentacar.core.utilities.exceptions.business.BusinessException;
import kodlama.io.rentacar.entities.Maintenance;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.functional.vehicle.MaintenanceRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class MaintenanceBusinessRules extends BaseBusinessRules<Maintenance, MaintenanceRepository> {
    public MaintenanceBusinessRules(MaintenanceRepository repository) {
        super(Messages.Maintenance.NotExists, repository);
    }

    public void checkIfCarCanBeSentToMaintenance(State state) throws BusinessException {
        switch (state) {
            case AVAILABLE -> {
                return;
            }
            case RENTED -> {
                throw new BusinessException(BusinessErrorCode.UnsuitableState, Messages.Maintenance.CarIsRented);
            }
            case MAINTENANCE -> {
                throw new BusinessException(BusinessErrorCode.UnsuitableState, Messages.Maintenance.CarExists);
            }
        }
    }

    public void checkIfCarIsUnderMaintenance(int carId) {
        if (getRepository().existsByCarIdAndIsCompletedIsFalse(carId)) return;
        throw new BusinessException(BusinessErrorCode.UnsuitableState, Messages.Maintenance.CarNotExists);
    }
}
