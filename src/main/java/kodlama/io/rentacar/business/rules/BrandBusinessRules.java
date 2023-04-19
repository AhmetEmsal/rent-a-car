package kodlama.io.rentacar.business.rules;

import kodlama.io.rentacar.common.constants.Messages;
import kodlama.io.rentacar.core.exceptions.business.BusinessErrorCode;
import kodlama.io.rentacar.core.exceptions.business.BusinessException;
import kodlama.io.rentacar.core.rules.BaseBusinessRules;
import kodlama.io.rentacar.entities.Brand;
import kodlama.io.rentacar.repository.bases.vehicle.BrandRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class BrandBusinessRules extends BaseBusinessRules<Brand, BrandRepository> {
    public BrandBusinessRules(BrandRepository repository) {
        super(Messages.Brand.NotExists, repository);
    }

    public void checkIfBrandNameNotExists(String name) {
        if (!getRepository().existsByNameIgnoreCase(name)) return;
        throw new BusinessException(BusinessErrorCode.AlreadyUsed, Messages.Brand.Exists);
    }
}
