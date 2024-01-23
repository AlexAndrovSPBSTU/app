create table owencategory
(
    category_id text not null
        constraint category_pkey
            primary key,
    name        text,
    link        text,
    parent_id   text
);

create table product
(
    product_id  text not null,
    link        text,
    sku         text,
    name        text,
    image       text,
    thumb       text,
    description text,
    specs       text,
    category_id text not null
        references owencategory
            on delete cascade,
    primary key (product_id, category_id)
);


create table doc
(
    doc_id      integer generated by default as identity
        primary key,
    name        text not null,
    product_id  text,
    category_id text,
    foreign key (product_id, category_id) references product
        on delete cascade
);

create table doc_item
(
    id     integer generated by default as identity,
    name   text,
    link   text,
    doc_id integer
        references doc
            on delete cascade
);

create table image
(
    image_id    integer generated by default as identity,
    src         text not null,
    alt         text,
    product_id  text,
    category_id text,
    foreign key (product_id, category_id) references product
        on delete cascade
);

create table kippribormeyrteccategory
(
    category_id text not null
        primary key,
    name        text,
    link        text,
    image       text,
    parent_id   text
);


create table modification
(
    part_number      text not null
        primary key,
    working_title    text,
    modification     text,
    full_title       text,
    price            double precision,
    price_nds        double precision,
    product_serial   text,
    group_           text,
    delivery_time    text,
    size             integer,
    oversize         integer,
    multiplicity     integer,
    code_tnved       text,
    status           text,
    guarantee_period smallint,
    market_exit_date varchar(30)
);


create table kippribormeyrtecprice
(
    id             text not null
        primary key,
    category_id    text
        references kippribormeyrteccategory
            on delete cascade,
    name           text,
    multiplicity   integer,
    unit           text,
    full_name      text,
    price          double precision,
    text           text,
    text2          text,
    text3          text,
    packing        text,
    additional_ids text,
    analog_ids     text,
    store_status   text,
    store_value    integer,
    modification   text
                        references modification
                            on delete set null,
    price_type     integer
);

create table owenprice
(
    izd_code     text not null,
    price        double precision,
    product_id   text not null,
    category_id  text not null,
    name         text,
    modification text
        constraint price_modification_fkey
            references modification
            on delete set null,
    constraint price_pkey
        primary key (izd_code, product_id, category_id),
    constraint price_product_id_category_id_fkey
        foreign key (product_id, category_id) references product
            on delete cascade
);

create table arrival
(
    quantity integer not null,
    date     date    not null,
    price_id text    not null
        references kippribormeyrtecprice
            on delete cascade,
    primary key (quantity, date, price_id)
);
