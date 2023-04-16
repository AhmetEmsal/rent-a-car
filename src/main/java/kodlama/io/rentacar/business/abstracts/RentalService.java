package kodlama.io.rentacar.business.abstracts;

import kodlama.io.rentacar.business.dto.requests.create.CreateRentalRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateRentalRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateRentalResponse;
import kodlama.io.rentacar.business.dto.responses.get.rentals.GetAllRentalsResponse;
import kodlama.io.rentacar.business.dto.responses.get.rentals.GetRentalResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateRentalResponse;
import kodlama.io.rentacar.core.utilities.exceptions.business.BusinessException;

import java.util.List;

public interface RentalService {
    CreateRentalResponse add(CreateRentalRequest request) throws BusinessException;

    GetRentalResponse getById(int id) throws BusinessException;

    List<GetAllRentalsResponse> getAll();

    UpdateRentalResponse update(int id, UpdateRentalRequest request) throws BusinessException;

    void delete(int id) throws BusinessException;

    UpdateRentalResponse returnCarFromRental(int carId) throws BusinessException;
}
