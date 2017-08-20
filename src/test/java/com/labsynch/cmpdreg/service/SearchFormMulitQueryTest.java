package com.labsynch.cmpdreg.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.chemclasses.CmpdRegMolecule;
import com.labsynch.cmpdreg.domain.CorpName;
import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.dto.SearchCompoundReturnDTO;
import com.labsynch.cmpdreg.dto.SearchFormDTO;
import com.labsynch.cmpdreg.dto.SearchFormReturnDTO;
import com.labsynch.cmpdreg.dto.SearchLotDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
@Transactional
public class SearchFormMulitQueryTest {

	private static final Logger logger = LoggerFactory.getLogger(SearchFormMulitQueryTest.class);

	@Autowired
	private ChemStructureService structureService;

	@Autowired
	private SearchFormService searchFormService;
	

	//@Test
	public void simpleDateSearch() throws ParseException{
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date beforeDate = df.parse("01/01/1980");    	
		Date minSynthesisDate = beforeDate;

		List<Lot> lots = Lot.findLotsByDate(minSynthesisDate).getResultList();
		logger.debug("number of lots found: " + lots.size());
		logger.debug("lot results: " + Lot.toJsonArray(lots));

	}

	//@Test
	public void basicLotSearch() throws ParseException {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date beforeDate = df.parse("01/01/1980");
		Date todayDate = df.parse("11/18/2015");

		Date minSynthesisDate = beforeDate;
		Date maxSynthesisDate = todayDate;
		Scientist chemist = null;

		//		Scientist chemist = Scientist.findScientistsByCodeEquals("cchemist").getSingleResult();

		List<Lot> lots = Lot.findLotsByMetaQuery(chemist, minSynthesisDate, maxSynthesisDate).getResultList();
		logger.debug("number of lots found: " + lots.size());

		logger.debug("lot results: " + Lot.toJsonArray(lots));

	}   

