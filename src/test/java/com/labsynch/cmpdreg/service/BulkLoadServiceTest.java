package com.labsynch.cmpdreg.service;

import java.io.IOException;
import java.net.MalformedURLException;
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
import com.labsynch.cmpdreg.dto.BulkLoadPropertiesDTO;
import com.labsynch.cmpdreg.dto.BulkLoadPropertyMappingDTO;
import com.labsynch.cmpdreg.dto.BulkLoadRegisterSDFRequestDTO;
import com.labsynch.cmpdreg.dto.BulkLoadRegisterSDFResponseDTO;
import com.labsynch.cmpdreg.dto.BulkLoadSDFPropertyRequestDTO;
import com.labsynch.cmpdreg.dto.PurgeFileResponseDTO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class BulkLoadServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(BulkLoadServiceTest.class);
	
	@Autowired
	private BulkLoadService bulkLoadService;
	
	@Test
	@Transactional
	public void basicReadSDFTest(){
    	String fileName = "src/test/resources/nciSample9_with_props.sdf";
    	BulkLoadSDFPropertyRequestDTO requestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, null);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(requestDTO);
    	logger.info(results.toJson());
    	Assert.assertFalse(results.getSdfProperties().isEmpty());
    	Assert.assertTrue(results.getNumRecordsRead() == 9);
    	Assert.assertFalse(results.getDbProperties().isEmpty());
    	Assert.assertFalse(results.getBulkLoadProperties().isEmpty());
    	Assert.assertEquals(1, results.getBulkLoadProperties().size());
	}
	
	@Test
	@Transactional
	public void readSDFTestAllLines(){
    	String fileName = "src/test/resources/nciSample9_with_props.sdf";
    	BulkLoadSDFPropertyRequestDTO requestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, -1, null, null, null);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(requestDTO);
    	logger.info(results.toJson());
    	Assert.assertFalse(results.getSdfProperties().isEmpty());
    	Assert.assertTrue(results.getNumRecordsRead() == 9);
    	Assert.assertFalse(results.getDbProperties().isEmpty());
    	Assert.assertFalse(results.getBulkLoadProperties().isEmpty());
    	Assert.assertEquals(1, results.getBulkLoadProperties().size());
	}
	
	@Test
	@Transactional
	public void readSDFTestWithPreviousMappings(){
    	String fileName = "src/test/resources/nciSample9_with_props.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("CAS Number", "Formula", false, null));
    	BulkLoadSDFPropertyRequestDTO requestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(requestDTO);
    	logger.info(results.toJson());
    	Assert.assertFalse(results.getSdfProperties().isEmpty());
    	Assert.assertTrue(results.getNumRecordsRead() == 9);
    	Assert.assertFalse(results.getDbProperties().isEmpty());
    	Assert.assertFalse(results.getBulkLoadProperties().isEmpty());
    	Assert.assertEquals(2, results.getBulkLoadProperties().size());
	}
	
	@Test
	@Transactional
	public void saveAndQueryTemplatesTest(){
		BulkLoadTemplate template1 = new BulkLoadTemplate("Template 1", "{\"mappings\":\"insert test json here\"}", "jane");
		template1.persist();
		BulkLoadTemplate template2 = new BulkLoadTemplate("Template 2", "{\"mappings\":\"insert test json 2 here\"}", "bob");
		template2.persist();
		Collection<BulkLoadTemplate> foundTemplates1 = BulkLoadTemplate.findBulkLoadTemplatesByRecordedByEquals("jane").getResultList();
		Assert.assertEquals(1, foundTemplates1.size());
		Assert.assertEquals("Template 1", foundTemplates1.iterator().next().getTemplateName());
		String json1 = BulkLoadTemplate.toJsonArray(foundTemplates1);
		logger.info(json1);
		Assert.assertTrue(json1.contains("jsonTemplate") && json1.contains("templateName") && json1.contains("recordedBy"));
		Collection<BulkLoadTemplate> foundTemplates2 = BulkLoadTemplate.findBulkLoadTemplatesByRecordedByEquals("bob").getResultList();
		Assert.assertEquals(1, foundTemplates2.size());
		Assert.assertEquals("Template 2", foundTemplates2.iterator().next().getTemplateName());
	}
	
	@Test
	@Transactional
	@Rollback(value = true)
	public void saveDuplicateTemplatesTest(){
		BulkLoadTemplate template1 = new BulkLoadTemplate("Template 1", "{\"mappings\":\"insert test json here\"}", "jane");
		template1.persist();
		try{
			BulkLoadTemplate template2 = new BulkLoadTemplate("Template 1", "{\"mappings\":\"insert test json 1 here\"}", "jane");
			template2.persist();
			template2.flush();
			Assert.assertFalse(true);
		}
		catch (JpaSystemException e){
			Assert.assertNotNull(e);
		}
	}
	
	@Test
	@Transactional
	@Rollback(value = true)
	public void saveTemplateWithServiceTest(){
		BulkLoadTemplate template1 = new BulkLoadTemplate("Template 1", "{\"mappings\":\"insert test json here\"}", "jane");
		BulkLoadTemplate savedTemplate = bulkLoadService.saveBulkLoadTemplate(template1);
		logger.info(savedTemplate.toJson());
		Assert.assertNotNull(savedTemplate);
		Assert.assertTrue(savedTemplate.toJson().contains("insert test json"));
	}
	
	@Test
	@Transactional
	@Rollback(value = true)
	public void saveAndUpdateTemplateWithServiceTest(){
		BulkLoadTemplate template1 = new BulkLoadTemplate("Template 1", "{\"mappings\":\"insert test json here\"}", "jane");
		logger.info(template1.toJson());
		BulkLoadTemplate savedTemplate = bulkLoadService.saveBulkLoadTemplate(template1);
		logger.info(savedTemplate.toJson());
		Assert.assertTrue(savedTemplate.toJson().contains("insert test json"));
		BulkLoadTemplate template2 = new BulkLoadTemplate("Template 1", "{\"mappings\":\"insert updated test json here\"}", "jane");
		savedTemplate = bulkLoadService.saveBulkLoadTemplate(template2);
		logger.info(savedTemplate.toJson());
		Assert.assertFalse(savedTemplate.toJson().contains("insert test json"));
		Assert.assertTrue(savedTemplate.toJson().contains("insert updated test json"));

	}
	
	@Test
	@Transactional
	@Rollback(value = true)
	public void saveTemplateAndConflictWithReadSdfProperties(){
		String fileName = "src/test/resources/nciSample9_with_props.sdf";
		BulkLoadTemplate template1 = new BulkLoadTemplate("Template 1", "[{\"dbProperty\":null,\"defaultVal\":null,\"required\":false,\"sdfProperty\":\"Bogus Sdf Property\"},{\"dbProperty\":\"Lot Comments\",\"defaultVal\":null,\"required\":false,\"sdfProperty\":\"Lot Comments\"},{\"dbProperty\":\"CAS Number\",\"defaultVal\":null,\"required\":false,\"sdfProperty\":\"Formula\"},{\"dbProperty\":\"Parent Stereo Category\",\"defaultVal\":\"unknown\",\"required\":true,\"sdfProperty\":null},{\"dbProperty\":\"Lot Number\",\"defaultVal\":\"10\",\"required\":true,\"sdfProperty\":null},{\"dbProperty\":\"Lot Notebook Page\",\"defaultVal\":\"A1234-043\",\"required\":true,\"sdfProperty\":null},{\"dbProperty\":\"Lot Purity Measured By\",\"defaultVal\":\"HPLC\",\"required\":true,\"sdfProperty\":null},{\"dbProperty\":\"Lot Chemist\",\"defaultVal\":\"cchemist\",\"required\":true,\"sdfProperty\":null}]", "jane");
		logger.info(template1.toJson());
		BulkLoadTemplate savedTemplate = bulkLoadService.saveBulkLoadTemplate(template1);
		logger.info(savedTemplate.toJson());
		Collection<BulkLoadPropertyMappingDTO> mappings = BulkLoadPropertyMappingDTO.fromJsonArrayToBulkLoadProes(savedTemplate.getJsonTemplate());
    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, savedTemplate.getTemplateName(), savedTemplate.getRecordedBy(), mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	logger.info(results.toJson());
    	Assert.assertFalse(results.getErrors().isEmpty());
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
	public void basicRegisterSDFTest(){
		String fileName = "src/test/resources/nciSample9_with_props.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("CAS Number", "Formula", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", null, true, "2015/06/12"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", null, true, "A1234-043"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, true, "HPLC"));
    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	mappings = results.getBulkLoadProperties();
    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	bulkLoadService.registerSdf(registerRequestDTO);
//    	logger.info(results.toJson());
//    	Assert.assertFalse(results.getSdfProperties().isEmpty());
//    	Assert.assertTrue(results.getNumRecordsRead() == 9);
//    	Assert.assertFalse(results.getDbProperties().isEmpty());
//    	Assert.assertFalse(results.getBulkLoadProperties().isEmpty());
//    	Assert.assertEquals(1, results.getBulkLoadProperties().size());
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
	public void registerSDFTest_Lot2(){
		String fileName = "src/test/resources/nciSample9_with_props_lot_2.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Number", "Lot Number", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, false, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", "Lot Synthesis Date", true, "2015/06/15"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", null, true, "A1234-043"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, true, "HPLC"));
    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	mappings = results.getBulkLoadProperties();
    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	bulkLoadService.registerSdf(registerRequestDTO);
//    	logger.info(results.toJson());
//    	Assert.assertFalse(results.getSdfProperties().isEmpty());
//    	Assert.assertTrue(results.getNumRecordsRead() == 9);
//    	Assert.assertFalse(results.getDbProperties().isEmpty());
//    	Assert.assertFalse(results.getBulkLoadProperties().isEmpty());
//    	Assert.assertEquals(1, results.getBulkLoadProperties().size());
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
	public void registerSDFTest_partial_missing_required_property(){
		String fileName = "src/test/resources/nciSample9_with_props_single_missing_lot_synthesis_date.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("CAS Number", "Formula", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", "Lot Synthesis Date", true, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", null, true, "A1234-043"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, true, "HPLC"));
    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	mappings = results.getBulkLoadProperties();
    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	bulkLoadService.registerSdf(registerRequestDTO);
//    	logger.info(results.toJson());
//    	Assert.assertFalse(results.getSdfProperties().isEmpty());
//    	Assert.assertTrue(results.getNumRecordsRead() == 9);
//    	Assert.assertFalse(results.getDbProperties().isEmpty());
//    	Assert.assertFalse(results.getBulkLoadProperties().isEmpty());
//    	Assert.assertEquals(1, results.getBulkLoadProperties().size());
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
	public void registerSDFTest_golden_path(){
		String fileName = "src/test/resources/nciSample9_with_props.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("CAS Number", "Formula", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Number", null, true, "10"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", "Lot Synthesis Date", true, "2015-06-24"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", null, true, "A1234-043"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, true, "HPLC"));
    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	mappings = results.getBulkLoadProperties();
    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	BulkLoadRegisterSDFResponseDTO result = bulkLoadService.registerSdf(registerRequestDTO);
    	logger.info(result.toJson());
//    	logger.info(results.toJson());
//    	Assert.assertFalse(results.getSdfProperties().isEmpty());
//    	Assert.assertTrue(results.getNumRecordsRead() == 9);
//    	Assert.assertFalse(results.getDbProperties().isEmpty());
//    	Assert.assertFalse(results.getBulkLoadProperties().isEmpty());
//    	Assert.assertEquals(1, results.getBulkLoadProperties().size());
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
	public void registerSDFTest_single_salt(){
		String fileName = "src/test/resources/single_compound_hcl_salt.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Number", null, true, "1"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", "Lot Synthesis Date", true, "2015-06-24"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", null, true, "A1234-043"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, true, "HPLC"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Salt Abbrev", "Salt Code", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Salt Equivalents", "Salt Equivs", false, null));
    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	mappings = results.getBulkLoadProperties();
    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	BulkLoadRegisterSDFResponseDTO result = bulkLoadService.registerSdf(registerRequestDTO);
    	logger.info(result.toJson());
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
	public void registerSDFTest_single_compound_multiple_salt(){
		String fileName = "src/test/resources/single_compound_hcl_hbr_salt.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Number", null, true, "2"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", "Lot Synthesis Date", true, "2015-06-24"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", null, true, "A1234-043"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, true, "HPLC"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Salt Abbrev", "Salt Code", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Salt Equivalents", "Salt Equivs", false, null));
    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	mappings = results.getBulkLoadProperties();
    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	BulkLoadRegisterSDFResponseDTO result = bulkLoadService.registerSdf(registerRequestDTO);
    	logger.info(result.toJson());
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
	public void registerAndPurge() throws MalformedURLException, IOException{
		String fileName = "src/test/resources/single_compound_hcl_hbr_salt.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Number", null, true, "2"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", "Lot Synthesis Date", true, "2015-06-24"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", null, true, "A1234-043"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, true, "HPLC"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Salt Abbrev", "Salt Code", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Salt Equivalents", "Salt Equivs", false, null));
    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	mappings = results.getBulkLoadProperties();
    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	BulkLoadRegisterSDFResponseDTO result = bulkLoadService.registerSdf(registerRequestDTO);
    	logger.info(result.toJson());
    	logger.info("Now attempting to purge");
    	BulkLoadFile fileToPurge = BulkLoadFile.findBulkLoadFilesByFileNameEquals("single_compound_hcl_hbr_salt.sdf").getSingleResult();
    	PurgeFileResponseDTO purgeResponse = bulkLoadService.purgeFile(fileToPurge);
    	logger.info(purgeResponse.toJson());
	}
	
	@Test
	@Transactional
	public void checkACASDependencies(){
		BulkLoadFile file = BulkLoadFile.findBulkLoadFilesByRecordedByEquals("cchemist").getResultList().get(0);
		logger.info("Checking dependencies for: "+file.getFileName());
		logger.info(bulkLoadService.checkPurgeFileDependencies(file).toJson());
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
	public void registerSDFTest_errors_on_per_lot_basis(){
		String fileName = "src/test/resources/nciSample9_with_props_single_missing_lot_synthesis_date.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("CAS Number", "Formula", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", null, true, "2015-08-12"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", null, true, "A1234-043"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, false, ""));
    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	mappings = results.getBulkLoadProperties();
    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	BulkLoadRegisterSDFResponseDTO response = bulkLoadService.registerSdf(registerRequestDTO);
    	logger.info(response.toJson());
//    	logger.info(results.toJson());
//    	Assert.assertFalse(results.getSdfProperties().isEmpty());
//    	Assert.assertTrue(results.getNumRecordsRead() == 9);
//    	Assert.assertFalse(results.getDbProperties().isEmpty());
//    	Assert.assertFalse(results.getBulkLoadProperties().isEmpty());
//    	Assert.assertEquals(1, results.getBulkLoadProperties().size());
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
	public void registerSDFTest_mismatched_parent_corpNames(){
		String fileName = "src/test/resources/nciSample9_with_props_single_missing_lot_synthesis_date.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("CAS Number", "Formula", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", null, false, "2015-08-12"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", null, false, "A1234-043"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, false, ""));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Corp Name", null, false, "CMPD-0000002"));
    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	mappings = results.getBulkLoadProperties();
    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	BulkLoadRegisterSDFResponseDTO response = bulkLoadService.registerSdf(registerRequestDTO);
    	logger.info(response.toJson());
//    	logger.info(results.toJson());
//    	Assert.assertFalse(results.getSdfProperties().isEmpty());
//    	Assert.assertTrue(results.getNumRecordsRead() == 9);
//    	Assert.assertFalse(results.getDbProperties().isEmpty());
//    	Assert.assertFalse(results.getBulkLoadProperties().isEmpty());
//    	Assert.assertEquals(1, results.getBulkLoadProperties().size());
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
	public void registerSDFTest_single_salt_with_spaces(){
		String fileName = "src/test/resources/single_compound_hcl_salt_with_spaces.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Salt Abbrev", "Salt Code", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Salt Equivalents", "Salt Equivs", false, null));
    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	mappings = results.getBulkLoadProperties();
    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	BulkLoadRegisterSDFResponseDTO result = bulkLoadService.registerSdf(registerRequestDTO);
    	logger.info(result.toJson());
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
	public void with_parent_alias(){
		String fileName = "src/test/resources/lot_0_lot_1_new_compound_with_parent_corpname.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Common Name", "Parent Corporate ID", false, null));
//    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
//    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
//    	mappings = results.getBulkLoadProperties();
    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	BulkLoadRegisterSDFResponseDTO result = bulkLoadService.registerSdf(registerRequestDTO);
    	logger.info(result.toJson());
	}
	
	@Test
	@Transactional
	@Rollback(value=true)
	public void trim_corp_name_whitespace(){
		String fileName = "src/test/resources/whitespace_corp_names.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Corp Name", "Parent Corporate ID", false, null));
