alter table manager
    add column role varchar(255);

comment on column manager.role is 'Роль пользователя';

update manager
set role = 'ROLE_MANAGER'
where manager.role is null;

insert into manager (id, name, password, role)
VALUES (3, 'Test_user', '$2y$10$7KScBP5vrhnm.0B/LEOtsOkD6x8M7AC6lYd02H5zyd3hV0qXwp.IG',
        'ROLE_USER');