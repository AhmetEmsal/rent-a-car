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
import java.util.Optional;

@Service
@AllArgsConstructor
public class BrandManager implements BrandService {
    private final BrandRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public List<GetAllBrandsResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(brand -> modelMapper.map(brand, GetAllBrandsResponse.class))
                .toList();
    }

    @Override
    public CreateBrandResponse add(CreateBrandRequest request) {
        Brand brand = modelMapper.map(request, Brand.class);
        brand.setId(0);
        Brand createdBrand = repository.save(brand);
        return modelMapper.map(createdBrand, CreateBrandResponse.class);
    }

    @Override
    public UpdateBrandResponse update(int id, UpdateBrandRequest request) throws Exception {
        throwErrorIfBrandNotExist(id);
        Brand brand = modelMapper.map(request,Brand.class);
        brand.setId(id);
        repository.save(brand);
        return modelMapper.map(brand,UpdateBrandResponse.class);
    }

    @Override
    public GetBrandResponse getById(int id) throws Exception {
        Optional<Brand> brand = repository.findById(id);
        if(brand.isEmpty()) throwErrorAboutBrandNotExist(id);

        return modelMapper.map(brand.get(), GetBrandResponse.class);
    }

    @Override
    public void delete(int id) throws Exception{
        throwErrorIfBrandNotExist(id);
        repository.deleteById(id);
    }


    private void throwErrorIfBrandNotExist(int id){
        if(repository.existsById(id)) return;
        throwErrorAboutBrandNotExist(id);
    }

    private void throwErrorAboutBrandNotExist(int id){
        throw new RuntimeException("Brand("+id+") not found!");

    }
}
