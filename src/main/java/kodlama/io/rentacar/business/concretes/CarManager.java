package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.dto.requests.create.CreateCarRequest;
import kodlama.io.rentacar.business.dto.requests.get.GetAllCarsRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateCarRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateCarResponse;
import kodlama.io.rentacar.business.dto.responses.get.cars.GetAllCarsResponse;
import kodlama.io.rentacar.business.dto.responses.get.cars.GetCarResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateCarResponse;
import kodlama.io.rentacar.business.rules.CarBusinessRules;
import kodlama.io.rentacar.core.utilities.exceptions.business.BusinessException;
import kodlama.io.rentacar.entities.Car;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.bases.vehicle.CarRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarManager implements CarService {
    private final CarRepository repository;
    private final ModelMapper modelMapper;
    private final CarBusinessRules businessRules;

    @Override
    public List<GetAllCarsResponse> getAll(GetAllCarsRequest request) {
        List<Car> cars;
        if (request.isIncludeMaintenance()) cars = repository.findAll();
        else cars = repository.findAllByStateIsNot(State.MAINTENANCE);

        return cars
                .stream()
                .map(car -> modelMapper.map(car, GetAllCarsResponse.class))
                .toList();
    }

    @Override
    public CreateCarResponse add(CreateCarRequest request) {
        Car car = modelMapper.map(request, Car.class);
        car.setId(0);
        Car createdCar = repository.save(car);
        return modelMapper.map(createdCar, CreateCarResponse.class);
    }

    @Override
    public UpdateCarResponse update(int id, UpdateCarRequest request) throws BusinessException {
        businessRules.checkIfEntityExistsById(id);
        Car car = modelMapper.map(request, Car.class);
        car.setId(id);
        Car updatedCar = repository.save(car);
        return modelMapper.map(updatedCar, UpdateCarResponse.class);
    }

    @Override
    public GetCarResponse getById(int id) throws BusinessException {
        Car car = businessRules.checkIfEntityExistsByIdThenReturn(id);
        return modelMapper.map(car, GetCarResponse.class);
    }

    @Override
    public void delete(int id) throws BusinessException {
        businessRules.checkIfEntityExistsById(id);
        repository.deleteById(id);
    }

    @Override
    public void changeState(int carId, State state) throws BusinessException {
        Car car = businessRules.checkIfEntityExistsByIdThenReturn(carId);
        car.setState(state);
        repository.save(car);
    }
}
