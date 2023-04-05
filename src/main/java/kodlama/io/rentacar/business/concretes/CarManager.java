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
import kodlama.io.rentacar.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CarManager implements CarService {
    private final CarRepository carRepository;
    private ModelMapper modelMapper;

    @Override
    public List<GetAllCarsResponse> getAll(GetAllCarsRequest request) {
        List<Car> cars;
        if(request.isWithoutInMaintanance()){
            cars = carRepository.getAllWithoutInMaintenance();
        }
        else {
            cars = carRepository.findAll();
        }

        return cars
                .stream()
                .map(car -> modelMapper.map(car, GetAllCarsResponse.class))
                .toList();
    }

    @Override
    public CreateCarResponse add(CreateCarRequest request) {
        Car car = modelMapper.map(request, Car.class);
        car.setId(0);
        Car createdCar = carRepository.save(car);
        return modelMapper.map(createdCar, CreateCarResponse.class);
    }

    @Override
    public UpdateCarResponse update(int id, UpdateCarRequest request) {
        Car car = modelMapper.map(request, Car.class);
        car.setId(id);
        Car updatedCar = carRepository.save(car);
        return modelMapper.map(updatedCar, UpdateCarResponse.class);
    }

    @Override
    public GetCarResponse getById(int id) {
        Optional<Car> car = carRepository.findById(id);
        if(car.isEmpty()){
            throw new IllegalStateException("Car(#" + id + ") not found");
        }
        return modelMapper.map(car.get(), GetCarResponse.class);
    }

    @Override
    public void delete(int id) {
        carRepository.deleteById(id);
    }

}
