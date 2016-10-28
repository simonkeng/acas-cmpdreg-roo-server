package com.labsynch.cmpdreg.service;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.dto.SearchCompoundReturnDTO;
import com.labsynch.cmpdreg.dto.SearchFormDTO;
import com.labsynch.cmpdreg.dto.SearchFormReturnDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class SearchFormMulitQueryTest5 {

	private static final Logger logger = LoggerFactory.getLogger(SearchFormMulitQueryTest5.class);

	@Before
	public void loginTestUser() {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("cchemist", "cchemist"));
	}


	@Autowired
	private ChemStructureService structureService;

	@Autowired
	private SearchFormService searchFormService;


	@Test
	public void searchParentAliasTest() throws ParseException {

		SearchFormDTO searchParams = new SearchFormDTO();

		String alias = "";
		String aliasContSelect = "contains";
		Scientist chemist = new Scientist();
		chemist.setId(0L);
		chemist.setCode("anyChemist");
		String corpNameFrom = "2";
		String corpNameTo = "";
		String dateFrom = "";
		String dateTo = "";
		String lotCorpName = null;
		Long maxParentNumber = null;
		Long minParentNumber = null;
		String molStructure = "";
		String parentCorpName = null;
		float percentSimilarity = 0.0f;
		String saltFormCorpName = null;
		String searchType = "EXACT";		

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

		SearchFormReturnDTO results = searchFormService.findQuerySaltForms(searchParams);
		logger.debug(results.toString());
		logger.debug("number of salt forms found: " + results.getFoundCompounds().size());

		if (!results.getFoundCompounds().isEmpty()) {
			SearchCompoundReturnDTO result = results.getFoundCompounds().iterator().next();
			logger.debug("result - stereoCategoryName: " + result.getStereoCategoryName());
		}
		
		logger.debug(searchParams.toJson());

	}
}
