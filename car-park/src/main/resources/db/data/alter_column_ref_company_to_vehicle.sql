alter table vehicle add column enterprise_id int references enterprise(id) on delete set null;

comment on column vehicle.enterprise_id is 'Ссылка на предприятие';

update vehicle
set enterprise_id = (select enterprise.id from enterprise where id = 1)
where id = 1;

update vehicle
set enterprise_id = (select enterprise.id from enterprise where id = 2)
where id = 2;

update vehicle
set enterprise_id = (select enterprise.id from enterprise where id = 3)
where id = 3;