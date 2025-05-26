CREATE TABLE campaign (id UUID PRIMARY KEY, name TEXT);
CREATE INDEX IF NOT EXISTS idx_campaign_name ON my_keyspace.campaign (name);

select * from campaign where name='';