//    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
//    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
//    	mappings = results.getBulkLoadProperties();
    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	BulkLoadRegisterSDFResponseDTO result = bulkLoadService.registerSdf(registerRequestDTO);
    	logger.info(result.toJson());
	}
	
	@Test
	@Transactional
	@Rollback(value=true)
	public void dupe_structure_corp_name_conflicting_stereo_category(){
		String fileName = "src/test/resources/TEST-00003_all_in_one_sdf.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", "Parent Stereo Category", true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Corp Name", "Parent Corp Name", false, null));
//    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
//    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
//    	mappings = results.getBulkLoadProperties();
    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	BulkLoadRegisterSDFResponseDTO result = bulkLoadService.registerSdf(registerRequestDTO);
    	Assert.assertTrue(result.toJson().contains("Mismatched stereo categories for same parent structure and corp name!"));
	}
	
	@Test
	@Transactional
	@Rollback(value=true)
	public void registerSDFTest_projectRestrictionsFromACAS(){
		String fileName = "src/test/resources/nciSample9_with_props.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("CAS Number", "Formula", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", null, true, "2015/06/12"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", null, true, "A1234-043"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, true, "HPLC"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Project", null, true, "PROJ-00000001"));
    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	mappings = results.getBulkLoadProperties();
    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "bfielder", null, mappings);
    	bulkLoadService.registerSdf(registerRequestDTO);
