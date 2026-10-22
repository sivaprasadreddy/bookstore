create sequence payment_id_seq start with 100 increment by 50;

create table payments
(
    id                bigint      not null default nextval('payment_id_seq'),
    order_id          bigint      not null references orders (id),
    payment_reference varchar(36) not null unique,
    amount            bigint      not null,
    currency          varchar(3)  not null,
    status            varchar(20) not null,
    error_description text,
    created_at        timestamp   not null default current_timestamp,
    completed_at      timestamp,
    version           bigint      not null default 0,
    primary key (id)
);

create index idx_payments_order_id on payments (order_id);
