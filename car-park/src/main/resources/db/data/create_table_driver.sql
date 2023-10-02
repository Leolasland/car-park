CREATE TABLE driver
(
    id            INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name          VARCHAR(255),
    salary        INTEGER,
    enterprise_id INTEGER,
    vehicle_id    INTEGER,
    CONSTRAINT pk_driver PRIMARY KEY (id)
);

ALTER TABLE driver
    ADD CONSTRAINT FK_DRIVER_ON_ENTERPRISE FOREIGN KEY (enterprise_id) REFERENCES enterprise (id);

ALTER TABLE driver
    ADD CONSTRAINT FK_DRIVER_ON_VEHICLE FOREIGN KEY (vehicle_id) REFERENCES vehicle (id);

comment on table driver is 'Водитель';
comment on column driver.id is 'Уникальный идентификатор записи';
comment on column driver.name is 'Имя водителя';
comment on column driver.salary is 'Зарплата';
comment on column driver.enterprise_id is 'Ссылка на предприятие';
comment on column driver.vehicle_id is 'Ссылка на автомобиль';