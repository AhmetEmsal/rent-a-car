package kodlama.io.rentacar.repository.bases.vehicle;

import kodlama.io.rentacar.entities.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Model, Integer> {

    public boolean existsByNameIgnoreCase(String name);
}
