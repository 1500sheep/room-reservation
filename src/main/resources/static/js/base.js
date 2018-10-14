const $ = selector => document.querySelector(selector);

const $all = selector => document.querySelectorAll(selector);


Date.prototype.yyyymmdd = function () {
    const mm = this.getMonth() + 1; // getMonth() is zero-based
    const dd = this.getDate();

    return [this.getFullYear(),
        (mm > 9 ? '' : '0') + mm,
        (dd > 9 ? '' : '0') + dd
    ].join('-');
};

const today = new Date();

function validateError(response) {
    response.json().then(({errors}) => {
        errors.forEach((error) => {
            console.log("error : " + error);
        })
    })
}

function validateReserveDate(reserveDate) {
    const regEx = /^\d{4}-\d{2}-\d{2}$/;

    if (!reserveDate.match(regEx)) {
        alert("예약 날짜를 제대로 입력해주세요");
        return false;
    }  // Invalid format

    const d = new Date(reserveDate);

    if (Number.isNaN(d.getTime())) {
        alert("예약 날짜를 제대로 입력해주세요");
        return false;
    } // Invalid date

    if (d.toISOString().slice(0, 10) !== reserveDate) {
        alert("예약 날짜를 제대로 입력해주세요");
        return false;
    }

    if (new Date().getDate() > d.getDate()) {
        alert("오늘 이후만 예약 가능합니다.");
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

    const owner = $('input[name=reservationName]').value;
    if (!owner) {
        alert("예약자 명을 입력해주세요");
        return;
    }

    const reserveDate = $('input[id=reserve-date]').value;

    if (!validateReserveDate(reserveDate)) {
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

    if (endHour < startHour || (endHour == startHour && endMinute <= startMinute)) {
        alert("시작 시간이 종료시간 보다 클 수 없습니다.")
        return;
    }

    startHour = startHour < 10 ? '0' + startHour : startHour;
    startMinute = startMinute == 0 ? '0' + startMinute : startMinute;
    endHour = endHour < 10 ? '0' + endHour : endHour;
    endMinute = endMinute == 0 ? '0' + endMinute : endMinute;

    const startDate = startHour + ":" + startMinute;
    const endDate = endHour + ":" + endMinute;

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
            owner,
            reserveDate,
            startDate,
            endDate,
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
            console.log("data : ", data);
        })
        .catch(error => {
            location.reload();
        });
}


$('#reserveForm').addEventListener('submit', reserveHandler);
$('input[type=button]').addEventListener('click', function(){
    const meetingDate = $('input[id=meeting-date]').value;

    // if (!validateReserveDate(meetingDate)) {
    //     return;
    // }
    getMeetingReservationByDate(meetingDate);
});

function reservationTemplate({owner, endDate, startDate, reservationGroup}) {
    const startHeight = 3 + parseInt(startDate.split(":")[0]) * 4 + parseInt(startDate.split(":")[1]) / 15;
    const endHeight = 3 + parseInt(endDate.split(":")[0]) * 4 + parseInt(endDate.split(":")[1]) / 15;

    return `
    <div class="repeat-one" style="top:${startHeight}em; height:${endHeight-startHeight}em;">
            <div class="title"></div>
            <div>${owner}</div>
        </div>
    `;
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
            data.forEach(reservation => {
                $(`span[name=${reservation.room.name}]`).insertAdjacentHTML('afterend', reservationTemplate(reservation));
            });
        })
        .catch(error => {
            location.reload();
        });
}

document.addEventListener("DOMContentLoaded", getMeetingReservationByDate(today.yyyymmdd()));