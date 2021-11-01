create table files
(
    id          serial,
    name        varchar(255),
    path        varchar(500),
    mime_type   varchar(255),
    ext         varchar(50),
    insert_date timestamp,
    state       integer default 1 not null
);

alter table files
    owner to postgres;

create unique index files_id_uindex
    on files (id);

