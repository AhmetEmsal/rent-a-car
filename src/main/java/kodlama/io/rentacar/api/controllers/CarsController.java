package kodlama.io.rentacar.api.controllers;

import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.dto.requests.create.CreateCarRequest;
import kodlama.io.rentacar.business.dto.requests.get.GetAllCarsRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateCarRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateCarResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllCarsResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetCarResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateCarResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/api/cars")
public class CarsController {
    private final CarService service;

    @GetMapping
    public List<GetAllCarsResponse> getAll(@RequestParam(name="includeMaintenance", required = false, defaultValue = "true") boolean includeMaintenance) {
        return service.getAll(new GetAllCarsRequest(includeMaintenance));
    }

    @GetMapping("/{id}")
    public GetCarResponse getById(@PathVariable int id) throws Exception {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateCarResponse add(@RequestBody CreateCarRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public UpdateCarResponse update(@PathVariable int id, @RequestBody UpdateCarRequest request) throws Exception {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) throws Exception {
        service.delete(id);
    }

/*    @PutMapping("/send-maintanance/{id}")
    public UpdateStateCarResponse sendMaintanance(@PathVariable int id){
        return service.sendMaintanance(id);
    }

    @PutMapping("/receive-maintanance/{id}")
    public UpdateStateCarResponse receiveMaintanance(@PathVariable int id){
        return service.receiveMaintanance(id);
    }*/
}
