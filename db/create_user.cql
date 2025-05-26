CREATE TABLE user (id UUID PRIMARY KEY, name TEXT, age INT);
CREATE INDEX IF NOT EXISTS idx_user_name ON my_keyspace.user (name);

select * from user where name='Abe Ryan';