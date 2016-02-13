CREATE TABLE 'event' (
    event_id TEXT PRIMARY KEY,
    event_type INTEGER NOT NULL,
    time INTEGER NOT NULL,
    data BLOB
);
