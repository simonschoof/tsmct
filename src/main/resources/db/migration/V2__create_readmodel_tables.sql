CREATE TABLE rm_inventory_item (
            id  BIGSERIAL  PRIMARY KEY,
            aggregate_id UUID NOT NULL,
            name VARCHAR NOT NULL
);

CREATE INDEX rm_inventory_item_aggregate_id ON rm_inventory_item (aggregate_id);

CREATE TABLE rm_inventory_item_details (
            id  BIGSERIAL  PRIMARY KEY,
            aggregate_id UUID NOT NULL,
            name VARCHAR NOT NULL,
            available_quantity INT NOT NULL,
            max_quantity INT NOT NULL
);

CREATE INDEX rm_inventory_item_details_aggregate_id ON rm_inventory_item_details (aggregate_id);