package com.labsynch.cmpdreg.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class ChemStructServiceSearchTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ChemStructServiceSearchTest.class);

	@Autowired
	private ChemStructureService chemStructServ;
	
	@Test
    public void queryTautomer1Test() {
    	
    	String plainTable = "parent";
    	String structureTable = "Parent_Structure";
    	String molfile = "C\\C=C(\\C)O";
    	String searchType = "EXACT"; //EXACT DUPLICATE, STEREO_IGNORE, DUPLICATE_TAUTOMER, SUBSTRUCTURE
//    	String searchType = "DUPLICATE"; //EXACT FULL_TAUTOMER DUPLICATE, STEREO_IGNORE, DUPLICATE_TAUTOMER, SUBSTRUCTURE
//    	String searchType = "STEREO_IGNORE"; //EXACT FULL_TAUTOMER DUPLICATE, STEREO_IGNORE, DUPLICATE_TAUTOMER, SUBSTRUCTURE
    	
    	int[] res = chemStructServ.searchMolStructures(molfile, structureTable, plainTable, searchType);
    	
    	logger.debug("Number of hits " + res.length  );
    	for (int q : res){
    		logger.debug("found a CdId: " + q);
    	}
    	
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
