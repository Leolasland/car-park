alter table enterprise add column time_zone varchar;

comment on column enterprise.time_zone is 'Часовой пояс предприятия';

update enterprise
set time_zone = '+03:00';