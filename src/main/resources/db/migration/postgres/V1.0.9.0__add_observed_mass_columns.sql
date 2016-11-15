DO $$ 
    BEGIN
        BEGIN
            ALTER TABLE lot ADD COLUMN observed_mass_one double precision,
							ADD COLUMN observed_mass_two double precision;
        EXCEPTION
            WHEN duplicate_column THEN SELECT version();
        END;
    END;
$$
