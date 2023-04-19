package kodlama.io.rentacar.api.controllers;


import kodlama.io.rentacar.business.abstracts.BrandService;
import kodlama.io.rentacar.business.dto.requests.create.CreateBrandRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateBrandRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateBrandResponse;
import kodlama.io.rentacar.business.dto.responses.get.brands.GetAllBrandsResponse;
import kodlama.io.rentacar.business.dto.responses.get.brands.GetBrandResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateBrandResponse;
import kodlama.io.rentacar.core.exceptions.business.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/brands")
public class BrandsController {
    private final BrandService service;

    @GetMapping
    public List<GetAllBrandsResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public GetBrandResponse getById(@PathVariable int id) throws BusinessException {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateBrandResponse add(@RequestBody CreateBrandRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public UpdateBrandResponse update(@PathVariable int id, @RequestBody UpdateBrandRequest request) throws BusinessException {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) throws BusinessException {
        service.delete(id);
    }
}

