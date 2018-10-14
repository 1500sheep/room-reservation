export function timetableTemplate() {
    return `
    <section class="timetable" style="margin-bottom: 20px;">
        <ol class="timings">
            <li><time datetime="00:00">0000</time></li>
            <li>0100</li>
            <li>0200</li>
            <li>0300</li>
            <li>0400</li>
            <li>0500</li>
            <li>0600</li>
            <li>0700</li>
            <li>0800</li>
            <li>0900</li>
            <li>1000</li>
            <li>1100</li>
            <li>1200</li>
            <li>1300</li>
            <li>1400</li>
            <li>1500</li>
            <li>1600</li>
            <li>1700</li>
            <li>1800</li>
            <li>1900</li>
            <li>2000</li>
            <li>2100</li>
            <li>2200</li>
            <li>2300</li>
            <li>2400</li>
        </ol>
        <ol class="meeting-table">
            <li class="room">
                <span class="name" name="A">회의실 A</span>


            </li>
            <li class="room">
                <span class="name" name="B">회의실 B</span>
            </li>
            <li class="room">
                <span class="name" name="C">회의실 C</span>

            </li>
            <li class="room">
                <span class="name" name="D">회의실 D</span>

            </li>
            <li class="room">
                <span class="name" name="E">회의실 E</span>

            </li>
            <li class="room">
                <span class="name" name="F">회의실 F</span>

            </li>
            <li class="room">
                <span class="name" name="G">회의실 G</span>

            </li>
            <li class="room">
                <span class="name" name="H">회의실 H</span>

            </li>
            <li class="room">
                <span class="name" name="I">회의실 I</span>

            </li>
            <li class="room">
                <span class="name" name="J">회의실 J</span>

            </li>
        </ol>
    </section>
    `;
}

export function reservationTemplate({reservationName, endTime, startTime, reservationGroup, repeatNum, currentRepeatNum}) {
    const startHeight = 3 + parseInt(startTime.split(":")[0]) * 4 + parseInt(startTime.split(":")[1]) / 15;
    const endHeight = 3 + parseInt(endTime.split(":")[0]) * 4 + parseInt(endTime.split(":")[1]) / 15;

    return `
    <div class="${reservationGroup ? "repeat-many" : "repeat-one"}" style="top:${startHeight}em; height:${endHeight - startHeight}em;">
            <div class="title">${reservationGroup ? "총반복횟수(" + repeatNum + ")" : ""}</div>
            <div class="title">${reservationGroup ? "남은반복횟수(" + currentRepeatNum + ")" : ""}</div>
            <div>${reservationName}</div>
        </div>
    `;
}