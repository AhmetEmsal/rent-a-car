package kodlama.io.rentacar.business.abstracts;

import kodlama.io.rentacar.business.dto.requests.create.CreatePaymentRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdatePaymentRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreatePaymentResponse;
import kodlama.io.rentacar.business.dto.responses.get.payments.GetAllPaymentsResponse;
import kodlama.io.rentacar.business.dto.responses.get.payments.GetPaymentResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdatePaymentResponse;
import kodlama.io.rentacar.common.dto.CreateRentalPaymentRequest;

import java.util.List;

public interface PaymentService {
    List<GetAllPaymentsResponse> getAll();
    GetPaymentResponse getById(int id) throws Exception;
    CreatePaymentResponse add(CreatePaymentRequest request) throws Exception;
    UpdatePaymentResponse update(int id, UpdatePaymentRequest request) throws Exception;
    void delete(int id) throws Exception;
    void processRentalPayment(CreateRentalPaymentRequest request) throws Exception;
}
