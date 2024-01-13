alter table vehicle_track add column ride_id int references ride(id) on delete set null;
comment on column vehicle_track.ride_id is 'Ссылка на поездку';