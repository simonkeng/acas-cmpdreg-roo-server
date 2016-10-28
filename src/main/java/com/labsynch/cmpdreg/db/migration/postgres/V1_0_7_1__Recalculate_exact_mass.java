package com.labsynch.cmpdreg.db.migration.postgres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.transaction.Transactional;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import chemaxon.formats.MolFormatException;
import chemaxon.struc.Molecule;
import chemaxon.util.MolHandler;

public class V1_0_7_1__Recalculate_exact_mass implements SpringJdbcMigration {
 
	Logger logger = LoggerFactory.getLogger(V1_0_7_1__Recalculate_exact_mass.class);

	@Transactional
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		logger.debug("attempting to pull out parent mol structure");

		String selectParentIds = "SELECT id FROM parent WHERE id IS NOT null";
		String selectParentByIdSQL = "SELECT * FROM parent WHERE id = ?";
		String updateParentWeights = "UPDATE parent SET mol_weight = ?, exact_mass = ? WHERE id = ?";

		List<Integer> ids = jdbcTemplate.queryForList(selectParentIds, Integer.class);

		for (Integer id : ids){
			ParentStructureObject parent = (ParentStructureObject)jdbcTemplate.queryForObject(selectParentByIdSQL, new Object[] { id }, new ParentRowMapper());
			if (logger.isDebugEnabled()) logger.debug(parent.getMolStructure());
			Double molWeight = getMolWeight(parent.getMolStructure());
			Double exactMass = getExactMass(parent.getMolStructure());
			
			int rs2 = jdbcTemplate.update(updateParentWeights,  new Object[] { molWeight, exactMass, id });
		}
	}
	
	private class ParentStructureObject{
		private long id;
		private String molStructure;
		
		public long getId(){
			return this.id;
		}
		
		public String getMolStructure(){
			return this.molStructure;
		}
		
		public void setId(long id){
			this.id = id;
		}
		
		public void setMolStructure(String molStructure){
			this.molStructure = molStructure;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public class ParentRowMapper implements RowMapper
	{
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			ParentStructureObject parent = new ParentStructureObject();
			parent.setId(rs.getLong("id"));
			parent.setMolStructure(rs.getString("mol_structure"));
			return parent;
		}
	}
	
	private double getMolWeight(String molStructure) {
		MolHandler mh = null;
		boolean badStructureFlag = false;
		Molecule mol = null;
		try {
			mh = new MolHandler(molStructure);
			mol = mh.getMolecule();			
		} catch (MolFormatException e) {
			badStructureFlag = true;
		}

		if (!badStructureFlag){
			return mol.getMass();
		} else {
			return 0d;
		}
	}

	private double getExactMass(String molStructure) {
		MolHandler mh = null;
		boolean badStructureFlag = false;
		Molecule mol = null;
		try {
			mh = new MolHandler(molStructure);
			mol = mh.getMolecule();			
		} catch (MolFormatException e) {
			badStructureFlag = true;
		}

		if (!badStructureFlag){
			return mol.getExactMass();
		} else {
			return 0d;
		}
	}


}

