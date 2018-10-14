INSERT INTO room (id, name) values (1, '회의실 A');
INSERT INTO room (id, name) values (2, '회의실 B');
INSERT INTO room (id, name) values (3, '회의실 C');
INSERT INTO room (id, name) values (4, '회의실 D');
INSERT INTO room (id, name) values (5, '회의실 E');
INSERT INTO room (id, name) values (6, '회의실 F');
INSERT INTO room (id, name) values (7, '회의실 G');
INSERT INTO room (id, name) values (8, '회의실 H');
INSERT INTO room (id, name) values (9, '회의실 I');
INSERT INTO room (id, name) values (10, '회의실 J');

INSERT INTO reservation(id, reserve_date, owner, start_date, end_date, room_id) values(1, now(), '강석윤', '20:40:22', '21:40:22' , 1);