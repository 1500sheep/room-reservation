package kakaopay.web;

import kakaopay.domain.Reservation;
import kakaopay.dto.ReserveDTO;
import kakaopay.service.ReserveService;
import kakaopay.utils.RestResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/reserve")
public class ApiReserveController {

    @Resource(name = "reserveService")
    private ReserveService reserveService;

    @PostMapping
    public ResponseEntity<Void> createReservation(@Valid @RequestBody ReserveDTO reserveDTO) {
        Reservation reservation = reserveService.createReservation(reserveDTO);
        return ResponseEntity.created(URI.create("/reserve/" + reservation.getId())).build();
    }

    @GetMapping
    public ResponseEntity<RestResponse> getReservationsByDate(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    LocalDate date
    ) {
        return ResponseEntity.ok(RestResponse.success(reserveService.getReservationsByDate(date)));
    }
}
