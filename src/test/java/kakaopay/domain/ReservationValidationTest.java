package kakaopay.domain;

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


public class ReservationValidationTest {

    private final static Logger logger = Logger.getGlobal();
    private static Validator validator;
    private Reservation reservation;

    @Before
    public void setUp() throws Exception {
        reservation = Reservation.builder()
                .room(new Room())
                .reservationName("강석윤")
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
    public void room_null() {
        reservation.setRoom(null);
        Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(reservation);
        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void reservationName_null() {
        reservation.setReservationName(null);
        Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(reservation);
        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void reserveDate_null() {
        reservation.setReserveDate(null);
        Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(reservation);
        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void startTime_null() {
        reservation.setStartTime(null);
        Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(reservation);
        assertThat(constraintViolations.size()).isEqualTo(1);
    }

    @Test
    public void endTime_null() {
        reservation.setEndTime(null);
        Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(reservation);
        assertThat(constraintViolations.size()).isEqualTo(1);
    }
}
