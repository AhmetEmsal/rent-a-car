package kodlama.io.rentacar.adapters;

import kodlama.io.rentacar.business.abstracts.PosService;
import kodlama.io.rentacar.common.constants.Messages;
import kodlama.io.rentacar.core.exceptions.business.BusinessErrorCode;
import kodlama.io.rentacar.core.exceptions.business.BusinessException;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class FakePosServiceAdapter implements PosService {
    @Override
    public void pay() throws BusinessException {
        boolean isPaymentSuccessful = new Random().nextBoolean();
        if (!isPaymentSuccessful) throw new BusinessException(BusinessErrorCode.Unknown, Messages.Payment.Failed);
    }
}
