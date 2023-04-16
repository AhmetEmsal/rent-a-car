package kodlama.io.rentacar.business.abstracts;

import kodlama.io.rentacar.business.dto.requests.create.CreateInvoicesRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateInvoicesRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateInvoicesResponse;
import kodlama.io.rentacar.business.dto.responses.get.invoices.GetAllInvoicesResponse;
import kodlama.io.rentacar.business.dto.responses.get.invoices.GetInvoicesResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateInvoicesResponse;

import java.util.List;

public interface InvoiceService {
    List<GetAllInvoicesResponse> getAll();

    GetInvoicesResponse getOneById(int id);

    CreateInvoicesResponse add(CreateInvoicesRequest request);

    UpdateInvoicesResponse update(int id, UpdateInvoicesRequest request);

    void delete(int id);
}
