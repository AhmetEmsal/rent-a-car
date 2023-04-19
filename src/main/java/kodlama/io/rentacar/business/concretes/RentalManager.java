package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.abstracts.InvoiceService;
import kodlama.io.rentacar.business.abstracts.PaymentService;
import kodlama.io.rentacar.business.abstracts.RentalService;
import kodlama.io.rentacar.business.dto.requests.create.CreateInvoicesRequest;
import kodlama.io.rentacar.business.dto.requests.create.CreateRentalRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateRentalRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateRentalResponse;
import kodlama.io.rentacar.business.dto.responses.get.cars.GetCarResponse;
import kodlama.io.rentacar.business.dto.responses.get.rentals.GetAllRentalsResponse;
import kodlama.io.rentacar.business.dto.responses.get.rentals.GetRentalResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateRentalResponse;
import kodlama.io.rentacar.business.rules.RentalBusinessRules;
import kodlama.io.rentacar.common.dto.CreateRentalPaymentRequest;
import kodlama.io.rentacar.core.exceptions.business.BusinessException;
import kodlama.io.rentacar.entities.Car;
import kodlama.io.rentacar.entities.Rental;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.functional.vehicle.RentalRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RentalManager implements RentalService {
    private final RentalRepository repository;
    private final CarService carService;
    private final PaymentService paymentService;
    private final ModelMapper modelMapper;
    private final RentalBusinessRules businessRules;
    private final InvoiceService invoiceService;

    @Override
    public CreateRentalResponse add(CreateRentalRequest request) throws BusinessException {

        // get car by car id
        final Car car;
        {
            var carResponse = carService.getById(request.getCarId());
            car = modelMapper.map(carResponse, Car.class);
        }

        // check car state
        businessRules.checkIfCarCanBeSentToRental(car.getState());

        // map to rental
        Rental rental = modelMapper.map(request, Rental.class);

        // calc total price
        final double totalPrice = rental.getRentedForDays() * rental.getDailyPrice();

        // payment
        CreateRentalPaymentRequest paymentRequest = new CreateRentalPaymentRequest();
        modelMapper.map(request.getPaymentRequest(), paymentRequest);
        paymentRequest.setPrice(totalPrice);
        paymentService.processRentalPayment(paymentRequest);

        // update car state
        carService.changeState(car.getId(), State.RENTED);

        // create and save rental
        LocalDateTime now = LocalDateTime.now();
        rental.setId(0);
        rental.setTotalPrice(totalPrice);
        rental.setEndDate(rental.getStartDate().plusDays(rental.getRentedForDays()));
        rental.setUpdatedAt(null);
        rental.setCreatedAt(now);
        Rental createdRental = repository.save(rental);

        invoiceService.add(new CreateInvoicesRequest(
                createdRental.getId(),
                "",// cardHolder: name surname
                car.getModel().getName(),
                car.getModel().getBrand().getName(),
                car.getPlate(),
                car.getModelYear(),
                car.getDailyPrice(),
                totalPrice,
                rental.getRentedForDays(),
                LocalDateTime.now()
        ));

        return modelMapper.map(createdRental, CreateRentalResponse.class);
    }

    @Override
    public List<GetAllRentalsResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(rental -> modelMapper.map(rental, GetAllRentalsResponse.class))
                .toList();
    }

    @Override
    public GetRentalResponse getById(int id) throws BusinessException {
        Rental rental = businessRules.checkIfEntityExistsByIdThenReturn(id);
        return modelMapper.map(rental, GetRentalResponse.class);
    }

    @Override
    public UpdateRentalResponse update(int id, UpdateRentalRequest request) throws BusinessException {
        // map to rental
        Rental rental = modelMapper.map(request, Rental.class);

        GetRentalResponse oldRentalResponse = getById(id);
        //
        final int oldCarId = oldRentalResponse.getCarId(),
                newCarId = request.getCarId();

        // check if car id change
        if (oldCarId != newCarId) {

            // get new car by car id
            Car newCar;
            {
                GetCarResponse carResponse = carService.getById(newCarId);
                newCar = modelMapper.map(carResponse, Car.class);
            }

            // check state of the new car
            businessRules.checkIfCarCanBeSentToRental(newCar.getState());

            // update new car state
            State newCarOldState = newCar.getState();
            carService.changeState(newCarId, State.RENTED);

            // update old car state
            try {
                carService.changeState(oldCarId, State.AVAILABLE);
            } catch (BusinessException BusinessException) {
                // transaction back
                carService.changeState(newCarId, newCarOldState);
                throw BusinessException;
            }

            rental.setCar(newCar);

        }


        // update necessary fields
        rental.setId(id);
        rental.setTotalPrice(rental.getDailyPrice() * rental.getRentedForDays());
        rental.setEndDate(rental.getStartDate().plusDays(rental.getRentedForDays()));
        rental.setUpdatedAt(LocalDateTime.now());
        rental.setCreatedAt(oldRentalResponse.getCreatedAt());

        Rental updatedRental = repository.save(rental);
        return modelMapper.map(updatedRental, UpdateRentalResponse.class);
    }

    @Override
    public void delete(int id) throws BusinessException {
        Rental rental = businessRules.checkIfEntityExistsByIdThenReturn(id);

        // update car state
        carService.changeState(rental.getCar().getId(), State.AVAILABLE);

        repository.deleteById(id);
    }

    @Override
    public UpdateRentalResponse returnCarFromRental(int carId) throws BusinessException {
        businessRules.checkIfCarIsUnderRental(carId);

        Rental rental = repository.findRentalByCarIdAndRentedIsContinue(carId);
        //rental.setCompleted(true);

        carService.changeState(carId, State.AVAILABLE);


        Rental savedRental = repository.save(rental);
        return modelMapper.map(savedRental, UpdateRentalResponse.class);
    }

}
