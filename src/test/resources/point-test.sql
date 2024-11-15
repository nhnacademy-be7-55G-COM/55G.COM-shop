insert into customer (customer_id, password, name, phone_number, email, active)
VALUES (1, '1234', '이름', '01010101010', 'ex@ample', true);

insert into member_status (member_status_id, type_name) VALUES (1, 'NORMAL');
insert into member_grade (member_grade_id, grade_name, grade_condition, point, active) VALUES (1, '보통', 0, 0, true);

insert into member (customer_id, member_status_id, member_grade_id, login_id, password, birth, created_at, latest_login_at, point)
VALUES (1, 1, 1, 'login', '1q2w3e4r!', '19990909', now(), now(), 10000);

insert into point_source (point_source_id, point_source_name)
VALUES
    (1, '회원가입'),
    (2, '구매');

insert into point_history (point_history_id, point_source_id, customer_id, point, remaining_point, created_at, active)
VALUES
    (1, 1, 1, 5000, 5000, now(), true),
    (2, 2, 1, 4999, 10010, now(), true),
    (3, 2, 1, 4998, 10009, now(), true),
    (4, 2, 1, 4997, 10008, now(), true),
    (5, 2, 1, 4996, 10007, now(), true),
    (6, 2, 1, 4995, 10006, now(), true),
    (7, 2, 1, 4994, 10005, now(), true),
        -- 하나는 삭제된 기록
    (8, 2, 1, 4993, 10004, now(), false),
    (9, 2, 1, 4992, 10003, now(), true),
    (10, 2, 1, 4991, 10002, now(), true),
    (11, 2, 1, 4990, 10001, now(), true);
