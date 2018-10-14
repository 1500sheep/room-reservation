# kakaopay 사전과제

> 회의실과 날짜, 사용 시간을 입력하여 회의실을 예약하는
> 회의실 예약 애플리케이션 개발



## Getting Started

### Prerequisites

- Java 1.8.x
- Lombok plugin



### Run in development

```
# build project
./gradlew build

# Run project
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```

#### Page

URL : [http://localhost:8080](http://localhost:8080)



### Dependencies

|           Dependency           |    Version    |
| :----------------------------: | :-----------: |
|    ======**FrontEnd**======    |  ==========   |
|          jquery-slim           |     3.3.1     |
|           bootstrap            |     4.1.3     |
|       vanilla Javascript       |  ECMAScript6  |
|    ======**Backend**======     |  ==========   |
|          spring-boot           | 2.0.5 RELEASE |
|  spring-boot-starter-data-jpa  |               |
| spring-boot-starter-validation |               |
|    spring-boot-starter-web     |               |
|       com.h2database:h2        |               |
| handlebars-spring-boot-starter |     0.3.0     |
|    org.projectlombok:lombok    |     1.18      |
|    org.assertj:assertj-core    |    3.10.0     |



------



## API Document

### 회의실 예약

#### Request

```
POST /api/reserve
```

| Parameter       | Type      | Required | Default | Description                                               |
| --------------- | --------- | -------- | ------- | --------------------------------------------------------- |
| reservationName | String    | true     |         | 예약자 명, 최소 1자 이상 최대 20자 이하 가능              |
| roomName        | String    | true     | A       | 회의실 명, Database 에 존재하는 A~J 만 가능               |
| reserveDate     | LocalDate | true     |         | 예약 날짜, yyyy-mm-dd 형식 e.g. 2018-10-15                |
| startTime       | LocaTime  | true     |         | 시작 시간, HH:mm 형식이며 0분 또는 30분만 가능 e.g. 10:30 |
| endTime         | LocalTime | true     |         | 종료 시간, 시작 시간과 동일하며 시작 시간 보다 큼         |
| repeatCount     | Int       | true     | 1       | 반복 횟수, 최소 1 이상 최대 100 이하 가능                 |

#### Success Response

```
HTTP/1.1 201 Created
Location: https://localhost/reserve/1
```

### Error Response

|                type                 |                        response data                         | description                                         |
| :---------------------------------: | :----------------------------------------------------------: | --------------------------------------------------- |
| Invalid parameter - Bad Request 400 | {<br/>    "errors": [
        {
            "field": "startTime",
            "message": "시작 시간이 겹칩니다"
        }
    ]
} | 도메인 설정에 맞지 않는 parameter, validation check |
| TimeFormatException - Forbidden 403 | {<br/>    "errors": [
        {
            "field": "date",
            "message": "시간 입력을 제대로 해주세요"
        }
    ]
} | 정각 또는 30분의 시간이 아닐 경우                   |
|    Room not Found- Not Found 404    | {<br/>    "errors": [         {             "message": "회의실이 존재하지 않습니다."         }     ] } | 존재 하지 않는 회의실 일 때                         |
| NotAllowedException - Forbidden 403 | {<br/>    "errors": [
        {
            "field": "startTime",
            "message": "시작 시간이 겹칩니다"
        }
    ]
} | 시작 시간, 종료 시간이 겹칠 때                      |

<hr/>

### 예약 확인 By 날짜

#### Request

```
Get /api/reserve?date={date}
```

| Parameter   | Type      | Required | Default | Description                                    |
| ----------- | --------- | -------- | ------- | ---------------------------------------------- |
| reserveDate | LocalDate | true     | Today   | 예약확인 날짜, yyyy-mm-dd 형식 e.g. 2018-10-15 |

#### Success ResponseHTTP/1.1 201 Created

```
HTTP/1.1 200 Ok
```

| Name              | response data                                                | Description                                                  |
| ----------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| List<Reservation> | [<br/>        {
            "id": 1,
            "room": {
                "id": 1,
                "name": "A"
            },
            "reservationGroup": null, 
            "reservationName": "예약1",
            "reserveDate": "2018-10-15",
            "startTime": "00:30:00",
            "endTime": "03:00:00",
            "currentRepeatNum": -1,
            "repeatNum": -1
        },<br/>         ...<br/>] | 예약 정보 List. 예약 그룹이 존재할 경우 reservationGroup이 null이 아니며, 예약 그룹의 총 반복횟수와 선택한 날짜의 예약에서 총 남은 반복 횟수를 알려주는 ```repeatNum ``` ,  ```currentRepeatNum```이 있다. |

### Error Response

|                type                 |                        response data                         |             description              |
| :---------------------------------: | :----------------------------------------------------------: | :----------------------------------: |
| Invalid parameter - Bad Request 400 | {<br/>    "errors": [
        {
            "field": "date",
            "message": "date time이 옳지 않습니다. "
        }
    ]
} | parameter date 형식이 옳지 않는 경우 |

<hr/>



## Requirement

- 예약 시간은 정시, 30분을 기준으로 시작하여 30분 단위로 예약 가능
  -  예) 13:00 ~ 16:30 예약, 17:00 ~ 17:30 예약

