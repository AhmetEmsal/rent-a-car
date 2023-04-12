package kodlama.io.rentacar.business.rules;

import kodlama.io.rentacar.common.rules.BaseBusinessRules;
import kodlama.io.rentacar.entities.Brand;
import kodlama.io.rentacar.repository.BrandRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class BrandBusinessRules extends BaseBusinessRules<Brand, BrandRepository> {
    public BrandBusinessRules(BrandRepository repository) {
        super("Brand", repository);
    }
}
