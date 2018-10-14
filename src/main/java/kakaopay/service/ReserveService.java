package kakaopay.service;

import kakaopay.domain.*;
import kakaopay.dto.ReserveDTO;
import kakaopay.exception.NotAllowedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReserveService {

    @Resource(name = "roomRepository")
    private RoomRepository roomRepository;

    @Resource(name = "reservationRepository")
    private ReservationRepository reservationRepository;

    @Resource(name = "reservationGroupRepository")
    private ReservationGroupRepository reservationGroupRepository;

    public Reservation createReservation(ReserveDTO reserveDTO) {
        Room room = roomRepository.findByName(reserveDTO.getRoomName()).orElseThrow(() -> new EntityNotFoundException("회의실이 존재하지 않습니다."));

        // 반복횟수가 1인, 즉 단일성 예약일 경우는 reservationGroup을 따로 만들지 않는다
        if (reserveDTO.checkRepeat_ONE()) {
            Reservation reservation;
            synchronized (this) {
                checkReservationExist(room, reserveDTO);
                reservation = reservationRepository.save(reserveDTO.toReservationWithoutReservationGroup(room));
            }
            return reservation;
        }

        return createReservations(reserveDTO, room);
    }

    @Transactional
    public Reservation createReservations(ReserveDTO reserveDTO, Room room) {
        // 반복 횟수가 2 이상인 예약은 reservationGroup을 만든다
        ReservationGroup reservationGroup = reservationGroupRepository.save(reserveDTO.toReservationGroup());

        // 반복 횟수 -1 만큼 저장한다.
        for (int i = 0; i < reserveDTO.getRepeatCount() - 1; i++) {
            synchronized (this) {
                checkReservationExist(room, reserveDTO);
                reservationRepository.save(reserveDTO.toReservationWithReservationGroup(room, reservationGroup));
            }
            reserveDTO.addReserveDate_OneWeek();
        }

        Reservation reservation;
        synchronized (this) {
            checkReservationExist(room, reserveDTO);
            reservation = reservationRepository.save(reserveDTO.toReservationWithoutReservationGroup(room));
        }
        return reservation;
    }

    // Database에 예약이 존재하는지 확인
    private void checkReservationExist(Room room, ReserveDTO reserveDTO) {

        if (reservationRepository.existsByRoomAndReserveDateAndStartTimeLessThanEqualAndEndTimeGreaterThan(room, reserveDTO.getReserveDate(), reserveDTO.getStartTime(), reserveDTO.getStartTime())) {
            throw new NotAllowedException("startTime", "시작 시간이 겹칩니다");
        }

        if (reservationRepository.existsByRoomAndReserveDateAndStartTimeLessThanAndEndTimeGreaterThanEqual(room, reserveDTO.getReserveDate(), reserveDTO.getEndTime(), reserveDTO.getEndTime())) {
            throw new NotAllowedException("endTime", "종료 시간이 겹칩니다");
        }
        if (reservationRepository.existsByRoomAndReserveDateAndStartTimeGreaterThanEqualAndStartTimeLessThan(room, reserveDTO.getReserveDate(), reserveDTO.getStartTime(), reserveDTO.getEndTime())) {
            throw new NotAllowedException("date", "시간이 겹칩니다");
        }
    }

    public List<Reservation> getReservationsByDate(LocalDate date) {
        return reservationRepository.findAllByReserveDate(date);
    }
}
