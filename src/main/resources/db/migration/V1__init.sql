CREATE TABLE event (
            id  BIGSERIAL  PRIMARY KEY,
            aggregate_uuid UUID,
            event_type VARCHAR(255) NOT NULL,
            data jsonb NOT NULL,
            timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX event_aggregate_uuid ON event (aggregate_uuid);