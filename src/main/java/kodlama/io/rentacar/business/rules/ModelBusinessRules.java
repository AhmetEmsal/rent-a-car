package kodlama.io.rentacar.business.rules;

import kodlama.io.rentacar.common.constants.Messages;
import kodlama.io.rentacar.core.exceptions.business.BusinessErrorCode;
import kodlama.io.rentacar.core.exceptions.business.BusinessException;
import kodlama.io.rentacar.core.rules.BaseBusinessRules;
import kodlama.io.rentacar.entities.Model;
import kodlama.io.rentacar.repository.bases.vehicle.ModelRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class ModelBusinessRules extends BaseBusinessRules<Model, ModelRepository> {
    public ModelBusinessRules(ModelRepository repository) {
        super(Messages.Model.NotExists, repository);
    }

    public void checkIfBrandNameNotExist(String name) {
        if (!getRepository().existsByNameIgnoreCase(name)) return;
        throw new BusinessException(BusinessErrorCode.AlreadyUsed, Messages.Model.Exists);
    }
}
