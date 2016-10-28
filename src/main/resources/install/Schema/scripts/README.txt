

# 1_cmpdreg_role_and_schema.ddl
# Creates a compound role and schema
# edit the script to change schema or passwords (default is role: compound, schema: compound, password: compound"
psql -U postgres -p 3247 -f 1_cmpdreg_role_and_schema.ddl

# 2_set_defaults.sh
# sets defaults for application settings which views use to link back to compound reg
# uses hostname --fqdn to determine "host" for application_settings as well as the route to curl PUT the values
sh 2_set_defaults.sh

# 3_api_views.ddl
# connects to schema "compound" created in step 1, creates views and changes ownership to role "compound"
psql -U postgres -p 3247 -f 2_api_views.ddl

# 4_seurat_grants.ddl
# alters the seurat search path in order to find "public, acas, compound"
# grants usage to seurat on schema compound
# grants select on all tables in schema compound to seurat
# grants necessary permissions to jchem tables for structure searches
psql -U postgres -p 3247 -f 4_seurat_grants.ddl
