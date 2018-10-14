package kakaopay.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationGroupRepository extends JpaRepository<ReservationGroup, Long> {
}
