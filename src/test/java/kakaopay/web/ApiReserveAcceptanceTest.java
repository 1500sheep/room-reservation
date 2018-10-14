package kakaopay.web;

import kakaopay.dto.ReserveDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.AcceptanceTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiReserveAcceptanceTest extends AcceptanceTest {

    private final static Logger logger = Logger.getGlobal();
    private ReserveDTO reserveDTO;

    @Before
    public void setUp() throws Exception {
        reserveDTO = ReserveDTO.builder()
                .owner("강석윤")
                .roomName("회의실 A")
                .reserveDate(LocalDate.now())
                .startDate(LocalTime.now())
                .endDate(LocalTime.now())
                .repeatCount(1)
                .build();
    }

    @Test
    public void createReserve_성공() {
        logger.info("reserveDTO : " + reserveDTO);

        ResponseEntity<Void> response = template.postForEntity("/api/reserve", reserveDTO, Void.class);

        logger.info("succeed : " + response.getHeaders().getLocation().getPath());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }

    @Test
    public void getReservation() {
        logger.info("test : "+LocalDate.now());
        ResponseEntity<List> response = template.getForEntity("/api/reserve?date="+LocalDate.now(),List.class);
        logger.info("reservation test : "+ response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
