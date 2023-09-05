create table products
(
    id          bigserial primary key,
    code        varchar not null unique,
    name        varchar not null,
    description varchar,
    image_url   varchar,
    price       numeric not null
);
