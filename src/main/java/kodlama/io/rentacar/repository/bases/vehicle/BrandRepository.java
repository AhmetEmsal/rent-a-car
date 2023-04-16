package kodlama.io.rentacar.repository.bases.vehicle;

import kodlama.io.rentacar.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    public boolean existsByNameIgnoreCase(String name);
}
