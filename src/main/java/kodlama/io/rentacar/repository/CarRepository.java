package kodlama.io.rentacar.repository;

import kodlama.io.rentacar.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car,Integer> {
    @Query("SELECT c FROM Car c WHERE c.state <> 'MAINTENANCE'")
    public List<Car> getAllWithoutInMaintenance();
}
