package kakaopay.dto;

import kakaopay.exception.TimeFormatException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

public class ReserveDTOTest {

    private final static Logger logger = Logger.getGlobal();
    private final static int NOT_EXISTS = -1;
    private final static int ONE = 1;
    private ReserveDTO reserveDTO;

    @Before
    public void setUp() throws Exception {
        reserveDTO = ReserveDTO.builder()
                .repeatCount(1)
                .reserveDate(LocalDate.now())
                .startTime(LocalTime.of(10, 30, 0))
                .endTime(LocalTime.of(11, 30, 0))
                .build();
    }

    @Test
    public void checkMinute_0OR30_SUCCESS() {
        int minute = 30;
        reserveDTO.checkMinute_0OR30("endTime", minute);
        minute = 0;
        reserveDTO.checkMinute_0OR30("endTime", minute);
    }

    @Test(expected = TimeFormatException.class)
    public void checkMinute_0OR30_FAIL() {
        int minute = 20;
        reserveDTO.checkMinute_0OR30("endTime", minute);
    }

    @Test
    public void addReserveDate_OneWeek_SUCCESS() {
        LocalDate today = reserveDTO.getReserveDate();
        reserveDTO.addReserveDate_OneWeek();
        assertThat(today.plusWeeks(ONE)).isEqualTo(reserveDTO.getReserveDate());
    }

    @Test
    public void addReserveDate_OneWeek_FAIL() {
        LocalDate today = reserveDTO.getReserveDate();
        assertThat(today.plusWeeks(ONE)).isNotEqualTo(reserveDTO.getReserveDate());
    }

    @Test
    public void checkRepeat_ONE_SUCCESS() {
        assertThat(reserveDTO.checkRepeat_ONE()).isTrue();
    }

    @Test
    public void checkRepeat_ONE_FAIL() {
        reserveDTO.setRepeatCount(2);
        assertThat(reserveDTO.checkRepeat_ONE()).isFalse();
    }
}
