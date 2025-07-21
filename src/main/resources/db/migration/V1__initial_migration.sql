create table users (
    id bigserial not null primary key,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    gender varchar(10) not null,
    date_of_birth date not null,
    email varchar(50) not null unique,
    password varchar(50) not null
);

create table todos (
    id bigserial not null primary key,
    title varchar(50) not null,
    description text not null,
    created_at date not null,
    completed boolean not null,
    user_id bigint not null,
    foreign key (user_id) references users(id)
                   on update cascade on delete cascade
);