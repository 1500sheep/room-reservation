package kakaopay.dto;

import kakaopay.domain.Reservation;
import kakaopay.domain.ReservationGroup;
import kakaopay.domain.Room;
import kakaopay.exception.TimeFormatException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
public class ReserveDTO {

    @NotNull
    private String owner;

    @NotNull
    private String roomName;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reserveDate;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startDate;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endDate;

    @NotNull
    @Min(1)
    private int repeatCount;

    public void setStartDate(LocalTime startDate) {
        log.info("setStartDate : "+startDate);
        checkTime("startDate",startDate.getMinute());
        this.startDate = startDate;
    }

    public void setEndDate(LocalTime endDate) {
        log.info("setEndDate : "+endDate);
        checkTime("endDate" , endDate.getMinute());
        this.endDate = endDate;
    }

    private void checkTime(String timeField, int minutes) {
        if (minutes != 0 && minutes != 30) {
            log.info("minutes : "+minutes);
            throw new TimeFormatException(timeField, "시간을 제대로 입력하세요");
        }
    }

    public LocalDate getChangedReserveDate(int count){
        return reserveDate.plusWeeks(count);
    }

    public void changeReserveDateAddOneWeek(){
        reserveDate = reserveDate.plusWeeks(1);
    }

    public boolean checkRepeat_ONE() {
        return repeatCount == 1;
    }

    public Reservation toReservationWithReservationGroup(Room room, ReservationGroup reservationGroup) {
        return Reservation.builder()
                .room(room)
                .reservationGroup(reservationGroup)
                .owner(owner)
                .reserveDate(reserveDate)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public Reservation toReservationWithoutReservationGroup(Room room) {
        return Reservation.builder()
                .room(room)
                .owner(owner)
                .reserveDate(reserveDate)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public ReservationGroup toReservationGroup() {
        return ReservationGroup.builder()
                .repeatCount(repeatCount)
                .startDate(startDate)
                .build();
    }

}
