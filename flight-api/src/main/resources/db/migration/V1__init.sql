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
    expedition_id bigserial references expeditions (id) on update cascade,
    astronaut_id  bigserial references astronauts (id) on update cascade,
    constraint expeditions_astronauts_pkey primary key (expedition_id, astronaut_id)
);

create index on spacecrafts (id);
create index on astronauts (id);
create index on expeditions (id);