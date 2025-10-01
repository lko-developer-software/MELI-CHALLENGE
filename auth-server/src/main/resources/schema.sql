create table user_info
(
    id bigint auto_increment,
    username varchar(255) not null,
    password varchar(255) not null,
    nombre varchar(255),
    apellido varchar(255),
    email varchar(255),
    telefono varchar(50),
    direccion varchar(255),
    rol varchar(50),
    cargo varchar(50),
    ml_id varchar(50),
    estado varchar(50),
    fecha_registro varchar(50),
    primary key(id)
);