- 13:05 ~ 14:10 과 같이 정시, 30분 시작이 아닌 경우 예약 불가능
- 1회성 예약과 주 단위 반복 예약 설정 가능
  - 반복 예약 시 선택한 날짜의 요일 마다 반복, 반복 횟수 지정 필수(반복 횟수는 예약에 포함됨)
  - 예) 2018년 5월 31일(목) 14:00 ~ 15:00 반복 예약 시 지정한 종료일까지 매주 목요일에 반복 예약
- 동일한 회의실에 중첩된 일시로 예약 불가
  - 종료 시각과 시작 시각이 겹치는 경우는 중첩으로 판단하지 않음
  - 예) 동일 날짜, 회의실에 대해 14:00 ~ 15:00, 15:00 ~ 16:00 두 건은 예약가능
- 다수의 사용자가 동시에 동일 날짜, 회의실에 예약할 때 일시가 중첩되어 예약될 수 없고, 서버에서 먼저 처리되는 1건만 예약





## 문제해결

- 예약 시간은 Jdk 1.8 부터 생긴 java.time의 LocalTime의 DatetimeFormatter와 getMinute()으로 분을 확인해서 처리

- 종료 시간은 시작 시간보다 무조건 큼

- 1회성 예약과 주 단위 반복 예약 설정

  - 단위 반복 예약을 위해 단위 반복을 그룹화 해주는 ```ReservationGroup``` 클래스 추가

  - 시작 날짜 ```startDate``` 와 총 반복 횟수 ```repeatCount``` 를 갖고 있으며 ```Reservation``` 클래스의 ```getRepeatNum()``` 와 ```getCurrentRepeatNum()``` 를 사용해서 특정 예약 그룹의 총 반복 횟수와 해당 예약에서 몇번 반복이 남았는지 확인할 수 있다

    ```java
    // 해당 예약과 예약 그룹의 첫 번째 날과의 격주를 확인할 수 있다
    ChronoUnit.WEEKS.between(reservationGroup.getStartDate(), reserveDate)
    ```

- **Spring JPA**를 이용해서 동일한 회의실에 중첩된 일시로 예약 불가 해결

  ```java
  // 동일한 방, 동일한 예약 날짜 그리고 시작시간이 이미 예약된 예약의 시작 시간과 종료시간 사이에 있는 지 확인
  boolean existsByRoomAndReserveDateAndStartTimeLessThanEqualAndEndTimeGreaterThan(Room room, LocalDate reserveDate, LocalTime time, LocalTime time2);
  
  // 동일한 방, 동일한 예약 날짜 그리고 종료시간이 이미 예약된 예약의 시작 시간과 종료시간 사이에 있는 지 확인
      boolean existsByRoomAndReserveDateAndStartTimeLessThanAndEndTimeGreaterThanEqual(Room room, LocalDate reserveDate, LocalTime time, LocalTime time2);
  
  // 동일한 방, 동일한 예약 날짜 그리고 시작시간과 종료시간이 이미 예약된 예약의 시작시간을 범위 안에 두고 있는 지 확인
      boolean existsByRoomAndReserveDateAndStartTimeGreaterThanEqualAndStartTimeLessThan(Room room, LocalDate reserveDate, LocalTime time, LocalTime time2);
  ```



- Java는 멀티 쓰레드 프로세스 언어

  - 다수의 사용자가 동시에 동일 날짜, 회의실에 예약할 때 일시가 중첩되어 예약될 수 없고, 서버에서 먼저 처리되는 1건만 예약하기 위해선 동일한 데이터가 있는지 검사하고 삽입하는 과정을 동기화 처리하여 순서대로 처리할 수 있게 한다.

  - Java의 ```synchronized``` 예약어를 사용

    ```java
    synchronized (this) {
                    checkReservationExist(room, reserveDTO);
                    reservationRepository.save(reserveDTO.toReservationWithReservationGroup(room, reservationGroup));
                }
    ```


<hr/>

### 단위테스트, 통합테스트

- 단위 테스트 ```org.junit``` 사용 , validation check를 위해 ```import javax.validation``` 사용
- 통합 테스트 ```org.springframework.boot.test.web.client.TestRestTemplate``` 사용

```
// project structure (test)
test
└───kakaopay
│   └───domain
│   │   │   ReservationTest // 단위 테스트
│   │   │   ReservationValidationTest // 단위 테스트
│   └───dto
│   │   │   ReserveDTOTest // 단위 테스트
│   │   │   ReserveDTOValidationTest // 단위 테스트
│   └───dto 
│       │   ApiReserveAcceptanceTest // 통합 테스트
└───support
    │   AcceptanceTest // 통합 테스트
```

