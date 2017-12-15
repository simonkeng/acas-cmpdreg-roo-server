package com.labsynch.cmpdreg.service;

import java.util.Collection;
import java.util.HashSet;

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
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.BulkLoadFile;
import com.labsynch.cmpdreg.domain.BulkLoadTemplate;
import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.dto.BulkLoadPropertiesDTO;
import com.labsynch.cmpdreg.dto.BulkLoadPropertyMappingDTO;
import com.labsynch.cmpdreg.dto.BulkLoadRegisterSDFRequestDTO;
import com.labsynch.cmpdreg.dto.BulkLoadRegisterSDFResponseDTO;
import com.labsynch.cmpdreg.dto.BulkLoadSDFPropertyRequestDTO;
import com.labsynch.cmpdreg.dto.PurgeFileResponseDTO;
import com.labsynch.cmpdreg.dto.SearchFormDTO;
import com.labsynch.cmpdreg.dto.SearchFormReturnDTO;
import com.labsynch.cmpdreg.dto.SearchResultExportRequestDTO;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ExportServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(ExportServiceTest.class);
	
	@Autowired
	private ExportService exportService;
	
	@Autowired
	private SearchFormService searchFormService;
	
	@Test
	@Transactional
	public void exportLots(){
    	Collection<Lot> lots = Lot.findLotEntries(0, 100);
    	Collection<String> lotCorpNames = new HashSet<String>();
    	for (Lot lot : lots){
    		lotCorpNames.add(lot.getCorpName());
    	}
    	Assert.assertFalse(lotCorpNames.isEmpty());
    	String filePath = "src/test/resources/test_export.sdf";
    	try{
    		exportService.exportLots(filePath, lotCorpNames);
    	}catch (Exception e){
    		logger.error("Caught an error",e);
    	}
	}
	
	@Test
	@Transactional
	public void exportSearchResults() throws CmpdRegMolFormatException{
		SearchFormDTO searchParams = new SearchFormDTO();
		Scientist chemist = Scientist.findScientistsByCodeEquals("cchemist").getSingleResult();
		searchParams.setChemist(chemist);
		searchParams.setDateFrom("");
		searchParams.setDateTo("");
		searchParams.setSearchType("substructure");
		searchParams.setCorpNameFrom("");
		searchParams.setCorpNameTo("");
		searchParams.setAlias("");
		SearchFormReturnDTO searchResults = searchFormService.findQuerySaltForms(searchParams);
		String filePath = "src/test/resources/test_search_export.sdf";
		SearchResultExportRequestDTO exportRequest = new SearchResultExportRequestDTO(filePath, searchResults);
		try{
			exportService.exportSearchResults(exportRequest);
		}catch (Exception e){
    		logger.error("Caught an error",e);
		}
	}
	

}


