package com.labsynch.cmpdreg.db.migration.postgres.jchem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chemaxon.jchem.db.StructureTableOptions;
import chemaxon.jchem.db.UpdateHandler;
import chemaxon.util.ConnectionHandler;

public class V1_0_8_1_2__Add_standardization_jchem_tables implements JdbcMigration {
 
	Logger logger = LoggerFactory.getLogger(V1_0_8_1_2__Add_standardization_jchem_tables.class);

	//create the jchem table to store the compounds

	public void migrate(Connection conn) throws Exception {
		logger.info("creating standardization_dryrun_structure table");
		conn.setAutoCommit(true);
		logger.info("connection autocommit mode: " + conn.getAutoCommit());
		logger.info("getTransactionIsolation  " + conn.getTransactionIsolation());

		createJChemTable(conn, "compound.standardization_dryrun_structure", true);

		conn.setAutoCommit(false);
		logger.info("connection autocommit mode: " + conn.getAutoCommit());

	}	

	private boolean createJChemTable(Connection conn, String tableName, boolean tautomerDupe) {
		ConnectionHandler ch = new ConnectionHandler();
		ch.setConnection(conn);
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		StructureTableOptions options = new StructureTableOptions(tableName, StructureTableOptions.TABLE_TYPE_MOLECULES);
		options.setTautomerDuplicateChecking(tautomerDupe);

		try {
			String[] tables = UpdateHandler.getStructureTables(ch);
			List<String> tableList = Arrays.asList(tables); 
			if (!tableList.contains(tableName)){
				UpdateHandler.createStructureTable(ch, options );
				logger.info("created the Jchem structure table " + tableName );
			}
		} catch (SQLException e) {
			logger.error("SQL error. Unable to create the Jchem structure table " + tableName );
			e.printStackTrace();
		}

		return true;
		
	}



}

