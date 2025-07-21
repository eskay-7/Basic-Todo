create table basic_todo (
    id bigserial not null primary key,
    name text not null,
    created_at date not null,
    completed boolean not null
);