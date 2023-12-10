alter table vehicle_track add column dt_point timestamptz;

comment on column vehicle_track.dt_point is 'Дата точки';

update vehicle_track
set dt_point = now();