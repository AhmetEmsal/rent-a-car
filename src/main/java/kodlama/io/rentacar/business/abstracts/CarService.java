package kodlama.io.rentacar.business.abstracts;

import kodlama.io.rentacar.business.dto.requests.create.CreateCarRequest;
import kodlama.io.rentacar.business.dto.requests.get.GetAllCarsRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateCarRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateCarResponse;
import kodlama.io.rentacar.business.dto.responses.get.cars.GetAllCarsResponse;
import kodlama.io.rentacar.business.dto.responses.get.cars.GetCarResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateCarResponse;
import kodlama.io.rentacar.core.utilities.exceptions.BusinessException;
import kodlama.io.rentacar.entities.enums.State;

import java.util.List;

public interface CarService {
    List<GetAllCarsResponse> getAll(GetAllCarsRequest request);

    CreateCarResponse add(CreateCarRequest request);

    UpdateCarResponse update(int id, UpdateCarRequest request) throws BusinessException;

    GetCarResponse getById(int id) throws BusinessException;

    void delete(int id) throws BusinessException;
    void changeState(int carId, State state) throws BusinessException;

}
