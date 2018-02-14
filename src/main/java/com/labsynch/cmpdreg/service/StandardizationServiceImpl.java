package com.labsynch.cmpdreg.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFWriterFactory;
import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.ParentAlias;
import com.labsynch.cmpdreg.domain.StandardizationDryRunCompound;
import com.labsynch.cmpdreg.domain.StandardizationHistory;
import com.labsynch.cmpdreg.domain.StandardizationSettings;
import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.dto.configuration.StandardizerSettingsConfigDTO;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;
import com.labsynch.cmpdreg.exceptions.StandardizerException;
import com.labsynch.cmpdreg.utils.Configuration;

@Service
public class StandardizationServiceImpl implements StandardizationService {

	Logger logger = LoggerFactory.getLogger(StandardizationServiceImpl.class);
	public static final MainConfigDTO mainConfig = Configuration.getConfigInfo();
	private static final StandardizerSettingsConfigDTO standardizerConfigs = Configuration.getConfigInfo().getStandardizerSettings();
	private static final boolean shouldStandardize = Configuration.getConfigInfo().getStandardizerSettings().getShouldStandardize();
    private static final String standardizerType = Configuration.getConfigInfo().getStandardizerSettings().getType();


	@Autowired
	public ChemStructureService chemStructureService;

	@Autowired
	public LDStandardizerService ldStandardizerService;
	
	@Autowired
	public ParentLotService parentLotService;

	@Autowired
	public ParentStructureService parentStructureService;

	@Autowired
	public ParentAliasService parentAliasService;
	
	@Autowired
	CmpdRegSDFWriterFactory cmpdRegSDFWriterFactory;
	
	@Autowired
	CmpdRegMoleculeFactory cmpdRegMoleculeFactory;

	@Transactional
	@Override
	public int populateStanardizationDryRunTable() throws CmpdRegMolFormatException, IOException, StandardizerException{
		List<Long> parentIds = Parent.getParentIds();
		Parent parent;
		StandardizationDryRunCompound stndznCompound;
		int nonMatchingCmpds = 0;
		logger.info("number of parents to check: " + parentIds.size());
		Date qcDate = new Date();
		String asDrawnStruct;
		Integer cdId = 0 ;
		List<Lot> queryLots;
		Integer runNumber = StandardizationDryRunCompound.findMaxRunNumber().getSingleResult();
		if (runNumber == null){
			runNumber = 1;
		} else {
			runNumber++;
		}

		for  (Long parentId : parentIds){
			parent = Parent.findParent(parentId);
			stndznCompound = new StandardizationDryRunCompound();
			stndznCompound.setRunNumber(runNumber);
			stndznCompound.setQcDate(qcDate);
			stndznCompound.setParentId(parent.getId());
			stndznCompound.setCorpName(parent.getCorpName());
			stndznCompound.setAlias(getParentAlias(parent));
			stndznCompound.setStereoCategory(parent.getStereoCategory().getName());
			stndznCompound.setStereoComment(parent.getStereoComment());
			stndznCompound.setOldMolWeight(parent.getMolWeight());
			
			queryLots = Lot.findLotByParentAndLowestLotNumber(parent).getResultList();
			if (queryLots.size() != 1) logger.error("!!!!!!!!!!!!  odd lot number size   !!!!!!!!!  " + queryLots.size() + "  saltForm: " + parent.getId());
			if (queryLots.size() > 0 && queryLots.get(0).getAsDrawnStruct() != null){
				asDrawnStruct = queryLots.get(0).getAsDrawnStruct();
			} else {
				asDrawnStruct = parent.getMolStructure();
			}
			logger.debug("attempting to standardize: " + parent.getCorpName() + "   " + asDrawnStruct);
			stndznCompound.setMolStructure(chemStructureService.standardizeStructure(asDrawnStruct));				
			stndznCompound.setNewMolWeight(chemStructureService.getMolWeight(stndznCompound.getMolStructure()));
			
			if(parent.getMolWeight() == 0 && stndznCompound.getNewMolWeight() == 0) {
				logger.info("mol weight 0 before and after standardization - skipping");

			} else {
				boolean displayTheSame = chemStructureService.isIdenticalDisplay(parent.getMolStructure(), stndznCompound.getMolStructure());
				if (!displayTheSame){
					stndznCompound.setDisplayChange(true);
					logger.info("the compounds are NOT matching: " + parent.getCorpName());
					nonMatchingCmpds++;
				}
				boolean asDrawnDisplaySame = chemStructureService.isIdenticalDisplay(asDrawnStruct, stndznCompound.getMolStructure());
				if (!asDrawnDisplaySame){
					stndznCompound.setAsDrawnDisplayChange(true);
					logger.info("the compounds are NOT matching: " + parent.getCorpName());
					nonMatchingCmpds++;
				}
			}
			logger.debug("time to save the struture");
			cdId = chemStructureService.saveStructure(stndznCompound.getMolStructure(), "standardization_dry_run_structure", false);
			if (cdId == -1){
				logger.error("Bad molformat. Please fix the molfile: " + stndznCompound.getMolStructure());
			} else {
				logger.debug("here is the cdId: " + cdId);
				stndznCompound.setCdId(cdId);
				stndznCompound.persist();
			}

		}
		logger.info("total number of nonMatching compounds: " + nonMatchingCmpds);
		return (nonMatchingCmpds);
	}

