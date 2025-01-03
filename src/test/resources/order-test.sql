insert into book_status (book_status_id, type_name) VALUES (1, 'normal');
insert into publisher (publisher_id, publisher_name, active) VALUES (1, '테스트 퍼블리셔', true);

insert into book (book_id, publisher_id, book_status_id, title, chapter, description, published_date, isbn, price, discount_rate, is_packed, stock, views, created_at)
VALUES
    (1, 1, 1, '테스트 타이틀1', '챕터', '설명', '2024-09-01', '23232323', 5000, 0, true, 3, 10000, '2024-09-01'),
    (2, 1, 1, '테스트 타이틀2', '챕터', '설명', '2024-09-01', '43443434', 5000, 0, true, 3, 10000, '2024-09-01'),
    (3, 1, 1, '테스트 타이틀3', '챕터', '설명', '2024-09-01', '54545454', 5000, 0, true, 3, 10000, '2024-09-01');

insert into wrapping_paper (wrapping_paper_id, name, price, active, image_name)
VALUES (1, '테스트 종이', 2000, true, '12313');

insert into customer (customer_id, password, name, phone_number, email, active)
values (1, '1234', 'asdf', '01012345678', 'example@nhnacademy.com', true);

insert into delivery_fee (delivery_fee_id, fee, "condition", refund_delivery_fee, name)
VALUES
    (1, 5000, 0, 5000, '일반'),
    (2, 0, 30000, 5000, '3만원 이상');

insert into delivery_status (delivery_status_id, type_name)
VALUES
    (1, 'PREPARING'),
    (2, 'SHIPPING'),
    (3, 'DELIVERED');

insert into delivery (delivery_id, delivery_fee_id, delivery_address, delivery_status_id, received_date, shipping_date, delivery_fee, invoice_number, receiver_name)
VALUES
    (1, 1, '테스트 주소1', 1, '2024-10-30', '2024-10-31', 5000, '12345678', 'ㅁㄴㅇㄹ'),
    (2, 2, '테스트 주소2', 1, '2024-10-30', '2024-10-31', 5000, '23456789', 'ㅋㅌㅊㅍ'),
    (3, 2, '테스트 주소2', 1, '2024-10-30', '2024-10-31', 5000, '23456789', 'ㅋㅌㅊㅍ'),
    (4, 2, '테스트 주소2', 1, '2024-10-30', '2024-10-31', 5000, '23456789', 'ㅋㅌㅊㅍ');

insert into order_detail_type (order_detail_type_id, type_name) VALUES (1, '타입타입');


insert into orders (order_id, customer_id, delivery_id, ordered_at, net_price, total_price, active)
VALUES
    (1, 1, 1, '2024-10-27', 12000, 13000, true),
    (2, 1, 2, '2024-10-29', 12000, 30000, true),
    (3, 1, 3, '2024-10-30', 12000, 115000, true),
    (4, 1, 4, '2024-10-30', 12000, 115000, false);

insert into order_detail (order_detail_id, book_id, order_id, wrapping_paper_id, order_detail_type_id, quantity, total_price, accumulation_price)
VALUES
    (1, 1, 1, 1, 1, 3, 15000, 2000),
    (2, 2, 1, 1, 1, 1, 15000, 2000),
    (3, 3, 1, 1, 1, 1, 15000, 2000),
    (4, 2, 2, 1, 1, 2, 15000, 2000),
    (5, 1, 2, 1, 1, 1, 15000, 2000),
    (6, 2, 3, 1, 1, 5, 100000, 2000),
    (7, 1, 3, 1, 1, 3, 15000, 2000),
    (8, 2, 4, 1, 1, 5, 100000, 2000),
    (9, 1, 4, 1, 1, 3, 15000, 2000);

insert into payment (payment_id, order_id, payment_key, currency, amount, payco_order_id)
VALUES
    (1, 1, '12312', 'KRW', 5000, '1231'),
    (2, 2, '45754', 'KRW', 5000, '547647');
