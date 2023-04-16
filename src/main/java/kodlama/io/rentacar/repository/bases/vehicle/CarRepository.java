package kodlama.io.rentacar.repository.bases.vehicle;

import kodlama.io.rentacar.entities.Car;
import kodlama.io.rentacar.entities.enums.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> {
    @Query("SELECT c FROM Car c WHERE c.state <> :state")
    public List<Car> findAllByStateIsNot(@Param("state") State state);
}
