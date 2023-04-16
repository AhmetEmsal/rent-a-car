package kodlama.io.rentacar.business.rules;

import kodlama.io.rentacar.common.constants.Messages;
import kodlama.io.rentacar.core.rules.BaseBusinessRules;
import kodlama.io.rentacar.entities.Invoice;
import kodlama.io.rentacar.repository.functional.InvoiceRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class InvoiceBusinessRules extends BaseBusinessRules<Invoice, InvoiceRepository> {
    public InvoiceBusinessRules(InvoiceRepository repository) {
        super(Messages.Invoice.NotExists, repository);
    }
}
