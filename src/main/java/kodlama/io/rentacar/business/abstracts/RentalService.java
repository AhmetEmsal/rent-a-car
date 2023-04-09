package kodlama.io.rentacar.business.abstracts;

import kodlama.io.rentacar.business.dto.requests.create.CreateRentalRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateRentalRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateRentalResponse;
import kodlama.io.rentacar.business.dto.responses.get.rentals.GetAllRentalsResponse;
import kodlama.io.rentacar.business.dto.responses.get.rentals.GetRentalResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateRentalResponse;

import java.util.List;

public interface RentalService {
    CreateRentalResponse add(CreateRentalRequest request) throws Exception;

    GetRentalResponse getById(int id) throws Exception;
    List<GetAllRentalsResponse> getAll();

    UpdateRentalResponse update(int id, UpdateRentalRequest request) throws Exception;

    void delete(int id) throws Exception;

    UpdateRentalResponse returnCarFromRental(int carId) throws Exception;
}
