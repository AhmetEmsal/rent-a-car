package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.PaymentService;
import kodlama.io.rentacar.business.abstracts.PosService;
import kodlama.io.rentacar.business.dto.requests.create.CreatePaymentRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdatePaymentRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreatePaymentResponse;
import kodlama.io.rentacar.business.dto.responses.get.payments.GetAllPaymentsResponse;
import kodlama.io.rentacar.business.dto.responses.get.payments.GetPaymentResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdatePaymentResponse;
import kodlama.io.rentacar.business.rules.PaymentBusinessRules;
import kodlama.io.rentacar.common.dto.CreateRentalPaymentRequest;
import kodlama.io.rentacar.core.utilities.exceptions.business.BusinessException;
import kodlama.io.rentacar.entities.Payment;
import kodlama.io.rentacar.repository.functional.PaymentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PaymentManager implements PaymentService {
    private final PaymentRepository repository;
    private final ModelMapper modelMapper;
    private final PosService posService;
    private final PaymentBusinessRules businessRules;

    @Override
    public List<GetAllPaymentsResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(payment -> modelMapper.map(payment, GetAllPaymentsResponse.class))
                .toList();
    }

    @Override
    public GetPaymentResponse getById(int id) throws BusinessException {
        Payment payment = businessRules.checkIfEntityExistsByIdThenReturn(id);
        return modelMapper.map(payment, GetPaymentResponse.class);
    }

    @Override
    public CreatePaymentResponse add(CreatePaymentRequest request) throws BusinessException {
        businessRules.checkIfCardNumberIsNotUsed(request.getCardNumber());

        Payment payment = modelMapper.map(request, Payment.class);
        payment.setId(0);
        Payment savedPayment = repository.save(payment);
        return modelMapper.map(savedPayment, CreatePaymentResponse.class);
    }

    @Override
    public UpdatePaymentResponse update(int id, UpdatePaymentRequest request) throws BusinessException {
        // get payment before updating to check whether the card number is updated
        Payment oldPayment = businessRules.checkIfEntityExistsByIdThenReturn(id);

        // map from request to payment and set id
        Payment payment = modelMapper.map(request, Payment.class);

        //
        final boolean cardNumberWantToBeChanged = !oldPayment.getCardNumber().equals(payment.getCardNumber());
        if (cardNumberWantToBeChanged) {
            businessRules.checkIfCardNumberIsNotUsed(payment.getCardNumber());
        }

        payment.setId(id);
        Payment updatedPayment = repository.save(payment);
        return modelMapper.map(updatedPayment, UpdatePaymentResponse.class);
    }

    @Override
    public void delete(int id) throws BusinessException {
        businessRules.checkIfEntityExistsById(id);
        repository.deleteById(id);
    }

    @Override
    public void processRentalPayment(CreateRentalPaymentRequest request) throws BusinessException {
        businessRules.checkIfPaymentIsExists(request);

        Payment payment = repository.findByCardNumber(request.getCardNumber()).get();
        double balance = payment.getBalance();
        final double price = request.getPrice();
        businessRules.checkIfBalanceIsEnough(balance, price);

        posService.pay();

        balance -= price;
        payment.setBalance(balance);
        repository.save(payment);

    }
}
