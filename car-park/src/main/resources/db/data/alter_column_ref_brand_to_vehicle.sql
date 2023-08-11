alter table vehicle add column brand_id int references brand(id) on delete set null;

comment on column vehicle.brand_id is 'Ссылка на марку автомобиля';

update vehicle
set brand_id = (select brand.id from brand where id = 1)
where id = 1;

update vehicle
set brand_id = (select brand.id from brand where id = 2)
where id = 2;

update vehicle
set brand_id = (select brand.id from brand where id = 3)
where id = 3;