package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.abstracts.MaintenanceService;
import kodlama.io.rentacar.business.dto.requests.create.CreateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetCarResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateMaintenanceResponse;
import kodlama.io.rentacar.entities.Car;
import kodlama.io.rentacar.entities.Maintenance;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.MaintenanceRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MaintenanceManager implements MaintenanceService {
    private final MaintenanceRepository repository;
    private final CarService carService;
    private final ModelMapper modelMapper;

    @Override
    public List<GetAllMaintenanceResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(maintenance -> modelMapper.map(maintenance, GetAllMaintenanceResponse.class))
                .toList();
    }

    @Override
    public CreateMaintenanceResponse add(CreateMaintenanceRequest request) throws Exception {

        // get car id
        int carId = request.getCarId();

        // check car state
        throwErrorIfCarCannotSentToMaintenance(carId);

        // update car state
        carService.changeState(carId, State.MAINTENANCE);


        // map to maintenance
        Maintenance maintenance = modelMapper.map(request, Maintenance.class);

        // create and save maintenance
        LocalDateTime now = LocalDateTime.now();
        maintenance.setId(0);
        maintenance.setCompleted(false);
        maintenance.setStartDate(now);
        maintenance.setEndDate(request.getDueDate());
        maintenance.setUpdatedAt(null);
        maintenance.setCreatedAt(now);
        Maintenance createdMaintenance = repository.save(maintenance);
        return modelMapper.map(createdMaintenance, CreateMaintenanceResponse.class);
    }

    @Override
    public UpdateMaintenanceResponse update(int id, UpdateMaintenanceRequest request) throws Exception {
        // map to maintenance
        Maintenance maintenance = modelMapper.map(request, Maintenance.class);

        GetMaintenanceResponse oldMaintenanceResponse = getById(id);

        //
        final int oldCarId = oldMaintenanceResponse.getCarId(),
                  newCarId = request.getCarId();

        // check if car id change
        if(oldCarId != newCarId){
            throwErrorIfCarCannotSentToMaintenance(newCarId);

            // update new car state
            GetCarResponse carResponse = carService.getById(newCarId);
            Car newCar =  modelMapper.map(carResponse, Car.class);
            State newCarOldState = newCar.getState();
            carService.changeState(newCarId, State.MAINTENANCE);

            // update old car state
            try {
                carService.changeState(oldCarId,State.AVAILABLE);
            }
            catch (Exception exception){
                // transaction back
                carService.changeState(newCarId, newCarOldState);
                throw exception;
            }

            maintenance.setCar(newCar);

        }


        // update necessary fields
        maintenance.setId(id);
        maintenance.setUpdatedAt(LocalDateTime.now());
        maintenance.setCreatedAt(oldMaintenanceResponse.getCreatedAt());

        Maintenance updatedMaintenance = repository.save(maintenance);
        return modelMapper.map(updatedMaintenance, UpdateMaintenanceResponse.class);
    }

    @Override
    public GetMaintenanceResponse getById(int id) throws Exception{
        Optional<Maintenance> maintenance = repository.findById(id);
        if(maintenance.isEmpty()) throwErrorAboutMaintenanceNotExist(id);

        return modelMapper.map(maintenance.get(), GetMaintenanceResponse.class);
    }

    @Override
    public void delete(int id) throws Exception {
        Optional<Maintenance> maintenanceOptional = repository.findById(id);
        if(maintenanceOptional.isEmpty()) throwErrorAboutMaintenanceNotExist(id);

        Maintenance maintenance = maintenanceOptional.get();

        // update car state
        carService.changeState(maintenance.getCar().getId(), State.AVAILABLE);

        repository.deleteById(id);
    }

    @Override
    public GetMaintenanceResponse returnCarFromMaintenance(int carId) {
        return null;
    }

    private void throwErrorIfCarCannotSentToMaintenance(int carId) throws Exception {
        var carResponse = carService.getById(carId);
        var car = modelMapper.map(carResponse, Car.class);
        boolean carCanBeSentToMaintenance = car.getState().equals(State.AVAILABLE);
        if(carCanBeSentToMaintenance) return;
        throw new IllegalStateException("Car cannot be sent to maintenance due to it's state: " + car.getState());
    }


    private void throwErrorAboutMaintenanceNotExist(int id){
        throw new RuntimeException("Maintenance("+id+") not found!");
    }

}
