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

INSERT INTO demo_bog.user_verifications (id, key, user_id, expire_date, state, create_date) VALUES (5, '14c44fb0-16ad-48ab-b777-23416e53e57b', 11, '2021-10-28 16:58:49.233000', 1, '2021-10-28 16:48:49.233000');