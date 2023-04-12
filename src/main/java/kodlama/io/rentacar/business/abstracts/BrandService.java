package kodlama.io.rentacar.business.abstracts;

import kodlama.io.rentacar.business.dto.requests.create.CreateBrandRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateBrandRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateBrandResponse;
import kodlama.io.rentacar.business.dto.responses.get.brands.GetAllBrandsResponse;
import kodlama.io.rentacar.business.dto.responses.get.brands.GetBrandResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateBrandResponse;
import kodlama.io.rentacar.core.utilities.exceptions.BusinessException;

import java.util.List;


public interface BrandService {
    List<GetAllBrandsResponse> getAll();

    CreateBrandResponse add(CreateBrandRequest request);

    UpdateBrandResponse update(int id, UpdateBrandRequest request) throws BusinessException;

    GetBrandResponse getById(int id) throws BusinessException;

    void delete(int id) throws BusinessException;


}
