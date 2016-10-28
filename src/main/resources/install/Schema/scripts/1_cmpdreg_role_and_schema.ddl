--
-- COMPOUND
--

\connect postgres

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET escape_string_warning = 'off';

--
-- Roles
--

DROP ROLE compound;
CREATE ROLE compound LOGIN PASSWORD 'compound'
   VALID UNTIL 'infinity';
COMMENT ON ROLE "compound" IS 'Comound Registration User';


\connect prod


SET standard_conforming_strings = off;
SET escape_string_warning = 'off';

--
-- COMPOUND SCHEMA
--

DROP SCHEMA compound;
CREATE SCHEMA compound
       AUTHORIZATION compound;
COMMENT ON SCHEMA compound IS 'Compound Schema';

GRANT ALL ON SCHEMA compound TO postgres;
ALTER USER compound SET search_path to compound;
