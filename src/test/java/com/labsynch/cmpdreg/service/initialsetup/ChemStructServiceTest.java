package com.labsynch.cmpdreg.service.initialsetup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.service.ChemStructureService;
import com.labsynch.cmpdreg.utils.Configuration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class ChemStructServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(ChemStructServiceTest.class);

	@Autowired
	private ChemStructureService chemStructServ;

	// @Test
	public void createJChemTableTest() {

		String tableName = "New_Structure";
		boolean created = chemStructServ.createJChemTable(tableName, true);
		if (created){
			logger.debug("New table " + tableName + " created.");
		} else {
			logger.error("ERROR: table " + tableName + " not created.");
		}
	}



	//   @Test
	public void dropJChemTableTest() {

		String tableName = "New_Structure";
		boolean destroyed = chemStructServ.dropJChemTable(tableName);
		if (destroyed){
			logger.debug("table " + tableName + " destroyed.");
		} else {
			logger.error("ERROR: table " + tableName + " not destroyed.");
		}
	}

	// @Test
	public void dropAllJChemTableTest() {

		chemStructServ.dropJChemTable("Salt_Structure");
		chemStructServ.dropJChemTable("SaltForm_Structure");
		chemStructServ.dropJChemTable("Parent_Structure");

	}

	@Test
	public void createAllJChemTableTest() {
		MainConfigDTO mainConfig = Configuration.getConfigInfo();
		if (mainConfig.getServerSettings().isInitalDBLoad()){
			chemStructServ.createJchemPropertyTable();
			chemStructServ.createJChemTable("Salt_Structure", false);
			chemStructServ.createJChemTable("SaltForm_Structure", true);
			chemStructServ.createJChemTable("Parent_Structure", true);			
		}
	}

	// @Test
	public void createJChemPropTableTest() {

		chemStructServ.createJchemPropertyTable();

	}

	// @Test
	public void queryParentTableTest() {

		String plainTable = "parent";
		String structureTable = "Parent_Structure";
		String molfile = "C1=CC=CC=C1";
		String searchType = "EXACT";

		int[] res = chemStructServ.searchMolStructures(molfile, structureTable, plainTable, searchType);

		logger.debug("Number of hits " + res.length );

	}

	//    @Test
	public void querySaltTableTest() {

		String plainTable = "salt";
		String structureTable = "Salt_Structure";
		String molfile = "CCCCC";
		String searchType = "EXACT";

		int[] res = chemStructServ.searchMolStructures(molfile, structureTable, plainTable, searchType);

		logger.debug("Number of hits " + res.length );

	}

}

//DROP TABLE compound.parent_structure;
//DROP TABLE compound.parent_structure_ul;
//DROP TABLE compound.salt_structure;
//DROP TABLE compound.salt_structure_ul;
//DROP TABLE compound.saltform_structure;
//DROP TABLE compound.saltform_structure_ul;
//DROP TABLE compound.jchemproperties;
//DROP TABLE compound.jchemproperties_cr;

