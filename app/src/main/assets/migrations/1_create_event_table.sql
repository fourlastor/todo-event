CREATE TABLE 'event' (
    event_id TEXT PRIMARY KEY,
    event_type INTEGER NOT NULL,
    created_at REAL NOT NULL,
    data BLOB
);
