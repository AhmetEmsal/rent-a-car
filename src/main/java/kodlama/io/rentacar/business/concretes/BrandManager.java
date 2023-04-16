package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.BrandService;
import kodlama.io.rentacar.business.dto.requests.create.CreateBrandRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateBrandRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateBrandResponse;
import kodlama.io.rentacar.business.dto.responses.get.brands.GetAllBrandsResponse;
import kodlama.io.rentacar.business.dto.responses.get.brands.GetBrandResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateBrandResponse;
import kodlama.io.rentacar.business.rules.BrandBusinessRules;
import kodlama.io.rentacar.core.utilities.exceptions.business.BusinessException;
import kodlama.io.rentacar.entities.Brand;
import kodlama.io.rentacar.repository.bases.vehicle.BrandRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BrandManager implements BrandService {
    private final BrandRepository repository;
    private final ModelMapper modelMapper;
    private final BrandBusinessRules businessRules;

    @Override
    public List<GetAllBrandsResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(brand -> modelMapper.map(brand, GetAllBrandsResponse.class))
                .toList();
    }

    @Override
    public CreateBrandResponse add(CreateBrandRequest request) {
        businessRules.checkIfBrandNameNotExists(request.getName());
        Brand brand = modelMapper.map(request, Brand.class);
        brand.setId(0);
        Brand createdBrand = repository.save(brand);
        return modelMapper.map(createdBrand, CreateBrandResponse.class);
    }

    @Override
    public UpdateBrandResponse update(int id, UpdateBrandRequest request) throws BusinessException {
        Brand oldBrand = businessRules.checkIfEntityExistsByIdThenReturn(id);

        final boolean wantToChangeBrandName = !oldBrand.getName().equalsIgnoreCase(request.getName());
        if (wantToChangeBrandName)
            businessRules.checkIfBrandNameNotExists(request.getName());

        Brand brand = modelMapper.map(request, Brand.class);
        brand.setId(id);
        repository.save(brand);
        return modelMapper.map(brand, UpdateBrandResponse.class);
    }

    @Override
    public GetBrandResponse getById(int id) throws BusinessException {
        Brand brand = businessRules.checkIfEntityExistsByIdThenReturn(id);
        return modelMapper.map(brand, GetBrandResponse.class);
    }

    @Override
    public void delete(int id) throws BusinessException {
        businessRules.checkIfEntityExistsById(id);
        repository.deleteById(id);
    }
}
