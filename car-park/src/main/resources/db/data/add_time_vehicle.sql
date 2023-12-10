alter table vehicle add column dt_buy timestamptz;

comment on column vehicle.dt_buy is 'Дата покупки';

update vehicle
set dt_buy = now();