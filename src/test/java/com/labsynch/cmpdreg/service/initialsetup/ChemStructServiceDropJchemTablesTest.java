package com.labsynch.cmpdreg.service.initialsetup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.cmpdreg.service.ChemStructureService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class ChemStructServiceDropJchemTablesTest {

	private static final Logger logger = LoggerFactory.getLogger(ChemStructServiceDropJchemTablesTest.class);

	@Autowired
	private ChemStructureService chemStructServ;


	@Test
	public void dropAllJChemTableTest() {

		chemStructServ.dropJChemTable("Salt_Structure");
		chemStructServ.dropJChemTable("SaltForm_Structure");
		chemStructServ.dropJChemTable("Parent_Structure");
		logger.debug("all jchem tables dropped");

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

