CREATE TABLE event (
            id  BIGSERIAL  PRIMARY KEY,
            aggregate_id UUID NOT NULL,
            aggregate_type VARCHAR NOT NULL,
            event_type VARCHAR(255) NOT NULL,
            data jsonb NOT NULL,
            timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX event_aggregate_id ON event (aggregate_id);