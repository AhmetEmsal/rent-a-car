package kodlama.io.rentacar.business.rules;

import kodlama.io.rentacar.common.constants.Messages;
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
}
