package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.ModelService;
import kodlama.io.rentacar.business.dto.requests.create.CreateModelRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateModelRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateModelResponse;
import kodlama.io.rentacar.business.dto.responses.get.models.GetAllModelsResponse;
import kodlama.io.rentacar.business.dto.responses.get.models.GetModelResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateModelResponse;
import kodlama.io.rentacar.business.rules.ModelBusinessRules;
import kodlama.io.rentacar.core.utilities.exceptions.business.BusinessException;
import kodlama.io.rentacar.entities.Model;
import kodlama.io.rentacar.repository.bases.vehicle.ModelRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ModelManager implements ModelService {
    private final ModelRepository repository;
    private final ModelMapper modelMapper;
    private final ModelBusinessRules businessRules;

    @Override
    public List<GetAllModelsResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(model -> modelMapper.map(model, GetAllModelsResponse.class))
                .toList();
    }

    @Override
    public CreateModelResponse add(CreateModelRequest request) {
        businessRules.checkIfBrandNameNotExist(request.getName());
        Model model = modelMapper.map(request, Model.class);
        model.setId(0);
        repository.save(model);
        return modelMapper.map(model, CreateModelResponse.class);
    }

    @Override
    public UpdateModelResponse update(int id, UpdateModelRequest request) throws BusinessException {
        Model oldModel = businessRules.checkIfEntityExistsByIdThenReturn(id);

        final boolean wantToChangeBrandName = !oldModel.getName().equalsIgnoreCase(request.getName());
        if (wantToChangeBrandName)
            businessRules.checkIfBrandNameNotExist(request.getName());

        Model model = modelMapper.map(request, Model.class);
        model.setId(id);
        repository.save(model);
        return modelMapper.map(model, UpdateModelResponse.class);
    }

    @Override
    public GetModelResponse getById(int id) throws BusinessException {
        Model model = businessRules.checkIfEntityExistsByIdThenReturn(id);
        return modelMapper.map(model, GetModelResponse.class);
    }

    @Override
    public void delete(int id) throws BusinessException {
        businessRules.checkIfEntityExistsById(id);
        repository.deleteById(id);
    }
}