	@Transactional
	private String getParentAlias(Parent parent) {
		StringBuilder aliasSB = new StringBuilder();
		Set<ParentAlias> parentAliases = parent.getParentAliases();
		boolean firstAlias = true;
		for (ParentAlias parentAlias : parentAliases){
			if (firstAlias){
				aliasSB.append(parentAlias.getAliasName());
				firstAlias = false;
			} else {
				aliasSB.append(";").append(parentAlias.getAliasName());
			}
		}
		return aliasSB.toString();
	}


	@Override
	public int dupeCheckStandardizationStructures() throws CmpdRegMolFormatException{
		List<Long> qcIds = StandardizationDryRunCompound.findAllIds().getResultList();
		logger.info("number of qcCompounds found: " + qcIds.size());
		int totalNewDupeCount = 0;
		int totalOldDupeCount = 0;
		if (qcIds.size() > 0){
			int[] hits;	
			StandardizationDryRunCompound qcCompound;
			String newDupeCorpNames = "";
			String oldDupeCorpNames = "";
			int newDupeCount = 0;
			int oldDupeCount = 0;
			for (Long qcId : qcIds){
				boolean firstNewDupeHit = true;
				boolean firstOldDupeHit = true;
				qcCompound = StandardizationDryRunCompound.findStandardizationDryRunCompound(qcId);
				logger.debug("query compound: " + qcCompound.getCorpName());
				if(qcCompound.getNewMolWeight() == 0) {
					logger.info("mol has a weight of 0 - skipping");			

				} else {
					hits = chemStructureService.searchMolStructures(qcCompound.getMolStructure(), "standardization_dry_run_structure", "DUPLICATE_TAUTOMER");
					newDupeCount = hits.length;
					logger.debug("current new dupeCount: " + newDupeCount);
					qcCompound.setChangedStructure(true);
					for (int hit:hits){
						List<StandardizationDryRunCompound> searchResults = StandardizationDryRunCompound.findStandardizationDryRunCompoundsByCdId(hit).getResultList();
						for (StandardizationDryRunCompound searchResult : searchResults){
							if (searchResult.getCorpName().equalsIgnoreCase(qcCompound.getCorpName())){
								qcCompound.setChangedStructure(false);
								newDupeCount = newDupeCount-1;
							} else {
								if (StringUtils.equals(searchResult.getStereoCategory(), qcCompound.getStereoCategory())
										&& StringUtils.equalsIgnoreCase(searchResult.getStereoComment(), qcCompound.getStereoComment())){
									if (!firstNewDupeHit) newDupeCorpNames = newDupeCorpNames.concat(";");
									newDupeCorpNames = newDupeCorpNames.concat(searchResult.getCorpName());
									firstNewDupeHit = false;
									logger.info("found new dupe parents");
									logger.info("query: " + qcCompound.getCorpName() + "     dupe: " + searchResult.getCorpName());
									totalNewDupeCount++;
								} else {
									logger.info("found different stereo codes and comments");
								}
							}
						}
					}
					
					hits = chemStructureService.searchMolStructures(qcCompound.getMolStructure(), "parent_structure", "DUPLICATE_TAUTOMER");
					oldDupeCount = hits.length;
					logger.debug("current old dupeCount: " + oldDupeCount);
					for (int hit:hits){
						List<Parent> searchResults = Parent.findParentsByCdId(hit).getResultList();
						for (Parent searchResult : searchResults){
							if (searchResult.getCorpName().equalsIgnoreCase(qcCompound.getCorpName())){
								oldDupeCount = oldDupeCount-1;
							} else {
								if (StringUtils.equals(searchResult.getStereoCategory().getName(), qcCompound.getStereoCategory())
										&& StringUtils.equalsIgnoreCase(searchResult.getStereoComment(), qcCompound.getStereoComment())){
									if (!firstOldDupeHit) oldDupeCorpNames = oldDupeCorpNames.concat(";");
									oldDupeCorpNames = oldDupeCorpNames.concat(searchResult.getCorpName());
									firstOldDupeHit = false;
									logger.info("found old dupe parents");
									logger.info("query: " + qcCompound.getCorpName() + "     dupe: " + searchResult.getCorpName());
									totalOldDupeCount++;
								} else {
									logger.info("found different stereo codes and comments");
								}
							}
						}
					}
					qcCompound.setNewDupeCount(newDupeCount);
					if(!newDupeCorpNames.equals("")) {
						qcCompound.setNewDuplicates(newDupeCorpNames);
					}
					qcCompound.setOldDupeCount(oldDupeCount);
					if(!oldDupeCorpNames.equals("")) {
						qcCompound.setOldDuplicates(oldDupeCorpNames);
					}	

				}

				qcCompound.merge();
				newDupeCorpNames = "";
				oldDupeCorpNames = "";
			}
		}
		return (totalNewDupeCount);
	}
	
