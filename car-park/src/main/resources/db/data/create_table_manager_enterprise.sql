CREATE TABLE manager_enterprise
(
    manager_id  INTEGER NOT NULL,
    enterprise_id INTEGER NOT NULL
);

ALTER TABLE manager_enterprise
    ADD CONSTRAINT fk_manent_on_manager FOREIGN KEY (manager_id) REFERENCES manager (id);

ALTER TABLE manager_enterprise
    ADD CONSTRAINT fk_manent_on_enterprise FOREIGN KEY (enterprise_id) REFERENCES enterprise (id);
