package kodlama.io.rentacar.business.abstracts;

import kodlama.io.rentacar.business.dto.requests.create.CreateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateMaintenanceRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.get.maintenances.GetAllMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.get.maintenances.GetMaintenanceResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateMaintenanceResponse;
import kodlama.io.rentacar.core.utilities.exceptions.BusinessException;

import java.util.List;

public interface MaintenanceService {

    List<GetAllMaintenanceResponse> getAll();

    CreateMaintenanceResponse add(CreateMaintenanceRequest request) throws BusinessException;

    UpdateMaintenanceResponse update(int id, UpdateMaintenanceRequest request) throws BusinessException;

    GetMaintenanceResponse getById(int id) throws BusinessException;

    void delete(int id) throws BusinessException;

    UpdateMaintenanceResponse returnCarFromMaintenance(int carId) throws BusinessException;
}
