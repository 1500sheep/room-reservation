package kakaopay.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByReserveDate(LocalDate date);

    boolean existsByRoomAndReserveDateAndStartTimeLessThanEqualAndEndTimeGreaterThan(Room room, LocalDate reserveDate, LocalTime time, LocalTime time2);

    boolean existsByRoomAndReserveDateAndStartTimeLessThanAndEndTimeGreaterThanEqual(Room room, LocalDate reserveDate, LocalTime time, LocalTime time2);

    boolean existsByRoomAndReserveDateAndStartTimeGreaterThanEqualAndStartTimeLessThan(Room room, LocalDate reserveDate, LocalTime time, LocalTime time2);

}
