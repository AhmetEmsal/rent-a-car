package kodlama.io.rentacar.business.rules;

import kodlama.io.rentacar.common.rules.BaseBusinessRules;
import kodlama.io.rentacar.repository.BrandRepository;
import org.springframework.stereotype.Service;

@Service
public class BrandBusinessRules extends BaseBusinessRules {
    private BrandRepository repository;

    public BrandBusinessRules(BrandRepository repository) {
        super("Brand", repository);
    }
}
