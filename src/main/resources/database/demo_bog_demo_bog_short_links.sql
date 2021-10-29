create table short_links
(
    id          serial,
    key         varchar(255)      not null,
    url         varchar(500)      not null,
    create_date timestamp,
    state       integer default 0 not null,
    update_date timestamp
);

alter table short_links
    owner to postgres;

create unique index short_links_id_uindex
    on short_links (id);

INSERT INTO demo_bog.short_links (id, key, url, create_date, state, update_date) VALUES (11, '14c44fb0-16ad-48ab-b777-23416e53e57b', 'http://localhost:4200/#/confirm/14c44fb0-16ad-48ab-b777-23416e53e57b', '2021-10-28 16:48:49.230000', 1, null);