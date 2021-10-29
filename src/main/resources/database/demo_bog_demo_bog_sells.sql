create table sells
(
    id             serial,
    product_id     integer,
    quantity       integer,
    buyer_user_id  integer,
    create_date    timestamp,
    state          integer,
    card_info      varchar(255),
    one_price      numeric(10, 2),
    cost           numeric(10, 2),
    seller_user_id integer
);

alter table sells
    owner to postgres;

create unique index sells_id_uindex
    on sells (id);

INSERT INTO demo_bog.sells (id, product_id, quantity, buyer_user_id, create_date, state, card_info, one_price, cost, seller_user_id) VALUES (2, 1, 1, null, '2021-10-29 12:58:47.410000', 1, 'asdsa', 2.00, 2.00, 11);
INSERT INTO demo_bog.sells (id, product_id, quantity, buyer_user_id, create_date, state, card_info, one_price, cost, seller_user_id) VALUES (3, 1, 1, null, '2021-10-29 12:59:23.292000', 1, 'adadsa', 2.00, 2.00, 11);
INSERT INTO demo_bog.sells (id, product_id, quantity, buyer_user_id, create_date, state, card_info, one_price, cost, seller_user_id) VALUES (1, 2, 1, 11, '2021-10-28 18:04:26.505000', 1, 'asdsadsa', 2.00, 2.00, null);