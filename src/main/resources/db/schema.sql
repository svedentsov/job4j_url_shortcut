create table sites (
    id serial primary key,
    name_of_site varchar(2000) not null unique,
    username varchar(7) not null,
    password varchar(2000) not null
);

create table links (
    id serial primary key,
    url varchar (2000) not null unique,
    shortcut varchar (7) not null,
    total int,
    site_id int references sites(id)
);
