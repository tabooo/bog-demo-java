create table users
(
    id             serial,
    email          varchar(255)             not null,
    first_name     varchar(255)             not null,
    last_name      varchar(255)             not null,
    account_number varchar(255),
    password       varchar(255),
    state          integer        default 1 not null,
    enabled        integer        default 0 not null,
    personal_no    varchar(30),
    balance        numeric(10, 2) default 0 not null
);

alter table users
    owner to postgres;

create unique index users_id_uindex
    on users (id);

