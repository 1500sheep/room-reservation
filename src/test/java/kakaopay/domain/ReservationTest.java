package kakaopay.domain;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationTest {

    private final static Logger logger = Logger.getGlobal();
    private final static int NOT_EXISTS = -1;
    private final static int MINIMUN_REPEATCOUNT = 1;
    private Reservation reservation;

    @Before
    public void setUp() throws Exception {
        reservation = Reservation.builder()
                .room(new Room())
                .reservationName("강석윤")
                .reservationGroup(
                        ReservationGroup.builder()
                                .startDate(LocalDate.now())
                                .repeatCount(3)
                        .build())
                .reserveDate(LocalDate.now())
                .startTime(LocalTime.of(10, 30, 0))
                .endTime(LocalTime.of(11, 30, 0))
                .build();
    }

    @Test
    public void getCurrentRepeatNum_Exists() {
        assertThat(reservation.getCurrentRepeatNum()).isGreaterThan(NOT_EXISTS);
    }

    @Test
    public void getCurrentRepeatNum_NotExists() {
        reservation.setReservationGroup(null);
        assertThat(reservation.getCurrentRepeatNum()).isEqualTo(NOT_EXISTS);
    }

    @Test
    public void getRepeatNum_Exists() {
        assertThat(reservation.getCurrentRepeatNum()).isGreaterThan(MINIMUN_REPEATCOUNT);
    }

    @Test
    public void getRepeatNum_NotExists() {
        reservation.setReservationGroup(null);
        assertThat(reservation.getCurrentRepeatNum()).isEqualTo(NOT_EXISTS);
    }
}
