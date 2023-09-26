CREATE TABLE enterprise
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255),
    city VARCHAR(255),
    CONSTRAINT pk_enterprise PRIMARY KEY (id)
);

comment on table enterprise is 'Предприятие';
comment on column enterprise.id is 'Уникальный идентификатор записи';
comment on column enterprise.name is 'Наименование предприятия';
comment on column enterprise.city is 'Город';