	//@Test
	public void basicParentSearch() throws ParseException {
		
		SearchFormDTO searchParams = new SearchFormDTO();

		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date beforeDate = df.parse("01/01/1980");
		Date todayDate = df.parse("11/18/2015");

		Date minSynthesisDate = beforeDate;
		//		Date maxSynthesisDate = todayDate;
		//     	String lotCorpName = "CMPD-0465-0-01";
		//     	String saltFormCorpName = "CMPD-0465-0";
		//     	String parentCorpName = "CMPD-0465";

		Date maxSynthesisDate = null;
		String saltFormCorpName = null;
		String parentCorpName = null;
		String lotCorpName = null;

		searchParams.setMinSynthDate(df.parse(minSynthesisDate.toString()));
		
		Date checkDate = searchParams.getMaxSynthDate();

//		
//		List<Integer> parentCdIds = Parent.findParentCdIdsByMetaQuery(null, minSynthesisDate, null, maxSynthesisDate, maxSynthesisDate, 0, 0, parentCorpName, saltFormCorpName, lotCorpName);
//
//		if (parentCdIds != null){
//			logger.debug("number of parents found: " + parentCdIds.size());
//			logger.debug("parent results: " + parentCdIds);
//			for (int cdId : parentCdIds){
//				logger.debug("Parent CdId: " + cdId);
//			}			
//		}
//
//		//next: feed the list of the cdIds into the structure search
//		//		parentStructureHits = searchMols(String molfile, String structureTable, int[] inputCdIdHitList, String plainTable, String searchType, float simlarityPercent)
//
//		String molfile = "C1=CC=CC=C1";
//		String structureTable = "Parent_Structure";
//		int[] inputCdIdHitList = ArrayUtils.toPrimitive(parentCdIds.toArray(new Integer[parentCdIds.size()]));
//		String plainTable = null;
//		String searchType = "SUBSTRUCTURE"; 
//		float simlarityPercent = 0f;
//
//		CmpdRegMolecule[] parentStructureHits = structureService.searchMols( molfile,  structureTable, inputCdIdHitList, plainTable, searchType, simlarityPercent);
//		logger.debug("number of mol hits := " + parentStructureHits.length);
//		
//		for (CmpdRegMolecule hitMol : parentStructureHits){
//			logger.debug("Parent mol cdId := " + hitMol.getProperty("cd_id"));
//		}
//
//
//		CmpdRegMolecule[] saltFormStructureHits = structureService.searchMols( molfile,  "SaltForm_Structure", null, "Salt_Form", searchType, simlarityPercent);
//		logger.debug("number of saltForm mol hits := " + saltFormStructureHits.length);
//		
//		for (CmpdRegMolecule hitMol : saltFormStructureHits){
//			logger.debug("SaltForm mol cdId := " + hitMol.getProperty("cd_id"));
//			int CdId = Integer.parseInt( hitMol.getProperty("cd_id"));
//			SaltForm saltForm = SaltForm.findSaltFormsByCdId(CdId).getSingleResult();
//			Parent parent = saltForm.getParent();
//		}
//
//		Set<SearchCompoundReturnDTO> foundCompounds = new HashSet<SearchCompoundReturnDTO>();

		
		// if I know the parent CdIds -- can I get the saltForm cdIds as well?
		// or do I simply return the full parent object at this time? 
		// then I need a helper method to build up my int[] of parent cd_ids
		// go back to full parent objects for now -- worry about performance issues later
		
		// then we can get the saltforms by parent
		// but only need the ones with cd_id
		// confusing - just do a simple structure search on the salt form now
		
		
		// What is the exact desired behavior for saltForm Structure search?
		// search the search form structure table
		// if there is a structure hit, then display the parent and return the lots.
		// continue to search the parent structure table as well.
		// merge the results of the two structure searches.
		// basically, the saltForm structure search could add additional parent CdIds to the list

	}

	
	//@Test
	public void fullSaltFormSearch() throws ParseException {
		
		SearchFormDTO searchParams = new SearchFormDTO();
		
		String alias = null;
		String aliasContSelect = null;
		Scientist chemist = null;
		String corpNameFrom = "1";
		String corpNameTo = "305";
		String dateFrom = "01/01/1980";
		String dateTo = "11/18/2015";
		String lotCorpName = null;
		Long maxParentNumber = null;
		Long minParentNumber = null;
		String molStructure = "C1=CC=CC=C1";
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

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		if (!searchParams.getDateFrom().equals("")){
			searchParams.setMinSynthDate(df.parse(searchParams.getDateFrom()));
		}
		if (!searchParams.getDateTo().equals("")){
			searchParams.setMaxSynthDate(df.parse(searchParams.getDateTo()));
		}
		
		

		
		List<SaltForm> saltForms = new ArrayList<SaltForm>();
		List<SearchCompoundReturnDTO> foundCompounds = new ArrayList<SearchCompoundReturnDTO>();
		HashMap<String, SaltForm> saltFormMols = new HashMap<String, SaltForm>(); 
		HashMap<String, SaltForm> parentMols = new HashMap<String, SaltForm>(); 


		// 1. Check if corpNameFrom and corpNameTo are both set  -- will search for a range of parents if both set
		//    Check if single corpName is submitted. Convert corpName to either parent, saltForm, or Lot name

		if (!searchParams.getCorpNameFrom().equals("") && !searchParams.getCorpNameTo().equals("")){
			logger.debug("parentFrom: check corpName " + searchParams.getCorpNameFrom());

			if (CorpName.checkParentCorpName(searchParams.getCorpNameFrom()) || CorpName.checkCorpNumber(searchParams.getCorpNameFrom())){
				Long minParent = null;
				if (CorpName.checkCorpNumber(searchParams.getCorpNameFrom())){
					minParent = Long.parseLong(searchParams.getCorpNameFrom().trim());
				} else {
					minParent = CorpName.convertCorpNameToNumber(searchParams.getCorpNameFrom());					
				}
				logger.debug("parentFrom: " + minParent);
				searchParams.setMinParentNumber(minParent);
			}
			
			if (CorpName.checkParentCorpName(searchParams.getCorpNameTo()) || CorpName.checkCorpNumber(searchParams.getCorpNameTo())){
				Long maxParent = null;
				if (CorpName.checkCorpNumber(searchParams.getCorpNameTo())){
					maxParent = Long.parseLong(searchParams.getCorpNameTo().trim());
				} else {
					maxParent = CorpName.convertCorpNameToNumber(searchParams.getCorpNameTo());					
				}
				logger.debug("parentTo: " + maxParent);	
				searchParams.setMaxParentNumber(maxParent);
			}
			
		} else if (!searchParams.getCorpNameFrom().equals("") && searchParams.getCorpNameTo().equals("")){
			String corpName = searchParams.getCorpNameFrom();
			if (CorpName.checkParentCorpName(corpName)){
				searchParams.setParentCorpName(corpName);
			} else if (CorpName.checkSaltFormCorpName(corpName)){
				searchParams.setSaltFormCorpName(corpName);
			} else if (CorpName.checkLotCorpName(corpName)){
				searchParams.setLotCorpName(corpName);
			} else if (CorpName.checkCorpNumber(corpName)){
				corpName = CorpName.convertCorpNameNumber(corpName);
				searchParams.setLotCorpName(corpName);
			}
			logger.debug("Converted corpName = " + corpName);
		}
		
		
		// 2. All other meta params will flow into the searches
		// if no search structure, we will only search with the meta data
		
		if (searchParams.getMolStructure() == null || searchParams.getMolStructure().equals("")){
			
			//
			List<SaltForm> saltFormsFound = SaltForm.findSaltFormsByMeta(searchParams).getResultList();
			//
			logger.debug("Number of saltForms found:  " + saltFormsFound.size());			
			logger.debug("search params: ---- " + searchParams.toJson());
			
			for (SaltForm saltForm : saltFormsFound){
				SearchCompoundReturnDTO searchCompound = new SearchCompoundReturnDTO();
				searchCompound.setCorpName(saltForm.getCorpName());
				if (saltForm.getCdId() == 0){
					searchCompound.setMolStructure(saltForm.getParent().getMolStructure());
				} else {
					searchCompound.setMolStructure(saltForm.getMolStructure());
				}
				List<Lot> lots = Lot.findLotsBySaltFormAndMeta(saltForm, searchParams).getResultList();
				for (Lot lot : lots){
					SearchLotDTO searchLot = new SearchLotDTO();
					searchLot.setCorpName(lot.getCorpName());
					searchLot.setLotNumber(lot.getLotNumber());
					searchLot.setRegistrationDate(lot.getRegistrationDate());
					searchLot.setSynthesisDate(lot.getSynthesisDate());
					searchCompound.getLotIDs().add(searchLot);
				}				
				foundCompounds.add(searchCompound);	
			}
			
			logger.debug("found Compounds: " + SearchCompoundReturnDTO.toJsonArray(foundCompounds));
			
		} else {
			//do the structure search --> saltForm followed by parent structures
			//get saltFormCdIds to search on
			//get parentCdIds to search on
			//the mol search will return the mols in search order
			//loop through the set of found parent mols first (larger set)
			//then loop through the results of the saltForm structure search

			int[] filterCdIds = null;
			String structureTable = null;
			String plainTable = null;	
			
			List<Integer> saltFormCdIds = SaltForm.findSaltFormCdIdsByMeta(searchParams);
			filterCdIds = ArrayUtils.toPrimitive(saltFormCdIds.toArray(new Integer[saltFormCdIds.size()]));
			structureTable = "SaltForm_Structure";
			CmpdRegMolecule[] saltFormStructureHits = structureService.searchMols( searchParams.getMolStructure(), structureTable,filterCdIds, plainTable, 
																			searchParams.getSearchType(), searchParams.getPercentSimilarity());

			logger.debug("found: " + saltFormStructureHits.length + " saltForm structure hits");
			
			for (CmpdRegMolecule hitMol : saltFormStructureHits){
				int hitCdId = Integer.parseInt(hitMol.getProperty("cd_id").trim());
				SaltForm saltForm = SaltForm.findSaltFormsByCdId(hitCdId).getSingleResult();
				saltForm.setMolStructure(hitMol.getMrvStructure());
				saltFormMols.put(saltForm.getCorpName(), saltForm);				
			}
			
			List<Integer> parentCdIds = Parent.findParentCdIdsByMeta(searchParams);
			filterCdIds = ArrayUtils.toPrimitive(parentCdIds.toArray(new Integer[parentCdIds.size()]));;
			structureTable = "Parent_Structure";
			CmpdRegMolecule[] parentStructureHits = structureService.searchMols( searchParams.getMolStructure(),  structureTable, filterCdIds, plainTable, 
																			searchParams.getSearchType(), searchParams.getPercentSimilarity());

			logger.debug("found: " + parentStructureHits.length + " parent structure hits");
			
			for (CmpdRegMolecule hitMol : parentStructureHits){
				int hitCdId = Integer.parseInt(hitMol.getProperty("cd_id").trim());
				Parent parent = Parent.findParentsByCdId(hitCdId).getSingleResult();
				saltForms = SaltForm.findSaltFormsByParentAndMeta(parent, searchParams).getResultList();
				for (SaltForm saltForm : saltForms){
					parentMols.put(saltForm.getCorpName(), saltForm);
					SearchCompoundReturnDTO searchCompound = new SearchCompoundReturnDTO();
					searchCompound.setCorpName(saltForm.getCorpName());
					if (saltForm.getParent().getStereoCategory() != null){
						if (saltForm.getParent().getStereoCategory().getCode() != null){
							searchCompound.setStereoCategoryName(saltForm.getParent().getStereoCategory().getCode());													
						}
					}
					if (saltForm.getParent().getStereoComment() != null){
						searchCompound.setStereoComment(saltForm.getParent().getStereoComment());						
					}
					if(saltFormMols.containsKey(saltForm.getCorpName())){
						searchCompound.setMolStructure(saltFormMols.get(saltForm.getCorpName()).getMolStructure());
					} else {
						searchCompound.setMolStructure(hitMol.getMrvStructure());
					}
					List<Lot> lots = Lot.findLotsBySaltFormAndMeta(saltForm, searchParams).getResultList();
					for (Lot lot : lots){
						SearchLotDTO searchLot = new SearchLotDTO();
						searchLot.setCorpName(lot.getCorpName());
						searchLot.setLotNumber(lot.getLotNumber());
						searchLot.setRegistrationDate(lot.getRegistrationDate());
						searchLot.setSynthesisDate(lot.getSynthesisDate());
						searchCompound.getLotIDs().add(searchLot);
					}				
					foundCompounds.add(searchCompound);									
				}				
			}
			
			//now add in any hits from the saltForm query that were not picked up in the parent query
			
			for (Integer cdId : saltFormCdIds){
				SaltForm saltForm = SaltForm.findSaltFormsByCdId(cdId).getSingleResult();
				if (!parentMols.containsKey(saltForm.getCorpName())){
					SearchCompoundReturnDTO searchCompound = new SearchCompoundReturnDTO();
					searchCompound.setCorpName(saltForm.getCorpName());
					searchCompound.setStereoCategoryName(saltForm.getParent().getStereoCategory().getCode());
					searchCompound.setStereoComment(saltForm.getParent().getStereoComment());
					searchCompound.setMolStructure(saltFormMols.get(saltForm.getCorpName()).getMolStructure());
					List<Lot> lots = Lot.findLotsBySaltFormAndMeta(saltForm, searchParams).getResultList();
					for (Lot lot : lots){
						SearchLotDTO searchLot = new SearchLotDTO();
						searchLot.setCorpName(lot.getCorpName());
						searchLot.setLotNumber(lot.getLotNumber());
						searchLot.setRegistrationDate(lot.getRegistrationDate());
						searchLot.setSynthesisDate(lot.getSynthesisDate());
						searchCompound.getLotIDs().add(searchLot);
					}				
					foundCompounds.add(searchCompound);									
				}
				
			}
			
			logger.debug("found Compounds: " + SearchCompoundReturnDTO.toJsonArray(foundCompounds));

			
		}
		
	}
	
