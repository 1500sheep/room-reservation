package kakaopay.dto;

import kakaopay.domain.Reservation;
import kakaopay.domain.ReservationGroup;
import kakaopay.domain.Room;
import kakaopay.exception.TimeFormatException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
public class ReserveDTO {
    private final static int ONE = 1;

    @NotNull
    @Length(min = 1, max = 20)
    private String reservationName;

    @NotNull
    private String roomName;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reserveDate;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @NotNull
    @Min(1)
    @Max(100)
    private int repeatCount;

    public void setStartTime(LocalTime startTime) {
        checkMinute_0OR30("startTime", startTime.getMinute());
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        checkMinute_0OR30("endTime", endTime.getMinute());
        this.endTime = endTime;
    }

    // check minute 정각 또는 30분 단위
    public void checkMinute_0OR30(String timeField, int minutes) {
        if (minutes != 0 && minutes != 30) {
            log.error("minute error : {}", minutes);
            throw new TimeFormatException(timeField, "시간을 제대로 입력하세요");
        }
    }

    public void addReserveDate_OneWeek() {
        reserveDate = reserveDate.plusWeeks(ONE);
    }

    public boolean checkRepeat_ONE() {
        return repeatCount == ONE;
    }

    public Reservation toReservationWithReservationGroup(Room room, ReservationGroup reservationGroup) {
        return Reservation.builder()
                .room(room)
                .reservationGroup(reservationGroup)
                .reservationName(reservationName)
                .reserveDate(reserveDate)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    public Reservation toReservationWithoutReservationGroup(Room room) {
        return Reservation.builder()
                .room(room)
                .reservationName(reservationName)
                .reserveDate(reserveDate)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    public ReservationGroup toReservationGroup() {
        return ReservationGroup.builder()
                .repeatCount(repeatCount)
                .startDate(reserveDate)
                .build();
    }

}