	@Override
	public String standardizeSingleMol(String mol) throws CmpdRegMolFormatException, StandardizerException, IOException {
		String result = "";
		if(shouldStandardize) {
			switch (standardizerType) {
			case "livedesign":
				result = ldStandardizerService.standardizeStructure(mol);
				break;
			case "jchem":
				result = chemStructureService.standardizeStructure(mol);
				break;
			}
		} else {
			result = mol;
		}

		return(result);
	}
	
	@Override
	public String getStandardizationDryRunReport() throws StandardizerException, CmpdRegMolFormatException, IOException{
		List<StandardizationDryRunCompound> stndznCompounds = StandardizationDryRunCompound.findStandardizationChanges().getResultList();
		String json = StandardizationDryRunCompound.toJsonArray(stndznCompounds);
		return(json);
	}
	
	@Override
	public void reset() {
		boolean dropTable = chemStructureService.dropJChemTable("compound.standardization_dry_run_structure");
		logger.info("drop table is: " + dropTable);
		if (dropTable){
			chemStructureService.createJChemTable("compound.standardization_dry_run_structure", true);				
			StandardizationDryRunCompound.truncateTable();
		} else {
			logger.info("unable to drop jchem table");
		}
	}
	
	@Override
	public int restandardizeParentStructures(List<Long> parentIds) throws CmpdRegMolFormatException, StandardizerException, IOException {
		if(shouldStandardize) {
			Parent parent;
			List<Lot> lots;
			Lot lot;
			String originalStructure = null;
			String result;
			logger.info("number of parents to restandardize: " + parentIds.size());
			for  (Long parentId : parentIds){
				parent = Parent.findParent(parentId);
				lots = Lot.findLotsByParent(parent).getResultList();
				lot = lots.get(0);
				if (lots.size() > 0 && lot.getAsDrawnStruct() != null){
					originalStructure = lot.getAsDrawnStruct();				
				} else {
					logger.warn("Did not find the asDrawnStruct for parent: " + parentId + "  " + parent.getCorpName());
					originalStructure = parent.getMolStructure();
				}
				
				switch (standardizerType) {
				case "livedesign":
					result = ldStandardizerService.standardizeStructure(originalStructure);
					parent.setMolStructure(result);
					parent = parentStructureService.update(parent);
					break;
				case "jchem":
					result = chemStructureService.standardizeStructure(originalStructure);
					parent.setMolStructure(result);
					parent = parentStructureService.update(parent);
					break;
				}
			}
			return parentIds.size();
		} else {
			return 0;
		}

	}