//    	logger.info(results.toJson());
//    	Assert.assertFalse(results.getSdfProperties().isEmpty());
//    	Assert.assertTrue(results.getNumRecordsRead() == 9);
//    	Assert.assertFalse(results.getDbProperties().isEmpty());
//    	Assert.assertFalse(results.getBulkLoadProperties().isEmpty());
//    	Assert.assertEquals(1, results.getBulkLoadProperties().size());
	}
	
	@Test
	@Transactional
	@Rollback(value=true)
	public void registerSDFTest_projectRestrictionsFromACAS_disallowed(){
		String fileName = "src/test/resources/nciSample9_with_props.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("CAS Number", "Formula", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", null, true, "2015/06/12"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", null, true, "A1234-043"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, true, "HPLC"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Project", null, true, "PROJ-00000002"));
    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	mappings = results.getBulkLoadProperties();
    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "bfielder", null, mappings);
    	bulkLoadService.registerSdf(registerRequestDTO);
//    	logger.info(results.toJson());
//    	Assert.assertFalse(results.getSdfProperties().isEmpty());
//    	Assert.assertTrue(results.getNumRecordsRead() == 9);
//    	Assert.assertFalse(results.getDbProperties().isEmpty());
//    	Assert.assertFalse(results.getBulkLoadProperties().isEmpty());
//    	Assert.assertEquals(1, results.getBulkLoadProperties().size());
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
	public void registerSDFTest_newProperties(){
		String fileName = "src/test/resources/nciSample9_with_newly_added_props.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("Project", null, true, "APPLE"));
    	mappings.add(new BulkLoadPropertyMappingDTO("CAS Number", "Formula", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", null, true, "2015/06/12"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", null, true, "A1234-043"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, true, "HPLC"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Annotation", "Parent Annotation", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Compound Type", "Parent Compound Type", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Comment", "Parent Comment", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Lambda", "Lot Lambda", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Absorbance", "Lot Absorbance", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Stock Solvent", "Lot Stock Solvent", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Stock Location", "Lot Stock Location", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Retain Location", "Lot Retain Location", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Observed Mass #1", "Lot Observed Mass #1", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Observed Mass #2", "Lot Observed Mass #2", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Vendor ID", "Lot Vendor ID", false, null));
    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	mappings = results.getBulkLoadProperties();
    	logger.info("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	BulkLoadRegisterSDFResponseDTO response = bulkLoadService.registerSdf(registerRequestDTO);
    	logger.info(response.toJson());
//    	Assert.assertFalse(results.getSdfProperties().isEmpty());
//    	Assert.assertTrue(results.getNumRecordsRead() == 9);
//    	Assert.assertFalse(results.getDbProperties().isEmpty());
//    	Assert.assertFalse(results.getBulkLoadProperties().isEmpty());
//    	Assert.assertEquals(1, results.getBulkLoadProperties().size());
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
	public void registerSDFTest_saltStripping(){
		String fileName = "src/test/resources/salted_structure.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("Project", null, true, "APPLE"));
    	mappings.add(new BulkLoadPropertyMappingDTO("CAS Number", "Formula", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", null, true, "2015/06/12"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", null, true, "A1234-043"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, true, "HPLC"));
    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	mappings = results.getBulkLoadProperties();
    	logger.info("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	BulkLoadRegisterSDFResponseDTO response = bulkLoadService.registerSdf(registerRequestDTO);
    	logger.info(response.toJson());
//    	Assert.assertFalse(results.getSdfProperties().isEmpty());
//    	Assert.assertTrue(results.getNumRecordsRead() == 9);
//    	Assert.assertFalse(results.getDbProperties().isEmpty());
//    	Assert.assertFalse(results.getBulkLoadProperties().isEmpty());
//    	Assert.assertEquals(1, results.getBulkLoadProperties().size());
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
	public void registerSDFTest_lotAndParentAliases(){
		String fileName = "src/test/resources/nciSample9_many_aliases.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("Project", null, true, "PROJ-00000001"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", null, true, "2015/06/12"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", null, true, "A1234-043"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, true, "HPLC"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Common Name", "Parent Common Name", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent LiveDesign Corp Name", "Parent LiveDesign Corp Name", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Alias", "CRO Parent Alias", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Alias", "Duplicate Corp Name", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Alias", "CRO Lot Alias", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Alias", "Collaborator Lot Alias", false, null));
    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	mappings = results.getBulkLoadProperties();
    	logger.info("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	BulkLoadRegisterSDFResponseDTO response = bulkLoadService.registerSdf(registerRequestDTO);
    	logger.info(response.toJson());
//    	Assert.assertFalse(results.getSdfProperties().isEmpty());
//    	Assert.assertTrue(results.getNumRecordsRead() == 9);
//    	Assert.assertFalse(results.getDbProperties().isEmpty());
//    	Assert.assertFalse(results.getBulkLoadProperties().isEmpty());
//    	Assert.assertEquals(1, results.getBulkLoadProperties().size());
	}


}


