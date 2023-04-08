package kodlama.io.rentacar.business.concretes;

import kodlama.io.rentacar.business.abstracts.ModelService;
import kodlama.io.rentacar.business.dto.requests.create.CreateModelRequest;
import kodlama.io.rentacar.business.dto.requests.update.UpdateModelRequest;
import kodlama.io.rentacar.business.dto.responses.create.CreateModelResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetAllModelsResponse;
import kodlama.io.rentacar.business.dto.responses.get.GetModelResponse;
import kodlama.io.rentacar.business.dto.responses.update.UpdateModelResponse;
import kodlama.io.rentacar.entities.Model;
import kodlama.io.rentacar.repository.ModelRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ModelManager implements ModelService {
    private final ModelRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public List<GetAllModelsResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(model -> modelMapper.map(model, GetAllModelsResponse.class))
                .toList();
    }

    @Override
    public CreateModelResponse add(CreateModelRequest request) {
        Model model = modelMapper.map(request, Model.class);
        model.setId(0);
        repository.save(model);
        return modelMapper.map(model, CreateModelResponse.class);
    }

    @Override
    public UpdateModelResponse update(int id, UpdateModelRequest request) throws Exception {
        throwErrorIfModelNotExist(id);
        Model model = modelMapper.map(request, Model.class);
        model.setId(id);
        repository.save(model);
        return modelMapper.map(model, UpdateModelResponse.class);
    }

    @Override
    public GetModelResponse getById(int id) throws Exception {
        Optional<Model> model = repository.findById(id);
        if(model.isEmpty()) throwErrorAboutModelNotExist(id);

        return modelMapper.map(model.get(), GetModelResponse.class);
    }

    @Override
    public void delete(int id) throws Exception {
        throwErrorIfModelNotExist(id);
        repository.deleteById(id);
    }

    private void throwErrorIfModelNotExist(int id){
        if(repository.existsById(id)) return;
        throwErrorAboutModelNotExist(id);
    }

    private void throwErrorAboutModelNotExist(int id){
        throw new RuntimeException("Model("+id+") not found!");

    }
}
