package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.InvoiceService;
import kodlama.io.rentacar.business.dto.requests.create.CreateInvoicesRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateInvoicesRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateInvoicesResponse;
import kodlama.io.rentacar.business.dto.responses.get.invoices.GetAllInvoicesResponse;
import kodlama.io.rentacar.business.dto.responses.get.invoices.GetInvoicesResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateInvoicesResponse;
import kodlama.io.rentacar.business.rules.InvoiceBusinessRules;
import kodlama.io.rentacar.entities.Invoice;
import kodlama.io.rentacar.repository.functional.InvoiceRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InvoiceManager implements InvoiceService {
    private final InvoiceRepository repository;
    private final ModelMapper modelMapper;
    private final InvoiceBusinessRules businessRules;

    @Override
    public List<GetAllInvoicesResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(invoice -> modelMapper.map(invoice, GetAllInvoicesResponse.class))
                .toList();
    }

    @Override
    public GetInvoicesResponse getOneById(int id) {
        Invoice invoice = businessRules.checkIfEntityExistsByIdThenReturn(id);
        return modelMapper.map(invoice, GetInvoicesResponse.class);
    }

    @Override
    public CreateInvoicesResponse add(CreateInvoicesRequest request) {

        Invoice invoice = modelMapper.map(request, Invoice.class);
        invoice.setId(0);
        return modelMapper.map(invoice, CreateInvoicesResponse.class);
    }

    @Override
    public UpdateInvoicesResponse update(int id, UpdateInvoicesRequest request) {

        Invoice invoice = modelMapper.map(request, Invoice.class);
        invoice.setId(id);
        return modelMapper.map(invoice, UpdateInvoicesResponse.class);
    }

    @Override
    public void delete(int id) {
        businessRules.checkIfEntityExistsById(id);
        repository.deleteById(id);
    }
}
