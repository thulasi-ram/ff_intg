CREATE OR REPLACE FUNCTION trigger_set_timestamp() RETURNS TRIGGER
LANGUAGE plpgsql
as $$
    BEGIN
        NEW.updated_at=now();
        RETURN NEW;
    END;
$$;