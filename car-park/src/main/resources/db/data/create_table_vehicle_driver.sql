CREATE TABLE vehicle_driver
(
    driver_id  INTEGER NOT NULL,
    vehicle_id INTEGER NOT NULL
);

ALTER TABLE vehicle_driver
    ADD CONSTRAINT fk_vehdri_on_driver FOREIGN KEY (driver_id) REFERENCES driver (id);

ALTER TABLE vehicle_driver
    ADD CONSTRAINT fk_vehdri_on_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle (id);

alter table driver drop column vehicle_id;