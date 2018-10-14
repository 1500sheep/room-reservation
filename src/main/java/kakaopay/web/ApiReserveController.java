package kakaopay.web;

import kakaopay.domain.Reservation;
import kakaopay.dto.ReserveDTO;
import kakaopay.service.ReserveService;
import kakaopay.utils.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/reserve")
@Slf4j
public class ApiReserveController {


    @Resource(name = "reserveService")
    private ReserveService reserveService;


    @PostMapping
    public ResponseEntity<Void> createReservation(@Valid @RequestBody ReserveDTO reserveDTO) {
        Reservation reservation = reserveService.createReserve(reserveDTO);
        log.info("reservation : "+reservation);
        return ResponseEntity.created(URI.create("/reserve/" + reservation.getId())).build();
    }

    @GetMapping
    public ResponseEntity<RestResponse> getReservationsByDate(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    LocalDate date
    ) {
        log.info("date : " + date);
        return ResponseEntity.ok(RestResponse.success(reserveService.getReservationsByDate(date)));
    }

}
