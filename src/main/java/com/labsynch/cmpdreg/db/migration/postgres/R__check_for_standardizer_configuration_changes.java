package com.labsynch.cmpdreg.db.migration.postgres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.transaction.Transactional;

import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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
		
		String selectStandardizationSettingsSQL = "SELECT * FROM standardization_settings";
		String selectCountParentSQL = "SELECT count(id) FROM parent";

		try{
			@SuppressWarnings("unchecked")
			StandardizationSettings standardizationSettings = (StandardizationSettings)jdbcTemplate.queryForObject(selectStandardizationSettingsSQL, new StandardizationSettingsRowMapper());
			logger.debug("Standardizer configs have changed, marking 'standardization needed' according to configs as " +standardizerConfigs.getShouldStandardize());
			standardizationSettings.setNeedsStandardization(standardizerConfigs.getShouldStandardize());
			standardizationSettings.save(jdbcTemplate);
		}catch(EmptyResultDataAccessException e){
			logger.debug("No standardization settings found in database");
			StandardizationSettings standardizationSettings = new StandardizationSettings();
			int numberOfParents = jdbcTemplate.queryForObject(selectCountParentSQL, Integer.class);
			if(numberOfParents == 0) {
				logger.debug("There are no parents registered, we can assume stanardization configs match the database standardization state so storing configs as the stanardization_settings");
				standardizationSettings.setNeedsStandardization(false);
				standardizationSettings.setCurrentSettings(standardizerConfigs.toJson());
				standardizationSettings.setCurrentSettingsHash(standardizerConfigs.hashCode());
				standardizationSettings.setModifiedDate(new Date());
				standardizationSettings.save(jdbcTemplate);
			} else {
				if(standardizerConfigs.getShouldStandardize() == false) {
					logger.warn("Standardization is turned off so marking the database as not requiring standardization at this time");
					standardizationSettings.setNeedsStandardization(false);
					standardizationSettings.setCurrentSettings(standardizerConfigs.toJson());
					standardizationSettings.setCurrentSettingsHash(standardizerConfigs.hashCode());
					standardizationSettings.setModifiedDate(new Date());
					standardizationSettings.save(jdbcTemplate);
				} else {
					logger.warn("Standardization is turned on but the database has not been stanardized so we don't know the current database stanardization state, stanardization_settings will reflect the unknown state");
				}
			}
		}

	}
	
	private class StandardizationSettings {

	    private Long id;
	    private Long version;
	    private String currentSettings;
	    private Date modifiedDate;
	    private Boolean needsStandardization;
	    private int currentSettingsHash;

	    public Long getId() {
	        return this.id;
	    }
	    
	    public void save(JdbcTemplate jdbcTemplate) {
	    	if(this.getId() == null) {
				String insert = "INSERT INTO standardization_settings"
						+ " (id, version, current_settings_hash, current_settings, modified_date, needs_standardization) VALUES "
						+ "((SELECT nextval('hibernate_sequence')), "
						+ ""+0+", "
						+ ""+this.getCurrentSettingsHash()+", "
						+ "'"+this.getCurrentSettings()+"', "
						+ "'"+new java.sql.Timestamp(this.getModifiedDate().getTime())+"', "
						+ "'"+this.getNeedsStandardization()+"'"
						+ ")";
				jdbcTemplate.update(insert);

	    	} else {
				String update = "UPDATE standardization_settings "
						+ "set (version, current_settings_hash, current_settings, modified_date, needs_standardization) = ("
						+ ""+(this.getVersion()+1)+", "
						+ ""+this.getCurrentSettingsHash()+", "
						+ "'"+this.getCurrentSettings()+"', "
						+ "'"+new java.sql.Timestamp(this.getModifiedDate().getTime())+"', "
						+ "'"+this.getNeedsStandardization()+"' "
						+ ") WHERE id = "+this.getId();
				jdbcTemplate.update(update);
	    	}
		

			
		}

		public void setId(Long id) {
	        this.id = id;
	    }

	    public Long getVersion() {
	        return this.version;
	    }
	    
	    public void setVersion(Long version) {
	        this.version = version;
	    }

	    
	    public String getCurrentSettings() {
	        return this.currentSettings;
	    }
	    
	    public void setCurrentSettings(String currentSettings) {
	        this.currentSettings = currentSettings;
	    }
	    
	    public Date getModifiedDate() {
	        return this.modifiedDate;
	    }
	    
	    public void setModifiedDate(Date modifiedDate) {
	        this.modifiedDate = modifiedDate;
	    }
	    
	    public Boolean getNeedsStandardization() {
	        return this.needsStandardization;
	    }
	    
	    public void setNeedsStandardization(Boolean needsStandardization) {
	        this.needsStandardization = needsStandardization;
	    }
	    
	    public int getCurrentSettingsHash() {
	        return this.currentSettingsHash;
	    }
	    
	    public void setCurrentSettingsHash(int currentSettingsHash) {
	        this.currentSettingsHash = currentSettingsHash;
	    }
	    
	    
	}
	
	@SuppressWarnings("rawtypes")
	public class StandardizationSettingsRowMapper implements RowMapper
	{
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			StandardizationSettings standardizationSettings = new StandardizationSettings();
			standardizationSettings.setId(rs.getLong("id"));
			standardizationSettings.setVersion(rs.getLong("version"));
			standardizationSettings.setCurrentSettingsHash(rs.getInt("current_settings_hash"));
			standardizationSettings.setCurrentSettings(rs.getString("current_settings"));
			standardizationSettings.setModifiedDate(rs.getDate("modified_date"));
			standardizationSettings.setNeedsStandardization(rs.getBoolean("needs_standardization"));
			return standardizationSettings;
		}
	}

	@Override
	public Integer getChecksum() {
		int hash = standardizerConfigs.hashCode();
		logger.debug("standardizerSettings configuration hash:" + hash);

		return java.util.Objects.hash(hash);
	}

}

