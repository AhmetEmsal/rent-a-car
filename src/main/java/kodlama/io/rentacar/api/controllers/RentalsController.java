package kodlama.io.rentacar.api.controllers;

import kodlama.io.rentacar.business.abstracts.RentalService;
import kodlama.io.rentacar.business.dto.requests.create.CreateRentalRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateRentalRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateRentalResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllRentalsResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetRentalResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateRentalResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/rentals")
public class RentalsController {
    private final RentalService service;

    @GetMapping
    public List<GetAllRentalsResponse> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public GetRentalResponse getById(@PathVariable int id) throws Exception {
        return service.getById(id);
    }

    @PostMapping
    public CreateRentalResponse add(@RequestBody CreateRentalRequest request) throws Exception {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public UpdateRentalResponse update(@PathVariable int id, @RequestBody UpdateRentalRequest request) throws Exception{
        return service.update(id, request);
    }

    @PutMapping("/return")
    public UpdateRentalResponse returnCarFromRental(@RequestParam(name="car-id", required = true) int carId) throws Exception{
        return service.returnCarFromRental(carId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) throws Exception {
        service.delete(id);
    }
}
