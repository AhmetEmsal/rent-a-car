package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.dto.requests.create.CreateCarRequest;
import kodlama.io.rentacar.business.dto.requests.get.GetAllCarsRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateCarRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateCarResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllCarsResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetCarResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateCarResponse;
import kodlama.io.rentacar.entities.Car;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CarManager implements CarService {
    private final CarRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public List<GetAllCarsResponse> getAll(GetAllCarsRequest request) {
        List<Car> cars;
        if(request.isIncludeMaintenance()) cars = repository.findAll();
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
    public UpdateCarResponse update(int id, UpdateCarRequest request) throws Exception{
        throwErrorIfCarNotExist(id);
        Car car = modelMapper.map(request, Car.class);
        car.setId(id);
        Car updatedCar = repository.save(car);
        return modelMapper.map(updatedCar, UpdateCarResponse.class);
    }

    @Override
    public GetCarResponse getById(int id) throws Exception{
        Optional<Car> car = repository.findById(id);
        if(car.isEmpty()) throwErrorAboutCarNotExist(id);

        return modelMapper.map(car.get(), GetCarResponse.class);
    }

    @Override
    public void delete(int id) throws Exception{
        throwErrorIfCarNotExist(id);

        repository.deleteById(id);
    }

    @Override
    public void changeState(int carId, State state) throws Exception {
        Optional<Car> optionalCar = repository.findById(carId);
        if(optionalCar.isEmpty()) throwErrorAboutCarNotExist(carId);

        Car car = optionalCar.get();
        car.setState(state);
        repository.save(car);
    }


    private void throwErrorIfCarNotExist(int id){
        if(repository.existsById(id)) return;
        throwErrorAboutCarNotExist(id);
    }

    private void throwErrorAboutCarNotExist(int id){
        throw new RuntimeException("Car("+id+") not found!");

    }
}
