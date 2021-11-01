create table products
(
    id       serial,
    file_id  integer,
    price    numeric(10, 2) default 0 not null,
    quantity integer        default 0 not null,
    state    integer        default 1 not null,
    name     varchar(255)             not null,
    user_id  integer
);

alter table products
    owner to postgres;

create unique index products_id_uindex
    on products (id);

