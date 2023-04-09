package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.PaymentService;
import kodlama.io.rentacar.business.abstracts.PosService;
import kodlama.io.rentacar.business.dto.requests.create.CreatePaymentRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdatePaymentRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreatePaymentResponse;
import kodlama.io.rentacar.business.dto.responses.get.payments.GetAllPaymentsResponse;
import kodlama.io.rentacar.business.dto.responses.get.payments.GetPaymentResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdatePaymentResponse;
import kodlama.io.rentacar.common.dto.CreateRentalPaymentRequest;
import kodlama.io.rentacar.entities.Payment;
import kodlama.io.rentacar.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentManager implements PaymentService {
    private final PaymentRepository repository;
    private final ModelMapper modelMapper;
    private final PosService posService;
    @Override
    public List<GetAllPaymentsResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(payment-> modelMapper.map(payment, GetAllPaymentsResponse.class))
                .toList();
    }

    @Override
    public GetPaymentResponse getById(int id) throws Exception{
        Optional<Payment> paymentOptional = repository.findById(id);
        if(paymentOptional.isEmpty()) throwErrorAboutPaymentNotExist(id);
        return modelMapper.map(paymentOptional.get(), GetPaymentResponse.class);
    }

    @Override
    public CreatePaymentResponse add(CreatePaymentRequest request) throws Exception{
        throwErrorIfCardNumberIsUsed(request.getCardNumber());

        Payment payment = modelMapper.map(request, Payment.class);
        payment.setId(0);
        Payment savedPayment = repository.save(payment);
        return modelMapper.map(savedPayment, CreatePaymentResponse.class);
    }

    @Override
    public UpdatePaymentResponse update(int id, UpdatePaymentRequest request) throws Exception {
        // get payment before updating to check whether the card number is updated
        Optional<Payment> oldPaymentOptional = repository.findById(id);
        if(oldPaymentOptional.isEmpty()) throwErrorAboutPaymentNotExist(id);
        Payment oldPayment = oldPaymentOptional.get();

        // map from request to payment and set id
        Payment payment = modelMapper.map(request, Payment.class);

        // If the card number is to be updated, it is necessary to check whether the card number is used
        final boolean cardNumberIsNotEqual = !oldPayment.getCardNumber().equals(payment.getCardNumber());
        if(cardNumberIsNotEqual){
            Optional<Payment> check = repository.findByCardNumber(payment.getCardNumber());
            if(check.isEmpty())
                throw new RuntimeException("Card number("+payment.getCardNumber()+") is already used!");
        }

        Payment updatedPayment = repository.save(payment);
        payment.setId(id);
        return modelMapper.map(updatedPayment, UpdatePaymentResponse.class);
    }

    @Override
    public void delete(int id) throws Exception {
        throwErrorIfPaymentNotExist(id);
        repository.deleteById(id);
    }

    @Override
    public void processRentalPayment(CreateRentalPaymentRequest request) throws Exception {
        throwErrorIfPaymentIsNotExists(request);

        Payment payment = repository.findByCardNumber(request.getCardNumber()).get();
        double balance = payment.getBalance();
        final double price = request.getPrice();
        throwErrorIfBalanceIsNotEnough(balance, price);
        posService.pay();
        balance -= price;
        payment.setBalance(balance);
        repository.save(payment);

    }


    private void throwErrorIfCardNumberIsUsed(String cardNumber) throws Exception{
        if(!repository.existsByCardNumber(cardNumber)) return;
        throw new RuntimeException("Card number("+cardNumber+") is already used!");
    }

    private void throwErrorIfPaymentIsNotExists(CreateRentalPaymentRequest request) throws Exception{
        if(repository.existsByCardNumberAndCardHolderAndCardExpirationYearAndCardExpirationMonthAndCardCvv(request)) return;
        throw new RuntimeException("Card information is incorrect.");
    }

    private void throwErrorIfBalanceIsNotEnough(double balance, double price) throws Exception{
        if(balance >= price) return;
        throw new RuntimeException("The "+price+" units of money required for the rental could not be afford by the credit card balance!");
    }

    private void throwErrorIfPaymentNotExist(int id) throws Exception {
        if(repository.existsById(id)) return;
        throwErrorAboutPaymentNotExist(id);
    }

    private void throwErrorAboutPaymentNotExist(int id) throws Exception{
        throw new Exception("Payment("+id+") not exist");
    }
}
