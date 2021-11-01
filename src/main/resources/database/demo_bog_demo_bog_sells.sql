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

