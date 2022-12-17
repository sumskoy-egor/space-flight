create table spacecrafts
(
    id      bigserial primary key,
    model   text not null,
    enabled boolean
);

create table astronauts
(
    id      bigserial primary key,
    name    text not null,
    is_busy boolean
);

create table expeditions
(
    id                bigserial primary key,
    mission           text,
    p_start_date      timestamp,
    p_completion_date timestamp
        check ( p_completion_date is null or p_start_date < p_completion_date ),

    a_start_date      timestamp,
    a_completion_date timestamp,
    spacecraft_id     bigint not null references spacecrafts (id)
);

create table expeditions_astronauts
(
    expedition_id bigserial references expeditions (id) on delete cascade,
    astronaut_id  bigserial references astronauts (id) on update cascade on delete cascade,
    constraint expeditions_astronauts_pkey primary key (expedition_id, astronaut_id)
);

create index on spacecrafts (id);
create index on astronauts (id);
create index on expeditions (id);

create table users
(
    id         bigserial primary key,
    email      text        not null,
    name       text        not null,
    password   text        not null,
    created_at timestamptz not null default now()
);

create unique index users_email_uindex on users (email);
create unique index users_email_name_index on users (email, name);

create table authorities
(
    id    int primary key,
    value text not null
);

create unique index authorities_value_uindex on authorities (value);

create table user_authorities
(
    user_id      bigint not null,
    authority_id int    not null,
    primary key (user_id, authority_id),
    constraint user_authorities_users_fk foreign key (user_id)
        references users (id) on delete cascade,
    constraint user_authorities_authorities_fk foreign key (authority_id)
        references authorities (id) on delete cascade
);
