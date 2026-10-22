create sequence book_id_seq start with 100 increment by 50;

create table books
(
    id          bigint    not null default nextval('book_id_seq'),
    isbn        varchar   not null unique,
    name        varchar   not null,
    description varchar,
    image_url   varchar,
    price       numeric   not null,
    created_at  timestamp not null default CURRENT_TIMESTAMP,
    updated_at  timestamp,
    version     bigint    not null default 0,
    primary key (id)
);
