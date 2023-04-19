package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.abstracts.MaintenanceService;
import kodlama.io.rentacar.business.dto.requests.create.CreateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.get.maintenances.GetAllMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.get.maintenances.GetMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateMaintenanceResponse;
import kodlama.io.rentacar.business.rules.MaintenanceBusinessRules;
import kodlama.io.rentacar.core.exceptions.business.BusinessException;
import kodlama.io.rentacar.entities.Car;
import kodlama.io.rentacar.entities.Maintenance;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.functional.vehicle.MaintenanceRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MaintenanceManager implements MaintenanceService {
    private final MaintenanceRepository repository;
    private final CarService carService;
    private final ModelMapper modelMapper;
    private final MaintenanceBusinessRules businessRules;

    @Override
    public List<GetAllMaintenanceResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(maintenance -> modelMapper.map(maintenance, GetAllMaintenanceResponse.class))
                .toList();
    }

    @Override
    public CreateMaintenanceResponse add(CreateMaintenanceRequest request) throws BusinessException {

        // get car
        Car car;
        {
            int carId = request.getCarId();
            var carResponse = carService.getById(carId);
            car = modelMapper.map(carResponse, Car.class);
        }

        // check car state
        businessRules.checkIfCarCanBeSentToMaintenance(car.getState());

        // update car state
        carService.changeState(car.getId(), State.MAINTENANCE);


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
    public UpdateMaintenanceResponse update(int id, UpdateMaintenanceRequest request) throws BusinessException {
        // map to maintenance
        Maintenance maintenance = modelMapper.map(request, Maintenance.class);

        GetMaintenanceResponse oldMaintenanceResponse = getById(id);

        //
        final int oldCarId = oldMaintenanceResponse.getCarId(),
                newCarId = request.getCarId();

        // check if car id change
        if (oldCarId != newCarId) {

            // get new car
            Car newCar;
            {
                var carResponse = carService.getById(newCarId);
                newCar = modelMapper.map(carResponse, Car.class);
            }

            // check new car state
            businessRules.checkIfCarCanBeSentToMaintenance(newCar.getState());

            // update new car state
            State newCarOldState = newCar.getState();
            carService.changeState(newCarId, State.MAINTENANCE);

            // update old car state
            try {
                carService.changeState(oldCarId, State.AVAILABLE);
            } catch (BusinessException BusinessException) {
                // transaction back
                carService.changeState(newCarId, newCarOldState);
                throw BusinessException;
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
    public GetMaintenanceResponse getById(int id) throws BusinessException {
        Maintenance maintenance = businessRules.checkIfEntityExistsByIdThenReturn(id);
        return modelMapper.map(maintenance, GetMaintenanceResponse.class);
    }

    @Override
    public void delete(int id) throws BusinessException {
        Maintenance maintenance = businessRules.checkIfEntityExistsByIdThenReturn(id);

        // update car state
        carService.changeState(maintenance.getCar().getId(), State.AVAILABLE);

        repository.deleteById(id);
    }

    @Override
    public UpdateMaintenanceResponse returnCarFromMaintenance(int carId) throws BusinessException {
        businessRules.checkIfCarIsUnderMaintenance(carId);

        Maintenance maintenance = repository.findByCarIdAndIsCompletedIsFalse(carId);
        maintenance.setCompleted(true);
        maintenance.setEndDate(LocalDateTime.now());

        carService.changeState(carId, State.AVAILABLE);

        Maintenance savedMaintenance = repository.save(maintenance);
        return modelMapper.map(savedMaintenance, UpdateMaintenanceResponse.class);
    }

}