	//@Test
	public void basicParentSearch2() throws ParseException {
		
		SearchFormDTO searchParams = new SearchFormDTO();

		String corpName = "CMPD-0101";
		searchParams.setMinParentNumber(1L);
		searchParams.setMaxParentNumber(10L);

//		searchParams.setParentCorpName(corpName);		
//		List<Parent> parents = Parent.findParentsByCorpNameEquals(corpName).getResultList();
//		logger.debug("number of parents found: " + parents.size());

		
		List<SaltForm> saltForms = SaltForm.findSaltFormsByMeta(searchParams).getResultList();
		
		List<Integer> cdIds = SaltForm.findSaltFormCdIdsByMeta(searchParams);
		
		logger.debug("number of salt forms found: " + saltForms.size());
		logger.debug("number of cdIds found: " + cdIds.toString());

		logger.debug(searchParams.toJson());
		
	}
		
	//@Test
	public void searchStructureTest() throws ParseException {
		
		SearchFormDTO searchParams = new SearchFormDTO();
		
		String alias = null;
		String aliasContSelect = null;
		Scientist chemist = null;
		String corpNameFrom = "";
		String corpNameTo = "";
		String dateFrom = "11/27/2011";
		String dateTo = "";
		String lotCorpName = null;
		Long maxParentNumber = null;
		Long minParentNumber = null;
		String molStructure = "C1CCCC1";
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
		
		SearchFormReturnDTO results = searchFormService.findQuerySaltForms(searchParams);
		
		logger.debug("number of salt forms found: " + results.getFoundCompounds().size());
		
		List<Lot> searchLots = Lot.findLotsBySynthesisDateGreaterThan(searchParams.getMinSynthDate()).getResultList();
		logger.debug("number of lots found: " + searchLots.size());
	
		
		logger.debug(searchParams.toJson());
		
	}
	
	
	@Test
	public void searchParentAliasTest() throws ParseException {
		
		SearchFormDTO searchParams = new SearchFormDTO();
		
		String alias = "r";
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
				
		SearchFormReturnDTO results = searchFormService.findQuerySaltForms(searchParams);
		logger.debug(results.toString());
		logger.debug("number of salt forms found: " + results.getFoundCompounds().size());
				
		logger.debug(searchParams.toJson());
		
	}
}
