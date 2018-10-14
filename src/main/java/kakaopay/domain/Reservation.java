package kakaopay.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    private final static int NOT_EXISTS = -1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn
    private ReservationGroup reservationGroup;

    @NotNull
    @Column(nullable = false)
    private String reservationName;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reserveDate;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    // ReservationGroup이 존재하는 예약일 경우 현재 예약 부터 남은 반복횟수가 몇번인지 확인할 수 있다
    public int getCurrentRepeatNum() {
        if (reservationGroup == null) {
            return NOT_EXISTS;
        }
        int repeatCount = reservationGroup.getRepeatCount();
        int leftWeeks = (int) ChronoUnit.WEEKS.between(reservationGroup.getStartDate(), reserveDate);

        return repeatCount - leftWeeks - 1;
    }

    public int getRepeatNum() {
        if (reservationGroup == null) {
            return NOT_EXISTS;
        }
        return reservationGroup.getRepeatCount();
    }
}
