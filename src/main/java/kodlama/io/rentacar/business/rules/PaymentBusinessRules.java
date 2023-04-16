package kodlama.io.rentacar.business.rules;

import kodlama.io.rentacar.common.constants.Messages;
import kodlama.io.rentacar.common.dto.CreateRentalPaymentRequest;
import kodlama.io.rentacar.core.rules.BaseBusinessRules;
import kodlama.io.rentacar.core.utilities.exceptions.business.BusinessErrorCode;
import kodlama.io.rentacar.core.utilities.exceptions.business.BusinessException;
import kodlama.io.rentacar.entities.Payment;
import kodlama.io.rentacar.repository.functional.PaymentRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class PaymentBusinessRules extends BaseBusinessRules<Payment, PaymentRepository> {
    public PaymentBusinessRules(PaymentRepository repository) {
        super(Messages.Payment.NotExists, repository);
    }


    public void checkIfCardNumberIsNotUsed(String cardNumber) throws BusinessException {
        if (!getRepository().existsByCardNumber(cardNumber)) return;
        throw new BusinessException(BusinessErrorCode.AlreadyUsed, Messages.Payment.CardNumberAlreadyExists);
    }

    public void checkIfPaymentIsExists(CreateRentalPaymentRequest request) throws BusinessException {
        if (getRepository().existsByCardNumberAndCardHolderAndCardExpirationYearAndCardExpirationMonthAndCardCvv(request))
            return;
        throw new BusinessException(BusinessErrorCode.Incorrect, Messages.Payment.NotValid);
    }

    public void checkIfBalanceIsEnough(double balance, double price) throws BusinessException {
        if (balance >= price) return;
        throw new BusinessException(BusinessErrorCode.NotEnough, Messages.Payment.NotEnoughMoney);
    }
}
