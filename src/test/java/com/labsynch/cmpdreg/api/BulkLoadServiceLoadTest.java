package com.labsynch.cmpdreg.api;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.BulkLoadFile;
import com.labsynch.cmpdreg.dto.BulkLoadPropertiesDTO;
import com.labsynch.cmpdreg.dto.BulkLoadPropertyMappingDTO;
import com.labsynch.cmpdreg.dto.BulkLoadRegisterSDFRequestDTO;
import com.labsynch.cmpdreg.dto.BulkLoadRegisterSDFResponseDTO;
import com.labsynch.cmpdreg.dto.BulkLoadSDFPropertyRequestDTO;
import com.labsynch.cmpdreg.service.BulkLoadService;
import com.labsynch.cmpdreg.service.ChemStructureService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", 
		"classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class BulkLoadServiceLoadTest {

	private static final Logger logger = LoggerFactory.getLogger(BulkLoadServiceLoadTest.class);
	
	@Autowired
	private BulkLoadService bulkLoadService;
		
	@Autowired
	private ChemStructureService chemStructureService;
	
	
//	@Test
//	@Transactional
	@Rollback(value=false)
	public void registerSDFTest_golden_path(){
		
	  	String fileName = "/Users/goshiro2014/Documents/McNeilco_2012/clients/CompoundRegistration/fixBugs/NKM-070114_assay.csv";
	  	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
	  	mappings.add(new BulkLoadPropertyMappingDTO("Parent Common Name", "Common Chemical Name", false, null));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Parent Corp Name", "Corporate ID", false, null));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Lot Amount", "Lot Amount Prepared", false, null));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Lot Amount Units", "Lot Amount Units", false, null));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Lot Color", "Lot Appearance", false, null));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", "Lot Date Prepared", true, "2222-01-01"));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", "Lot Notebook Page", true, null));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Lot Number", "Lot Number", true, "1"));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity", "Lot Purity", false, null));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Lot Comments", "Lot Register Comment", false, null));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Lot Salt Equivalents", "Lot Salt Equivalents", false, null));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Lot Salt Abbrev", "Lot Salt Name", false, null));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", "Lot Scientist", true, null));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Lot Solution Amount", "Lot Solution Amount", false, null));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Lot Solution Amount Units", "Lot Solution Amount Units", false, null));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Lot Supplier", "Lot Supplier", false, null));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Project", "Project Code Name", true, "KRAS"));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, true, "Unknown"));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
	  	mappings.add(new BulkLoadPropertyMappingDTO("Lot Is Virtual", "Lot Is Virtual", false, "false"));
	  	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 2, null, null, mappings);
		
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	mappings = results.getBulkLoadProperties();
    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	BulkLoadRegisterSDFResponseDTO result = bulkLoadService.registerSdf(registerRequestDTO);
    	logger.info(result.toJson());
	}
	

//	@Test
	@Transactional
	public void checkACASDependencies(){
		BulkLoadFile file = BulkLoadFile.findBulkLoadFilesByRecordedByEquals("cchemist").getResultList().get(0);
		logger.info("Checking dependencies for: "+file.getFileName());
		logger.info(bulkLoadService.checkPurgeFileDependencies(file).toJson());
	}
	


}


