alter table manager add column chat_id bigint;
comment on column manager.chat_id is 'Ид пользователя в telegram';