create table vehicle
(
    id               uuid primary key,
    price            varchar(255),
    year_manufacture varchar(255)
);

comment on table vehicle is 'Автомобиль';
comment on column vehicle.id is 'Уникальный идентификатор записи';
comment on column vehicle.price is 'Стоимость автомобиля';
comment on column vehicle.year_manufacture is 'Год выпуска';