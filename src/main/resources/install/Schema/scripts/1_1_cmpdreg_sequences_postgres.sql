CREATE SEQUENCE compound.hibernate_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE compound.hibernate_sequence
  OWNER TO compound_admin;

  CREATE SEQUENCE compound.parent_structure_cd_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1011
  CACHE 1;
ALTER TABLE compound.parent_structure_cd_id_seq
  OWNER TO compound_admin;

  CREATE SEQUENCE compound.parent_structure_ul_update_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 4082
  CACHE 1;
ALTER TABLE compound.parent_structure_ul_update_id_seq
  OWNER TO compound_admin;

  CREATE SEQUENCE compound.salt_structure_cd_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 6
  CACHE 1;
ALTER TABLE compound.salt_structure_cd_id_seq
  OWNER TO compound_admin;

  CREATE SEQUENCE compound.salt_structure_ul_update_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 12
  CACHE 1;
ALTER TABLE compound.salt_structure_ul_update_id_seq
  OWNER TO compound_admin;

CREATE SEQUENCE compound.saltform_structure_cd_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE compound.saltform_structure_cd_id_seq
  OWNER TO compound_admin;

  CREATE SEQUENCE compound.saltform_structure_ul_update_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE compound.saltform_structure_ul_update_id_seq
  OWNER TO compound_admin;