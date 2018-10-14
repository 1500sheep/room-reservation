package kakaopay.domain;


import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
public class Reservation {
    private final static String regexTime = "[0-9][0-9]:[0,3][0]";

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
    private String owner;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reserveDate;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startDate;

    @NotNull
    @DateTimeFormat(pattern = regexTime)
    private LocalTime endDate;

    public void setStartDate(LocalTime startDate) {
        log.info("sample :  " + startDate);
        this.startDate = startDate;
    }
}
