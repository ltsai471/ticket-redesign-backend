CREATE TABLE ticket (
id UUID PRIMARY KEY,
userId TEXT,
seatId TEXT,
paid boolean,
creationDate date,
);