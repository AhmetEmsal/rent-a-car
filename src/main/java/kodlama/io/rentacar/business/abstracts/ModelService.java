package kodlama.io.rentacar.business.abstracts;

import kodlama.io.rentacar.business.dto.requests.create.CreateModelRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateModelRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateModelResponse;
import kodlama.io.rentacar.business.dto.responses.get.models.GetAllModelsResponse;
import kodlama.io.rentacar.business.dto.responses.get.models.GetModelResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateModelResponse;
import kodlama.io.rentacar.core.utilities.exceptions.business.BusinessException;

import java.util.List;

public interface ModelService {
    List<GetAllModelsResponse> getAll();

    CreateModelResponse add(CreateModelRequest request);

    UpdateModelResponse update(int id, UpdateModelRequest request) throws BusinessException;

    GetModelResponse getById(int id) throws BusinessException;

    void delete(int id) throws BusinessException;
}
