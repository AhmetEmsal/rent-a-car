package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.abstracts.MaintenanceService;
import kodlama.io.rentacar.business.dto.requests.create.CreateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateCarRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetCarResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateCarResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateMaintenanceResponse;
import kodlama.io.rentacar.entities.Car;
import kodlama.io.rentacar.entities.Maintenance;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.MaintenanceRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class MaintenanceManager implements MaintenanceService {
    private final MaintenanceRepository maintenanceRepository;
    private final CarService carService;
    private ModelMapper modelMapper;

    @Override
    public List<GetAllMaintenanceResponse> getAll() {
        List<Maintenance> maintenances = maintenanceRepository.findAll();
        return maintenances
                .stream()
                .map(maintenance -> modelMapper.map(maintenance, GetAllMaintenanceResponse.class))
                .toList();
    }

    @Override
    public CreateMaintenanceResponse add(CreateMaintenanceRequest request) {
        Maintenance maintenance = modelMapper.map(request, Maintenance.class);
        maintenance.setId(0);
        GetCarResponse carResponse = carService.getById(request.getCarId());
        Car car = modelMapper.map(carResponse, Car.class);
        maintenance.setCar(car);

        // check car state
        checkCarCanBeSentToMaintenance(car);

        // create maintenance
        maintenance.setCreatedAt(new Date());
        Maintenance createdMaintenance = maintenanceRepository.save(maintenance);

        // update car state
        car.setState(State.MAINTENANCE);
        UpdateCarResponse updatedCarResponse = carService.update(car.getId(), modelMapper.map(car, UpdateCarRequest.class));

        // update car in the maintenance object
        maintenance.setCar(modelMapper.map(updatedCarResponse, Car.class));

        return modelMapper.map(createdMaintenance, CreateMaintenanceResponse.class);
    }

    @Override
    public UpdateMaintenanceResponse update(int id, UpdateMaintenanceRequest request) {
        GetMaintenanceResponse oldMaintenanceResponse = getById(id);


        Maintenance maintenance = modelMapper.map(request, Maintenance.class);
        maintenance.setId(id);
        GetCarResponse carResponse = carService.getById(request.getCarId());
        Car car = modelMapper.map(carResponse, Car.class);
        maintenance.setCar(car);
        maintenance.setCreatedAt(oldMaintenanceResponse.getCreatedAt());

        // check if car id change
        /*if(oldMaintenanceResponse.getCar().getId() != request.getCarId()){

            checkCarCanBeSentToMaintenance(car);

            // update new car state
            car.setState(State.MAINTENANCE);
            carService.update(car.getId(), modelMapper.map(car, UpdateCarRequest.class));


            // update old car state
            Car oldCar = oldMaintenanceResponse.getCar();
            oldCar.setState(State.AVAILABLE);
            carService.update(oldCar.getId(), modelMapper.map(oldCar, UpdateCarRequest.class));


        }*/


        maintenance.setUpdateAt(new Date());
        Maintenance updatedMaintenance = maintenanceRepository.save(maintenance);
        return modelMapper.map(updatedMaintenance, UpdateMaintenanceResponse.class);
    }

    @Override
    public GetMaintenanceResponse getById(int id) {
        Optional<Maintenance> maintenance = maintenanceRepository.findById(id);
        if(maintenance.isEmpty()){
            throw new IllegalStateException("Maintenance(#"+ id + ") not found");
        }
        return modelMapper.map(maintenance.get(), GetMaintenanceResponse.class);
    }

    @Override
    public void delete(int id) {
        Maintenance maintenance = maintenanceRepository.findById(id).orElseThrow();
        Car car = maintenance.getCar();

        maintenanceRepository.deleteById(id);

        // update car state
        car.setState(State.AVAILABLE);
        carService.update(car.getId(), modelMapper.map(car, UpdateCarRequest.class));
    }

    private void checkCarCanBeSentToMaintenance(Car car){
        boolean carCanBeSentToMaintenance = car.getState() == State.AVAILABLE;
        if(!carCanBeSentToMaintenance){
            throw new IllegalStateException("Car cannot be sent to maintenance due to it's state: " + car.getState());
        }
    }
}
