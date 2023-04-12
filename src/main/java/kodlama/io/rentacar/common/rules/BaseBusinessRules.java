package kodlama.io.rentacar.common.rules;

import kodlama.io.rentacar.core.utilities.exceptions.BusinessErrorCodes;
import kodlama.io.rentacar.core.utilities.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

@AllArgsConstructor
public class BaseBusinessRules {
    private String repositoryName;
    private JpaRepository repository;
    public void checkIfEntityExistsById(int id) throws BusinessException{
        if(repository.existsById(id)) return;
        throw new BusinessException(BusinessErrorCodes.NotExists, "No data exists in repository " + repositoryName +" with id " + id);
    }
}
