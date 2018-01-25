package com.labsynch.cmpdreg.db.migration.postgres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.transaction.Transactional;

import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;

import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.dto.configuration.StandardizerSettingsConfigDTO;
import com.labsynch.cmpdreg.utils.Configuration;


public class R__check_for_standardizer_configuration_changes implements SpringJdbcMigration, MigrationChecksumProvider {
 
	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();
	StandardizerSettingsConfigDTO standardizerConfigs = mainConfig.getStandardizerSettings();

	Logger logger = LoggerFactory.getLogger(R__check_for_standardizer_configuration_changes.class);
	//standardizer
	@Transactional
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		logger.debug("Standardizer configs have changed");
	}

	@Override
	public Integer getChecksum() {
		int hash = standardizerConfigs.hashCode();
		logger.debug("standardizerSettings configuration hash:" + hash);

		return java.util.Objects.hash(hash);
	}

}

