package com.labsynch.cmpdreg.service;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.Project;
import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.dto.BulkLoadPropertiesDTO;
import com.labsynch.cmpdreg.dto.BulkLoadPropertyMappingDTO;
import com.labsynch.cmpdreg.dto.BulkLoadRegisterSDFRequestDTO;
import com.labsynch.cmpdreg.dto.BulkLoadSDFPropertyRequestDTO;
import com.labsynch.cmpdreg.dto.SearchCompoundReturnDTO;
import com.labsynch.cmpdreg.dto.SearchFormDTO;
import com.labsynch.cmpdreg.dto.SearchFormReturnDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class ProjectRolesTest {

	private static final Logger logger = LoggerFactory.getLogger(ProjectRolesTest.class);

	@Before
	public void loginTestUser() {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("cchemist", "cchemist"));
	}


	@Autowired
	private SearchFormService searchFormService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private BulkLoadService bulkLoadService;

	@Test
	@Transactional
	@Rollback(value=false)
	public void loadAgainstProjectTest(){
		boolean okToRun = false;
		try{
			Scientist jappleseed = Scientist.findScientistsByCodeEquals("jappleseed").getSingleResult();
			okToRun = true;
		}catch(Exception e){
			logger.error("Create user jappleseed before running this test.");
			okToRun = false;
		}
		if(okToRun){
			//load a set of compounds with Apple project
			Collection<Project> authorizedProjects = projectService.getACASProjectsByUser("jappleseed");
			Project apple = null;
			for (Project project : authorizedProjects){
				if (project.getName().equalsIgnoreCase("Apple")) apple = project;
			}
			String fileName = "src/test/resources/nciSample9_with_props.sdf";
			Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
	    	mappings.add(new BulkLoadPropertyMappingDTO("CAS Number", "Formula", false, null));
	    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
	    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "jappleseed"));
	    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", null, true, "2015/06/12"));
	    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", null, true, "A1234-043"));
	    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, true, "HPLC"));
	    	mappings.add(new BulkLoadPropertyMappingDTO("Project", null, true, apple.getCode()));
	    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
	    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
	    	mappings = results.getBulkLoadProperties();
	    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
	    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
	    	bulkLoadService.registerSdf(registerRequestDTO);
		}
	}

	@Test
	public void searchWithProjectRestrictions() throws ParseException {

		SearchFormDTO searchParams = new SearchFormDTO();

		String alias = "";
		String aliasContSelect = "contains";
		Scientist chemist = new Scientist();
		chemist.setId(0L);
		chemist.setCode("anyChemist");
		String corpNameFrom = "";
		String corpNameTo = "";
		String dateFrom = "";
		String dateTo = "";
		String lotCorpName = null;
		Long maxParentNumber = null;
		Long minParentNumber = null;
		String molStructure = "C";
		String parentCorpName = null;
		float percentSimilarity = 0.0f;
		String saltFormCorpName = null;
		String searchType = "SUBSTRUCTURE";	
		String loggedInUser = "jappleseed";

		searchParams.setAlias(alias);
		searchParams.setAliasContSelect(aliasContSelect);
		searchParams.setChemist(chemist);
		searchParams.setCorpNameFrom(corpNameFrom);
		searchParams.setCorpNameTo(corpNameTo);
		searchParams.setDateFrom(dateFrom);
		searchParams.setDateTo(dateTo);
		searchParams.setLotCorpName(lotCorpName);
		searchParams.setMaxParentNumber(maxParentNumber);
		searchParams.setMinParentNumber(minParentNumber);
		searchParams.setMolStructure(molStructure);
		searchParams.setParentCorpName(parentCorpName);
		searchParams.setPercentSimilarity(percentSimilarity);
		searchParams.setSaltFormCorpName(saltFormCorpName);
		searchParams.setSearchType(searchType);
		searchParams.setLoggedInUser(loggedInUser);

		//		List<SaltForm> saltForms = SaltForm.findSaltFormsByMeta(searchParams).getResultList();
		//		List<Integer> cdIds = SaltForm.findSaltFormCdIdsByMeta(searchParams);
		//		logger.debug("number of salt forms found: " + saltForms.size());
		//		logger.debug("number of cdIds found: " + cdIds.toString());

		SearchFormReturnDTO results = searchFormService.findQuerySaltForms(searchParams);
		logger.debug(results.toString());
		logger.debug("number of salt forms found: " + results.getFoundCompounds().size());
		Assert.assertEquals(9, results.getFoundCompounds().size());
		logger.debug(searchParams.toJson());

	}
	
	@Test
	public void searchNotAllowedProject() throws ParseException {
		//This test is written for a database populated with only the loadAgainstProjectTest results.
		//This test will fail unless the configuration.json property "projectRestrictions" is true
		SearchFormDTO searchParams = new SearchFormDTO();

		String alias = "";
		String aliasContSelect = "contains";
		Scientist chemist = new Scientist();
		chemist.setId(0L);
		chemist.setCode("anyChemist");
		String corpNameFrom = "";
		String corpNameTo = "";
		String dateFrom = "";
		String dateTo = "";
		String lotCorpName = null;
		Long maxParentNumber = null;
		Long minParentNumber = null;
		String molStructure = "C";
		String parentCorpName = null;
		float percentSimilarity = 0.0f;
		String saltFormCorpName = null;
		String searchType = "SUBSTRUCTURE";	
		String loggedInUser = "bsplit";

		searchParams.setAlias(alias);
		searchParams.setAliasContSelect(aliasContSelect);
		searchParams.setChemist(chemist);
		searchParams.setCorpNameFrom(corpNameFrom);
		searchParams.setCorpNameTo(corpNameTo);
		searchParams.setDateFrom(dateFrom);
		searchParams.setDateTo(dateTo);
		searchParams.setLotCorpName(lotCorpName);
		searchParams.setMaxParentNumber(maxParentNumber);
		searchParams.setMinParentNumber(minParentNumber);
		searchParams.setMolStructure(molStructure);
		searchParams.setParentCorpName(parentCorpName);
		searchParams.setPercentSimilarity(percentSimilarity);
		searchParams.setSaltFormCorpName(saltFormCorpName);
		searchParams.setSearchType(searchType);
		searchParams.setLoggedInUser(loggedInUser);

		//		List<SaltForm> saltForms = SaltForm.findSaltFormsByMeta(searchParams).getResultList();
		//		List<Integer> cdIds = SaltForm.findSaltFormCdIdsByMeta(searchParams);
		//		logger.debug("number of salt forms found: " + saltForms.size());
		//		logger.debug("number of cdIds found: " + cdIds.toString());

		SearchFormReturnDTO results = searchFormService.findQuerySaltForms(searchParams);
		logger.debug(results.toString());
		logger.debug("number of salt forms found: " + results.getFoundCompounds().size());
		Assert.assertEquals(0, results.getFoundCompounds().size());
		Assert.assertTrue(results.isLotsWithheld());
		logger.debug(searchParams.toJson());

	}
}
