package kakaopay.dto;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

public class ReserveDTOValidationTest {

    private final static Logger logger = Logger.getGlobal();
    private static Validator validator;
    private ReserveDTO reserveDTO;

    @Before
    public void setUp() throws Exception {
        reserveDTO = ReserveDTO.builder()
                .roomName("A")
                .reservationName("강석윤")
                .repeatCount(1)
                .reserveDate(LocalDate.now())
                .startTime(LocalTime.of(10, 30, 0))
                .endTime(LocalTime.of(11, 30, 0))
                .build();
    }

    @BeforeClass
    public static void setup() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    @Test
    public void roomName_null() {
        reserveDTO.setRoomName(null);
        Set<ConstraintViolation<ReserveDTO>> constraintViolations = validator.validate(reserveDTO);
        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void reservationName_null() {
        reserveDTO.setReservationName(null);
        Set<ConstraintViolation<ReserveDTO>> constraintViolations = validator.validate(reserveDTO);
        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void reservationName_LESS_MIN_1() {
        reserveDTO.setReservationName("");
        Set<ConstraintViolation<ReserveDTO>> constraintViolations = validator.validate(reserveDTO);
        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void reservationName_GREATER_MAX_20() {
        int MAX = 20;
        StringBuffer sb = new StringBuffer(MAX + 1);
        for (int i = 0; i <= MAX; i++) {
            sb.append('1');
        }
        reserveDTO.setReservationName(sb.toString());
        Set<ConstraintViolation<ReserveDTO>> constraintViolations = validator.validate(reserveDTO);
        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void reserveDate_null() {
        reserveDTO.setReserveDate(null);
        Set<ConstraintViolation<ReserveDTO>> constraintViolations = validator.validate(reserveDTO);
        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test(expected = NullPointerException.class)
    public void startTime_null() {
        reserveDTO.setStartTime(null);
        Set<ConstraintViolation<ReserveDTO>> constraintViolations = validator.validate(reserveDTO);
        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test(expected = NullPointerException.class)
    public void endTime_null() {
        reserveDTO.setEndTime(null);
        Set<ConstraintViolation<ReserveDTO>> constraintViolations = validator.validate(reserveDTO);
        assertThat(constraintViolations.size()).isEqualTo(1);
    }
}
