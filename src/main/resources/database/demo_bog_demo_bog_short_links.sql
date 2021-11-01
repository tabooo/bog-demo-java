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

