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

INSERT INTO demo_bog.files (id, name, path, mime_type, ext, insert_date, state) VALUES (1, '289-2894084_hair-scissors-png-barber-scissors-logo-png-transparent.png', 'D:\document_files\0\1.png', 'image/png', '.png', '2021-10-28 15:42:19.715977', 1);
INSERT INTO demo_bog.files (id, name, path, mime_type, ext, insert_date, state) VALUES (2, '289-2894084_hair-scissors-png-barber-scissors-logo-png-transparent.png', 'D:\document_files\0\2.png', 'image/png', '.png', '2021-10-28 15:54:07.548909', 1);
INSERT INTO demo_bog.files (id, name, path, mime_type, ext, insert_date, state) VALUES (3, '289-2894084_hair-scissors-png-barber-scissors-logo-png-transparent.png', 'D:\document_files\0\3.png', 'image/png', '.png', '2021-10-28 15:55:46.958686', 1);
INSERT INTO demo_bog.files (id, name, path, mime_type, ext, insert_date, state) VALUES (4, '289-2894084_hair-scissors-png-barber-scissors-logo-png-transparent.png', 'D:\document_files\0\4.png', 'image/png', '.png', '2021-10-28 17:26:18.403926', 1);