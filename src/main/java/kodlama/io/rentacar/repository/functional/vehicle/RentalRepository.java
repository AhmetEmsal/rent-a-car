package kodlama.io.rentacar.repository.functional.vehicle;

import kodlama.io.rentacar.entities.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RentalRepository extends JpaRepository<Rental, Integer> {

    @Query("SELECT r FROM Rental r WHERE r.car.id = :carId and CURRENT_TIMESTAMP BETWEEN r.startDate AND r.endDate")
    Rental findRentalByCarIdAndRentedIsContinue(@Param("carId") int carId);

    @Query("SELECT COUNT(r) > 0 FROM Rental r WHERE r.car.id = :carId AND CURRENT_TIMESTAMP BETWEEN r.startDate AND r.endDate")
    boolean existsByCarIdAndRentalIsContinue(@Param("carId") int carId);
}
