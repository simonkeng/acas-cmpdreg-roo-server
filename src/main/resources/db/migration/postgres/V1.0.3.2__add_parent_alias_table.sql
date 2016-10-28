CREATE TABLE parent_alias
(
  id bigint NOT NULL,
  version integer,
  parent bigint,
  alias_name character varying(255),
  ls_type character varying(255),
  ls_kind character varying(255),
  ignored boolean,
  deleted boolean,
  preferred boolean,
  CONSTRAINT parent_alias_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parent_alias
  OWNER TO compound_admin;
  
  
  
CREATE TABLE salt_form_alias
(
  id bigint NOT NULL,
  version integer,
  salt_form bigint,
  alias_name character varying(255),
  ls_type character varying(255),
  ls_kind character varying(255),
  ignored boolean,
  deleted boolean,
  preferred boolean,
  CONSTRAINT saltform_alias_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE salt_form_alias
  OWNER TO compound_admin;  
  
  
CREATE TABLE lot_alias
(
  id bigint NOT NULL,
  version integer,
  lot bigint,
  alias_name character varying(255),
  ls_type character varying(255),
  ls_kind character varying(255),
  ignored boolean,
  deleted boolean,
  preferred boolean,
  CONSTRAINT lot_alias_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE lot_alias
  OWNER TO compound_admin;  
  
  
CREATE TABLE parent_alias_type (
    id bigint NOT NULL,
    type_name character varying(255),
    version integer,
    CONSTRAINT parent_alias_type_pkey PRIMARY KEY (id)
);  

CREATE TABLE salt_form_alias_type (
    id bigint NOT NULL,
    type_name character varying(255),
    version integer,
    CONSTRAINT salt_form_alias_type_pkey PRIMARY KEY (id)
);  

CREATE TABLE lot_alias_type (
    id bigint NOT NULL,
    type_name character varying(255),
    version integer,
    CONSTRAINT lot_alias_type_pkey PRIMARY KEY (id)    
);  


CREATE TABLE parent_alias_kind (
    id bigint NOT NULL,
	parent_alias_type bigint,
    kind_name character varying(255),
    version integer,
    CONSTRAINT parent_alias_kind_pkey PRIMARY KEY (id)
);  

CREATE TABLE salt_form_alias_kind (
    id bigint NOT NULL,
	salt_form_alias_type bigint,
    kind_name character varying(255),
    version integer,
    CONSTRAINT salt_form_alias_kind_pkey PRIMARY KEY (id)
);  

CREATE TABLE lot_alias_kind (
    id bigint NOT NULL,
	lot_alias_type bigint,
    kind_name character varying(255),
    version integer,
    CONSTRAINT lot_alias_kind_pkey PRIMARY KEY (id)
);  