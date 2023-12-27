alter table vehicle_track add column ride_id int references ride(id) on delete set null;
comment on column vehicle_track.ride_id is 'Ссылка на поездку';

update vehicle_track
set ride_id = (select ride.id from ride where id = 1)
where dt_point < to_date('2023-12-09', 'yyyy-mm-dd');

update vehicle_track
set ride_id = (select ride.id from ride where id = 2)
where dt_point < '2023-12-10 17:36:30.726503 +00:00';

update vehicle_track
set ride_id = (select ride.id from ride where id = 3)
where vehicle_id = '2';