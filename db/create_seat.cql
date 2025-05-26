DROP TABLE seat;

CREATE TABLE seat (
id UUID,
area TEXT,
row int,
column int,
price int,
status TEXT,
campaign_id TEXT,
PRIMARY KEY(campaign_id,area,row,column)
);
CREATE INDEX IF NOT EXISTS idx_id ON my_keyspace.seat (id);

select * from seat where campaign_id='0' and area='0' and row=0 and column=0;
select * from seat where id=123e4567-e89b-12d3-a456-426614174000;


