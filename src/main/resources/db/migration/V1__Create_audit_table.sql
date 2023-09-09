create table audit (
    id SERIAL PRIMARY KEY,
    entity_id varchar not null,
    entity_type varchar not null,
    entity_payload text,
    created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
);


CREATE TRIGGER trigger_set_timestamp
BEFORE UPDATE ON audit
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();