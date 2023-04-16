package kodlama.io.rentacar.core.rules;

import kodlama.io.rentacar.core.utilities.exceptions.business.BusinessErrorCode;
import kodlama.io.rentacar.core.utilities.exceptions.business.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseBusinessRules<E, T extends JpaRepository<E, Integer>> {
    private String notExistsMessage;
    private T repository;

    public void checkIfEntityExistsById(int id) throws BusinessException {
        if (repository.existsById(id)) return;
        throwNotExistError();
    }

    public E checkIfEntityExistsByIdThenReturn(int id) throws BusinessException {
        var optionalT = repository.findById(id);
        if (optionalT.isEmpty()) throwNotExistError();
        return optionalT.get();
    }

    private void throwNotExistError() throws BusinessException {
        throw new BusinessException(BusinessErrorCode.NotExists, notExistsMessage);
    }
}
