package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.BrandService;
import kodlama.io.rentacar.business.dto.requests.create.CreateBrandRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateBrandRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateBrandResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllBrandsResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetBrandResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateBrandResponse;
import kodlama.io.rentacar.entities.Brand;
import kodlama.io.rentacar.repository.BrandRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BrandManager implements BrandService {
    private final BrandRepository brandRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<GetAllBrandsResponse> getAll() {
        List<Brand> brands = brandRepository.findAll();
        return brands
                .stream()
                .map(brand -> modelMapper.map(brand, GetAllBrandsResponse.class))
                .toList();
    }

    @Override
    public CreateBrandResponse add(CreateBrandRequest request) {
        Brand brand = modelMapper.map(request, Brand.class);
        brand.setId(0);
        Brand createdBrand = brandRepository.save(brand);
        return modelMapper.map(createdBrand, CreateBrandResponse.class);
    }

    @Override
    public UpdateBrandResponse update(int id, UpdateBrandRequest request) {
        Brand brand = modelMapper.map(request,Brand.class);
        brand.setId(id);
        brandRepository.save(brand);
        return modelMapper.map(brand,UpdateBrandResponse.class);
    }

    @Override
    public GetBrandResponse getById(int id) {
        Brand brand = brandRepository.findById(id).orElseThrow();
        return modelMapper.map(brand, GetBrandResponse.class);
    }

    @Override
    public void delete(int id) {
        brandRepository.deleteById(id);
    }
}
