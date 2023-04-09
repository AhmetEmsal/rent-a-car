package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.CarService;
import kodlama.io.rentacar.business.abstracts.PaymentService;
import kodlama.io.rentacar.business.abstracts.RentalService;
import kodlama.io.rentacar.business.dto.requests.create.CreateRentalRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateRentalRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateRentalResponse;
import kodlama.io.rentacar.business.dto.responses.get.rentals.GetAllRentalsResponse;
import kodlama.io.rentacar.business.dto.responses.get.cars.GetCarResponse;
import kodlama.io.rentacar.business.dto.responses.get.rentals.GetRentalResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateRentalResponse;
import kodlama.io.rentacar.common.dto.CreateRentalPaymentRequest;
import kodlama.io.rentacar.entities.Car;
import kodlama.io.rentacar.entities.Rental;
import kodlama.io.rentacar.entities.enums.State;
import kodlama.io.rentacar.repository.RentalRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RentalManager implements RentalService {
    private final RentalRepository repository;
    private final CarService carService;
    private final PaymentService paymentService;
    private final ModelMapper modelMapper;

    @Override
    public CreateRentalResponse add(CreateRentalRequest request) throws Exception {

        // get car id
        final int carId = request.getCarId();

        // check car state
        throwErrorIfCarCannotSentToRental(carId);

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
        carService.changeState(carId, State.RENTED);

        // create and save rental
        LocalDateTime now = LocalDateTime.now();
        rental.setId(0);
        rental.setTotalPrice(totalPrice);
        rental.setEndDate(rental.getStartDate().plusDays(rental.getRentedForDays()));
        rental.setUpdatedAt(null);
        rental.setCreatedAt(now);
        Rental createdRental = repository.save(rental);
        return modelMapper.map(createdRental, CreateRentalResponse.class);
    }

    @Override
    public List<GetAllRentalsResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(rental-> modelMapper.map(rental, GetAllRentalsResponse.class))
                .toList();
    }

    @Override
    public GetRentalResponse getById(int id) throws Exception {
        Optional<Rental> rentalOptional = repository.findById(id);
        if(rentalOptional.isEmpty()) throwErrorAboutRentalNotExist(id);

        return modelMapper.map(rentalOptional.get(), GetRentalResponse.class);
    }

    @Override
    public UpdateRentalResponse update(int id, UpdateRentalRequest request) throws Exception {
        // map to rental
        Rental rental = modelMapper.map(request, Rental.class);

        GetRentalResponse oldRentalResponse = getById(id);
        //
        final int oldCarId = oldRentalResponse.getCarId(),
                newCarId = request.getCarId();

        // check if car id change
        if(oldCarId != newCarId){
            throwErrorIfCarCannotSentToRental(newCarId);

            // update new car state
            GetCarResponse carResponse = carService.getById(newCarId);
            Car newCar =  modelMapper.map(carResponse, Car.class);
            State newCarOldState = newCar.getState();
            carService.changeState(newCarId, State.RENTED);

            // update old car state
            try {
                carService.changeState(oldCarId,State.AVAILABLE);
            }
            catch (Exception exception){
                // transaction back
                carService.changeState(newCarId, newCarOldState);
                throw exception;
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
    public void delete(int id) throws Exception {
        Optional<Rental> rentalOptional = repository.findById(id);
        if(rentalOptional.isEmpty()) throwErrorAboutRentalNotExist(id);

        Rental rental = rentalOptional.get();

        // update car state
        carService.changeState(rental.getCar().getId(), State.AVAILABLE);

        repository.deleteById(id);
    }

    @Override
    public UpdateRentalResponse returnCarFromRental(int carId) throws Exception {
        throwErrorIfCarIsNotRented(carId);

        Rental rental = repository.findRentalByCarIdAndRentedIsContinue(carId);
        //rental.setCompleted(true);

        carService.changeState(carId, State.AVAILABLE);


        Rental savedRental = repository.save(rental);
        return modelMapper.map(savedRental, UpdateRentalResponse.class);
    }

    private void throwErrorIfCarIsNotRented(int carId) {
        if(repository.existsByCarIdAndRentalIsContinue(carId)) return;
        throw new RuntimeException("No car("+carId+") is rented found !");
    }


    private  void throwErrorIfCarCannotSentToRental(int carId) throws Exception{
        var carResponse = carService.getById(carId);
        var car = modelMapper.map(carResponse, Car.class);

        boolean carCanBeSentToRental = car.getState().equals(State.AVAILABLE);
        if(carCanBeSentToRental) return;
        throw new IllegalStateException("Car cannot be sent to rental due to it's state: " + car.getState());
    }

    private void throwErrorAboutRentalNotExist(int id){
        throw new RuntimeException("Rental("+id+") not exist");
    }
}
