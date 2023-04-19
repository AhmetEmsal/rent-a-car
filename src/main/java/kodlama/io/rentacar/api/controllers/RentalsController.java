package kodlama.io.rentacar.api.controllers;

import kodlama.io.rentacar.business.abstracts.RentalService;
import kodlama.io.rentacar.business.dto.requests.create.CreateRentalRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateRentalRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateRentalResponse;
import kodlama.io.rentacar.business.dto.responses.get.rentals.GetAllRentalsResponse;
import kodlama.io.rentacar.business.dto.responses.get.rentals.GetRentalResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateRentalResponse;
import kodlama.io.rentacar.core.exceptions.business.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/rentals")
public class RentalsController {
    private final RentalService service;

    @GetMapping
    public List<GetAllRentalsResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public GetRentalResponse getById(@PathVariable int id) throws BusinessException {
        return service.getById(id);
    }

    @PostMapping
    public CreateRentalResponse add(@RequestBody CreateRentalRequest request) throws BusinessException {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public UpdateRentalResponse update(@PathVariable int id, @RequestBody UpdateRentalRequest request) throws BusinessException {
        return service.update(id, request);
    }

    @PutMapping("/return")
    public UpdateRentalResponse returnCarFromRental(@RequestParam(name = "car-id") int carId) throws BusinessException {
        return service.returnCarFromRental(carId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) throws BusinessException {
        service.delete(id);
    }
}
