\connect prod

set search_path = "compound";

ALTER USER seurat SET search_path to seurat, acas, compound;
GRANT USAGE ON SCHEMA compound to seurat;
GRANT SELECT ON ALL TABLES in SCHEMA compound to seurat;
GRANT INSERT,DELETE ON JCHEMPROPERTIES_CR to seurat;
GRANT UPDATE ON JCHEMPROPERTIES to seurat;
