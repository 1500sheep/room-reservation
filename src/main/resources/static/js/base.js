import {$} from './utils.js';
import {timetableTemplate, reservationTemplate} from './template/meetingTableTemplate.js';

Date.prototype.yyyymmdd = function () {
    const mm = this.getMonth() + 1; // getMonth() is zero-based
    const dd = this.getDate();

    return [this.getFullYear(),
        (mm > 9 ? '' : '0') + mm,
        (dd > 9 ? '' : '0') + dd
    ].join('-');
};

const today = new Date();
const date_today = today.yyyymmdd();

$('input[id=reserve-date]').placeholder = "e.g. " + date_today;
$('input[id=meeting-date]').placeholder = "e.g. " + date_today;
$('input[id=meeting-date]').value = date_today;

function validateError(response) {
    response.json().then(({errors}) => {
        errors.forEach(error => {
            alert("error comes : " + error.message);
        })
    })
}

function compareDateOverToday(date) {
    if (new Date().getDate() > date.getDate()) {
        alert("오늘 이후만 예약 가능합니다.");
        return false;
    }
    return true;
}

function getAndValidateReserveDate(message, reserveDate) {
    const regEx = /^\d{4}-\d{2}-\d{2}$/;

    if (!reserveDate.match(regEx)) {
        alert(message + "를 제대로 입력해주세요");
        return false;
    }  // Invalid format

    const d = new Date(reserveDate);

    if (Number.isNaN(d.getTime())) {
        alert(message + "를 제대로 입력해주세요");
        return false;
    } // Invalid date

    if (d.toISOString().slice(0, 10) !== reserveDate) {
        alert(message + "를 제대로 입력해주세요");
        return false;
    }

    return true;
}


function reserveHandler(event) {
    event.preventDefault();

    const roomName = $('select[id=room]').value;

    if (!roomName) {
        alert("회의실을 선택해주세요");
        return;
    }

    const reservationName = $('input[name=name-reservation]').value;
    if (!reservationName || reservationName.length > 20) {
        alert("예약자 명을 제대로 입력해주세요");
        return;
    }

    const reserveDate = $('input[id=reserve-date]').value;

    if (!getAndValidateReserveDate("예약 날짜", reserveDate)) {
        return;
    }

    if (!compareDateOverToday(new Date(reserveDate))) {
        return;
    }

    let startHour = $('input[name=start-hour]').value;
    let startMinute = $('input[name=start-minute]').value;
    if (!startHour || !startMinute) {
        alert("시작 시간을 입력하세요");
        return;
    }
    let endHour = $('input[name=end-hour]').value;
    let endMinute = $('input[name=end-minute]').value;
    if (!endHour || !endMinute) {
        alert("종료 시간을 입력하세요");
        return;
    }

    if (endHour < startHour || (endHour === startHour && endMinute <= startMinute)) {
        alert("시작 시간이 종료시간 보다 클 수 없습니다.")
        return;
    }

    startHour = startHour < 10 ? '0' + startHour : startHour;
    startMinute = startMinute === 0 ? '0' + startMinute : startMinute;
    endHour = endHour < 10 ? '0' + endHour : endHour;
    endMinute = endMinute === 0 ? '0' + endMinute : endMinute;

    const startTime = startHour + ":" + startMinute;
    const endTime = endHour + ":" + endMinute;

    const repeatCount = $('input[name=repeat-count]').value;
    if (repeatCount < 1 || repeatCount > 100) {
        alert("반복 횟수 범위를 벗어났습니다.");
        return;
    }

    fetch('/api/reserve', {
        method     : 'post',
        headers    : {'content-type': 'application/json'},
        credentials: 'same-origin',
        body       : JSON.stringify({
            roomName,
            reservationName,
            reserveDate,
            startTime,
            endTime,
            repeatCount
        })
    })
        .then(response => {
            if (response.status >= 400 && response.status <= 404) {
                validateError(response);
            }
            else if (response.status === 201) {
                return response.json();
            }
        })
        .then(({data}) => {
            alert("예약 성공");
            console.log("data : ", data);
        })
        .catch(error => {
            location.reload();
        });
}

function getMeetingReservationByDate(date) {
    fetch('/api/reserve?date=' + date)
        .then(response => {
            if (response.status >= 400 && response.status <= 404) {
                validateError(response);
            }
            else if (response.status === 200) {
                return response.json();
            }
        })
        .then(({data}) => {
            const timetable = $('.timetable');

            if (timetable) {
                timetable.remove();
            }

            $('div[name=check-reserve]').insertAdjacentHTML('afterend', timetableTemplate());
            data.forEach(reservation => {
                $(`span[name=${reservation.room.name}]`).insertAdjacentHTML('afterend', reservationTemplate(reservation));
            });
        })
        .catch(error => {
            console.log("error : ", error);
            // location.reload();
        });
}


$('#reserveForm').addEventListener('submit', reserveHandler);
$('input[type=button]').addEventListener('click', function () {
    const meetingDate = $('input[id=meeting-date]').value;

    if (!getAndValidateReserveDate("예약 확인 날짜", meetingDate)) {
        return;
    }
    getMeetingReservationByDate(meetingDate);
});
document.addEventListener("DOMContentLoaded", getMeetingReservationByDate(date_today));
