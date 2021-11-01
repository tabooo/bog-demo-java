create table user_verifications
(
    id          serial,
    key         varchar(255),
    user_id     integer,
    expire_date timestamp,
    state       integer,
    create_date timestamp
);

alter table user_verifications
    owner to postgres;

