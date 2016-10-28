package com.labsynch.cmpdreg.service;

import java.text.ParseException;
import java.util.List;

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
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.dto.SearchCompoundReturnDTO;
import com.labsynch.cmpdreg.dto.SearchFormDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
@Transactional
public class LotSearchBySaltFormAndMetaTest {

	private static final Logger logger = LoggerFactory.getLogger(LotSearchBySaltFormAndMetaTest.class);

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

		String alias = "test";
		String aliasContSelect = "contains";
		Scientist chemist = null;
		String corpNameFrom = "";
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
		String searchType = "SUBSTRUCTURE";		

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

		SaltForm saltForm = SaltForm.findSaltForm(1L);
		logger.info("query saltform: " + saltForm.toJson());
		List<Lot> saltLots = Lot.findLotsBySaltForm(saltForm).getResultList();
		logger.debug("Number of salt lots: " + saltLots.size());
		
		List<Lot> lots = Lot.findLotsBySaltFormAndMeta(saltForm, searchParams).getResultList();
		logger.debug("Number of saltForm-meta lots: " + lots.size());

		for (Lot lot : lots){
			logger.debug("Lot: " + lot.toJson());
		}

	}
}
