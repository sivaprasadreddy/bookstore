create sequence order_id_seq start with 1 increment by 1;
create sequence order_item_id_seq start with 1 increment by 1;
create sequence cc_id_seq start with 1 increment by 1;

create table orders
(
    id                        bigint DEFAULT nextval('order_id_seq') not null,
    order_id                  varchar(100),
    customer_email            varchar(100),
    customer_name             varchar(100),
    delivery_address_line1    varchar(255),
    delivery_address_line2    varchar(255),
    delivery_address_city     varchar(255),
    delivery_address_state    varchar(255),
    delivery_address_zip_code varchar(255),
    delivery_address_country  varchar(255),
    status                    varchar(100),
    comments                  text,
    created_at                timestamp,
    updated_at                timestamp,
    primary key (id)
);

create table order_items
(
    id            bigint DEFAULT nextval('order_item_id_seq') not null,
    product_code  varchar(255)                                not null,
    product_name  varchar(1024)                               not null,
    product_price numeric                                     not null,
    quantity      integer                                     not null,
    order_id      bigint                                      not null references orders (id),
    primary key (id)
);

create table creditcards
(
    id            bigint DEFAULT nextval('cc_id_seq') not null,
    customer_name varchar(100)                        not null,
    card_number   varchar(16)                         not null,
    cvv           varchar(6)                          not null,
    expiry_month  numeric                             not null,
    expiry_year   numeric                             not null,
    primary key (id),
    CONSTRAINT cc_card_num_unique UNIQUE (card_number)
);