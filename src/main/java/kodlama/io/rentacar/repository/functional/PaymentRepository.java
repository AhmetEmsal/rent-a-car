package kodlama.io.rentacar.repository.functional;

import kodlama.io.rentacar.business.dto.requests.PaymentRequest;
import kodlama.io.rentacar.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByCardNumber(String cardNumber);

    boolean existsByCardNumber(String cardNumber);

    boolean existsByCardNumberAndCardHolderAndCardExpirationYearAndCardExpirationMonthAndCardCvv(String cardNumber, String cardHolder, int cardExpirationYear, int cardExpirationMonth, String cardCvv);

    // SPeL -> Spring Expression Language
    @Query("SELECT CASE WHEN COUNT(p)>0 THEN true ELSE false END" +
            " FROM Payment p WHERE p.cardNumber = :#{#paymentRequest.cardNumber} AND" +
            " p.cardHolder = :#{#paymentRequest.cardHolder} AND" +
            " p.cardExpirationYear = :#{#paymentRequest.cardExpirationYear} AND" +
            " p.cardExpirationMonth = :#{#paymentRequest.cardExpirationMonth} AND" +
            " p.cardCvv = :#{#paymentRequest.cardCvv}"
    )
    boolean existsByCardNumberAndCardHolderAndCardExpirationYearAndCardExpirationMonthAndCardCvv(@Param("paymentRequest") PaymentRequest paymentRequest);
}
