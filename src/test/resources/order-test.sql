insert into book_status (type_name) VALUES ('normal');
insert into publisher (publisher_name, active) VALUES ('테스트 퍼블리셔', true);

insert into book (book_id, publisher_id, book_status_id, title, chapter, description, published_date, isbn, price, discount_rate, is_packed, stock, views, created_at)
VALUES
    (1, 1, 1, '테스트 타이틀', '챕터', '설명', '2024-09-01', '23232323', 5000, 0, true, 3, 10000, 2024-09-01);

insert into wrapping_paper (name, price, active, image_name) VALUES ('테스트 종이', 2000, true, '12313');

insert into customer (password, name, phone_number, email, active)
values ('1234', 'asdf', '01012345678', 'example@nhnacademy.com', true);

insert into delivery_fee (fee, `condition`, refund_delivery_fee, name)
VALUES
    (5000, 0, 5000, '일반'),
    (0, 30000, 5000, '3만원 이상');

insert into delivery (delivery_fee_id, delivery_address, received_date, shipping_date, delivery_fee, invoice_number)
VALUES
    (1, '테스트 주소', '2024-10-30', '2024-10-31', 5000, '12345678');

insert into order_detail_type (order_detail_type_id, type_name) VALUES (1, '타입타입');


insert into orders (customer_id, delivery_id, ordered_at, net_price, total_price, active)
VALUES
    (1, 1, '2024-10-29', 12000, 13000, true);

insert into order_detail (book_id, order_id, wrapping_paper_id, order_detail_type_id, quantity, total_price, accumulation_price)
VALUES
    (1, 1, 1, 1, 3, 15000, 2000),
    (1, 1, 1, 1, 2, 15000, 2000),
    (1, 1, 1, 1, 1, 15000, 2000);
