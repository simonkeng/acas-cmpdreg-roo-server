package com.labsynch.cmpdreg.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.BulkLoadFile;
import com.labsynch.cmpdreg.domain.BulkLoadTemplate;
import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.dto.BulkLoadPropertiesDTO;
import com.labsynch.cmpdreg.dto.BulkLoadPropertyMappingDTO;
import com.labsynch.cmpdreg.dto.BulkLoadRegisterSDFRequestDTO;
import com.labsynch.cmpdreg.dto.BulkLoadRegisterSDFResponseDTO;
import com.labsynch.cmpdreg.dto.BulkLoadSDFPropertyRequestDTO;
import com.labsynch.cmpdreg.dto.PurgeFileResponseDTO;
import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.utils.Configuration;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class BulkLoadServiceWithAliasTest {

	private static final Logger logger = LoggerFactory.getLogger(BulkLoadServiceWithAliasTest.class);
	
	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();
	private static final boolean isUnitTestDB = mainConfig.getServerSettings().isUnitTestDB();
	private static final boolean isInitalDBLoad = mainConfig.getServerSettings().isInitalDBLoad();
	
	
	@Autowired
	private BulkLoadService bulkLoadService;
	
	@Autowired
	private ChemStructureService chemStructServ;
	
	private void purgeAllFiles() throws MalformedURLException, IOException{
    	logger.info("Now attempting to purge all files");
    	List<BulkLoadFile> filesToPurge = BulkLoadFile.findAllBulkLoadFiles();
    	for (BulkLoadFile bulkLoadFile : filesToPurge){
        	PurgeFileResponseDTO purgeResponse = bulkLoadService.purgeFile(bulkLoadFile);
        	logger.info(purgeResponse.toJson());
    	}
	}
	

	private void deleteAllCmpds(){
		List<Lot> lots = Lot.findAllLots();
		for (Lot lot : lots){
			lot.remove();
		}
		List<SaltForm> saltForms = SaltForm.findAllSaltForms();
		for (SaltForm saltForm : saltForms){
			saltForm.remove();
		}
		Parent.deleteAllParents();
		
//		chemStructServ.dropJChemTable("parent_structure");
//		chemStructServ.createJChemTable("parent_structure", true);
	}

	@Test
	public void deleteCmpds() throws MalformedURLException, IOException{
		if (isUnitTestDB){
			purgeAllFiles();
			deleteAllCmpds();			
		}
	}
		
	
	//@Test
//	@Transactional()
	@Rollback(value=false)
	public void basicRegisterSDFTest(){
		if (isUnitTestDB){

//			purgeAllFiles();
//			deleteAllCmpds();
			
			String fileName = "src/test/resources/nciSample9_with_alias.sdf";
			
//			String fileName = "/Users/goshiro2014/Downloads/test_dupes.sdf";
			
	    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
	    	mappings.add(new BulkLoadPropertyMappingDTO("CAS Number", "Formula", false, null));
	    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
	    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
	    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", null, true, "2015/06/12"));
	    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", null, true, "A1234-043"));
	    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, true, "HPLC"));
	    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Common Name", "Name", false, null));
	    	mappings.add(new BulkLoadPropertyMappingDTO("Parent LiveDesign Corp Name", "Parent LiveDesign Corp Name", false, null));
	    	
	    	
	    	if (Scientist.findScientistsByCodeEquals("cchemist").getResultList().size() == 0){
		    	Scientist scientist = new Scientist();
		    	scientist.setName("cchemist");
		    	scientist.setCode("cchemist");
		    	scientist.setIsChemist(true);
		    	scientist.persist();	    		
	    	}
	    	

	    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
	    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
	    	mappings = results.getBulkLoadProperties();
	    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
	    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
	    	bulkLoadService.registerSdf(registerRequestDTO);
//	    	logger.info(results.toJson());
//	    	Assert.assertFalse(results.getSdfProperties().isEmpty());
//	    	Assert.assertTrue(results.getNumRecordsRead() == 9);
//	    	Assert.assertFalse(results.getDbProperties().isEmpty());
//	    	Assert.assertFalse(results.getBulkLoadProperties().isEmpty());
//	    	Assert.assertEquals(1, results.getBulkLoadProperties().size());
			
		}
		
	}

}