	@Override
	public String executeStandardization() {
		StandardizationHistory standardizationHistory = getMostRecentStandardizationHistory();
		if(StringUtils.equalsIgnoreCase(standardizationHistory.getStandardizationStatus(), "complete")) {
			standardizationHistory = new StandardizationHistory();
			standardizationHistory.setSettings(standardizerConfigs.toJson());
			standardizationHistory.setSettingsHash(standardizerConfigs.hashCode());
			standardizationHistory.setRecordedDate(new Date());			
		}
		standardizationHistory.setStandardizationStart(new Date());
		standardizationHistory.setStandardizationComplete(null);
		standardizationHistory.setStandardizationStatus("running");
		standardizationHistory.persist();
		
		Integer result;
		try {
			result = this.runStandardization();
		} catch (CmpdRegMolFormatException | IOException | StandardizerException e) {
			standardizationHistory.setStandardizationComplete(new Date());
			standardizationHistory.setStandardizationStatus("failed");
			standardizationHistory.persist();
			return(standardizationHistory.toJson());
		}
		
		StandardizationSettings stndardizationSettings = this.getStandardizationSettings();
		stndardizationSettings.setNeedsStandardization(false);
		stndardizationSettings.persist();
		
		standardizationHistory = StandardizationDryRunCompound.addStatsToHistory(standardizationHistory);
		standardizationHistory.setStructuresStandardizedCount(result);
		standardizationHistory.setStandardizationComplete(new Date());
		standardizationHistory.setStandardizationStatus("complete");
		standardizationHistory.persist();
		return standardizationHistory.toJson();
	}
	
	@Transactional
	private int runStandardization() throws CmpdRegMolFormatException, IOException, StandardizerException {
		List<Long> parentIds = StandardizationDryRunCompound.findParentIdsWithStandardizationChanges().getResultList();
		int result = restandardizeParentStructures(parentIds);
		this.reset();
		return(result);
	}
	
	@Override
	public String getDryRunStats() {
		String dryRunStats = StandardizationDryRunCompound.fetchStats().toString();
		return dryRunStats;
	}
	
	@Override
	public StandardizationSettings getStandardizationSettings() {
		List<StandardizationSettings> standardizationSettingses = StandardizationSettings.findAllStandardizationSettingses();
		StandardizationSettings standardizationSettings = new StandardizationSettings();
		if(standardizationSettingses.size() > 0) {
			standardizationSettings = standardizationSettingses.get(0);
		}
		return(standardizationSettings);
	}

	@Override
	public List<StandardizationHistory> getStanardizationHistory() {
		List<StandardizationHistory> standardizationSettingses = StandardizationHistory.findStandardizationHistoryEntries(0, 500, "dateOfStandardization", "DESC");
		return standardizationSettingses;
	}

	@Override
	public void executeDryRun() throws CmpdRegMolFormatException, IOException, StandardizerException {
		StandardizationHistory stndznHistory = getMostRecentStandardizationHistory();
		if(StringUtils.equalsIgnoreCase(stndznHistory.getStandardizationStatus(), "complete")) {
			stndznHistory = new StandardizationHistory();
			stndznHistory.setSettings(standardizerConfigs.toJson());
			stndznHistory.setSettingsHash(standardizerConfigs.hashCode());
			stndznHistory.setRecordedDate(new Date());			
		}
		stndznHistory.setDryRunStatus("running");
		stndznHistory.setDryRunStart(new Date());
		stndznHistory.setDryRunComplete(null);
		stndznHistory.persist();
		
	    int numberOfDisplayChanges = this.runDryRun();
	    
		stndznHistory.setDryRunComplete(new Date());
		stndznHistory.setDryRunStatus("complete");
		stndznHistory.persist();
	}
	
	@Transactional
	private int runDryRun() throws CmpdRegMolFormatException, IOException, StandardizerException {
		this.reset();
		int numberOfDisplayChanges = this.populateStanardizationDryRunTable();
	    numberOfDisplayChanges = this.dupeCheckStandardizationStructures();
	    return(numberOfDisplayChanges);
	}
	
	
	public StandardizationHistory getMostRecentStandardizationHistory() {
		List<StandardizationHistory> standardizationSettingses = StandardizationHistory.findStandardizationHistoryEntries(0, 1, "id", "DESC");
		return(standardizationSettingses.get(0));
	}


}

