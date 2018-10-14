package kakaopay.service;

import kakaopay.domain.*;
import kakaopay.dto.ReserveDTO;
import kakaopay.exception.NotAllowedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class ReserveService {

    @Resource(name = "roomRepository")
    private RoomRepository roomRepository;

    @Resource(name = "reservationRepository")
    private ReservationRepository reservationRepository;

    @Resource(name = "reservationGroupRepository")
    private ReservationGroupRepository reservationGroupRepository;

    public Reservation createReserve(ReserveDTO reserveDTO) {
        Room room = roomRepository.findByName(reserveDTO.getRoomName()).orElseThrow(() -> new EntityNotFoundException("회의실이 존재하지 않습니다."));

        if (reserveDTO.checkRepeat_ONE()) {
            existsReservation(room, reserveDTO);
            return reservationRepository.save(reserveDTO.toReservationWithoutReservationGroup(room));
        }

        return createReserves(reserveDTO, room);
    }

    @Transactional
    public Reservation createReserves(ReserveDTO reserveDTO, Room room) {
        ReservationGroup reservationGroup = reservationGroupRepository.save(reserveDTO.toReservationGroup());

        reserveDTO.changeReserveDateAddOneWeek();
        existsReservation(room, reserveDTO);
        reservationRepository.save(reserveDTO.toReservationWithReservationGroup(room, reservationGroup));

        for (int i = 1; i < reserveDTO.getRepeatCount(); i++) {
            reserveDTO.changeReserveDateAddOneWeek();
            existsReservation(room, reserveDTO);
            log.info("reservation : " + reservationRepository.save(reserveDTO.toReservationWithReservationGroup(room, reservationGroup)));
        }

        reserveDTO.changeReserveDateAddOneWeek();
        existsReservation(room, reserveDTO);
        return reservationRepository.save(reserveDTO.toReservationWithReservationGroup(room, reservationGroup));
    }

    private void existsReservation(Room room, ReserveDTO reserveDTO) {

        if (reservationRepository.existsByRoomAndReserveDateAndStartDateLessThanEqualAndEndDateGreaterThan(room, reserveDTO.getReserveDate(), reserveDTO.getStartDate(), reserveDTO.getStartDate())) {
            throw new NotAllowedException("startDate", "시작 시간이 겹칩니다.");
        }

        if (reservationRepository.existsByRoomAndReserveDateAndStartDateLessThanAndEndDateGreaterThanEqual(room, reserveDTO.getReserveDate(), reserveDTO.getEndDate(), reserveDTO.getEndDate())) {
            throw new NotAllowedException("endDate", "종료 시간이 겹칩니다.");
        }
        if(reservationRepository.existsByRoomAndReserveDateAndStartDateGreaterThanEqualAndStartDateLessThan(room, reserveDTO.getReserveDate(), reserveDTO.getStartDate(), reserveDTO.getEndDate())){
            throw new NotAllowedException("date", "시간이 겹칩니다");
        }
//        if(reservationRepository.existsByRoomAndReserveDateAndStartDateAndEndDate(room, reserveDTO.getReserveDate(), reserveDTO.getStartDate(), reserveDTO.getEndDate())){
//            throw new NotAllowedException("date", "동일한 시간이 존재합니다.");
//        }

    }

    public List<Reservation> getReservationsByDate(LocalDate date) {
        log.info("reservation111 : " + reservationRepository.findAllByReserveDate(date));
        return reservationRepository.findAllByReserveDate(date);
    }
}
