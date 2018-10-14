package kakaopay.web;

import kakaopay.dto.ReserveDTO;
import kakaopay.utils.RestResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.AcceptanceTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiReserveAcceptanceTest extends AcceptanceTest {

    private final static Logger logger = Logger.getGlobal();
    private ReserveDTO reserveDTO;

    @Before
    public void setUp() throws Exception {
        reserveDTO = ReserveDTO.builder()
                .reservationName("강석윤")
                .roomName("A")
                .reserveDate(LocalDate.now().plusWeeks(1))
                .startTime(LocalTime.of(10, 30, 0))
                .endTime(LocalTime.of(12, 30, 0))
                .repeatCount(1)
                .build();
    }

    // 예외 케이스s are in 단위 테스트
    @Test
    public void createReserve_성공() {

        ResponseEntity<Void> response = template.postForEntity("/api/reserve", reserveDTO, Void.class);

        logger.info("createReserve 성공 : " + response.getHeaders().getLocation().getPath());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }

    @Test
    public void getReservationsByDate() {
        ResponseEntity<RestResponse> response = template.getForEntity("/api/reserve?date=" + LocalDate.now(), RestResponse.class);

        logger.info("Reservations Data : {}" + response.getBody().getData());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
