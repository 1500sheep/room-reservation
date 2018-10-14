package kakaopay.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByReserveDate(LocalDate date);

    boolean existsByRoomAndReserveDateAndStartDateLessThanEqualAndEndDateGreaterThan(Room room, LocalDate reserveDate, LocalTime time, LocalTime time2);
    boolean existsByRoomAndReserveDateAndStartDateLessThanAndEndDateGreaterThanEqual(Room room, LocalDate reserveDate, LocalTime time, LocalTime time2);
    boolean existsByRoomAndReserveDateAndStartDateGreaterThanEqualAndStartDateLessThan(Room room, LocalDate reserveDate, LocalTime time, LocalTime time2);

    boolean existsByRoomAndReserveDateAndStartDateAndEndDate(Room room, LocalDate reserveDate, LocalTime time, LocalTime time2);
    Reservation findByReserveDateAndStartDateLessThanAndEndDateGreaterThan(LocalDate reserveDate, LocalTime time, LocalTime time2);
}
