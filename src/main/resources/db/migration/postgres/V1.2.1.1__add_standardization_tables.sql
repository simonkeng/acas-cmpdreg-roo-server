CREATE TABLE standardization_settings
(
  id bigint NOT NULL,
  version integer,
  current_settings_hash integer,
  current_settings text,
  modified_date timestamp without time zone,
  needs_standardization boolean
)
WITH (
  OIDS=FALSE
);

ALTER TABLE standardization_settings
  OWNER TO compound_admin;

CREATE TABLE standardization_dryrun_compound
(
  id bigint NOT NULL,
  version integer,
  run_number integer,
  qc_date timestamp without time zone,
  parent_id bigint,
  changed_structure boolean,
  old_mol_weight double precision,
  new_mol_weight double precision,
  old_dupe_count integer,
  new_dupe_count integer,
  new_duplicates text,
  display_change boolean,  
  old_duplicates text,
  as_drawn_display_change boolean,  
  corp_name character varying(255),
  stereo_category character varying(255),
  stereo_comment character varying(255),
  alias character varying(1024),
  cd_id integer NOT NULL,
  mol_structure text,
  comment character varying(2000),
  ignore boolean,
  CONSTRAINT stndzn_dryrun_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE standardization_dryrun_compound
  OWNER TO compound_admin;
    
CREATE INDEX stndzn_dry_run_cdid_idx ON standardization_dryrun_compound USING btree (cd_id);

CREATE TABLE standardization_history
(
  id bigint NOT NULL,
  version integer,
  settings_hash integer,
  settings text,
  date_of_standardization timestamp without time zone,
  structures_standardized_count integer,
  new_duplicate_count integer,
  old_duplicate_count integer,
  display_change_count integer,
  as_drawn_display_change_count integer,
  changed_structure_count integer,
  CONSTRAINT stndzn_history_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE standardization_history
  OWNER TO compound_admin;
