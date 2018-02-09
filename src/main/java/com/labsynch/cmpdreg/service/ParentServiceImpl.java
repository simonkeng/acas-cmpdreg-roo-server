package com.labsynch.cmpdreg.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFWriterFactory;
import com.labsynch.cmpdreg.domain.CompoundType;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.ParentAlias;
import com.labsynch.cmpdreg.domain.ParentAnnotation;
import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.domain.StereoCategory;
import com.labsynch.cmpdreg.dto.CodeTableDTO;
import com.labsynch.cmpdreg.dto.ParentAliasDTO;
import com.labsynch.cmpdreg.dto.ParentDTO;
import com.labsynch.cmpdreg.dto.ParentEditDTO;
import com.labsynch.cmpdreg.dto.ParentValidationDTO;
import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;
import com.labsynch.cmpdreg.utils.Configuration;



@Service
public class ParentServiceImpl implements ParentService {

	Logger logger = LoggerFactory.getLogger(ParentServiceImpl.class);

	public static final MainConfigDTO mainConfig = Configuration.getConfigInfo();

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
	public CmpdRegSDFWriterFactory cmpdRegSDFWriterFactory;
	
	@Autowired
	public CmpdRegMoleculeFactory cmpdRegMoleculeFactory;

	@Override
	@Transactional
	public ParentValidationDTO validateUniqueParent(Parent queryParent) throws CmpdRegMolFormatException {
		ParentValidationDTO validationDTO = new ParentValidationDTO();

		if (queryParent.getCorpName() == null) validationDTO.getErrors().add(new ErrorMessage("error","Must provide corpName for parent to be validated"));
		if (queryParent.getStereoCategory() == null) validationDTO.getErrors().add(new ErrorMessage("error","Must provide stereo category for parent to be validated"));
		if (queryParent.getStereoCategory().getCode().equalsIgnoreCase("see_comments") && (queryParent.getStereoComment() == null || queryParent.getStereoComment().length()==0)){
			validationDTO.getErrors().add(new ErrorMessage("error","Stereo category is See Comments, but no stereo comment provided"));
		}
		if (chemStructureService.checkForSalt(queryParent.getMolStructure())){
			if (queryParent.getIsMixture() != null){
				if (!queryParent.getIsMixture()){
					validationDTO.getErrors().add(new ErrorMessage("error","Multiple fragments found. Please register the neutral base parent or mark as a Mixture"));
				}
			}else{
				validationDTO.getErrors().add(new ErrorMessage("error","Multiple fragments found. Please register the neutral base parent or mark as a Mixture"));
			}
		}
		if (!validationDTO.getErrors().isEmpty()){
			for (ErrorMessage error : validationDTO.getErrors()){
				logger.error(error.getMessage());
			}
			return validationDTO;
		}
		Collection<ParentDTO> dupeParents = new HashSet<ParentDTO>();
		int[] dupeParentList = chemStructureService.checkDupeMol(queryParent.getMolStructure(), "Parent_Structure", "Parent");
		if (dupeParentList.length > 0){
			searchResultLoop:
				for (int foundParentCdId : dupeParentList){
					List<Parent> foundParents = Parent.findParentsByCdId(foundParentCdId).getResultList();
					for (Parent foundParent : foundParents){
						//same structure hits
						if (queryParent.getCorpName().equals(foundParent.getCorpName())){
							//corpName match => this is the parent we're searching on. ignore this match.
							continue;
						}else{
							//same structure, different corpName => check stereo category
							if(queryParent.getStereoCategory().getCode().equalsIgnoreCase(foundParent.getStereoCategory().getCode())){
								//same structure and stereo category => check stereo comment
								if (queryParent.getStereoComment() == null && foundParent.getStereoComment() == null){
									//both null - stereo comments match => this is a dupe
									ParentDTO foundDupeParentDTO = new ParentDTO();
									foundDupeParentDTO.setCorpName(foundParent.getCorpName());
									foundDupeParentDTO.setStereoCategory(foundParent.getStereoCategory());
									foundDupeParentDTO.setStereoComment(foundParent.getStereoComment());
									dupeParents.add(foundDupeParentDTO);
								}else if (queryParent.getStereoComment() == null || foundParent.getStereoComment() == null){
									//one null, the other is not => not a dupe
									continue;
								}else if (queryParent.getStereoComment().equalsIgnoreCase(foundParent.getStereoComment())){
									//same stereo comment => this is a dupe
									ParentDTO foundDupeParentDTO = new ParentDTO();
									foundDupeParentDTO.setCorpName(foundParent.getCorpName());
									foundDupeParentDTO.setStereoCategory(foundParent.getStereoCategory());
									foundDupeParentDTO.setStereoComment(foundParent.getStereoComment());
									dupeParents.add(foundDupeParentDTO);
								}else{
									//different stereo comment => not a dupe
									continue;
								}
							}else{
								//different stereo category => not a dupe
								continue;
							}
						}
					}
				}
		}
		if (!dupeParents.isEmpty()){
			validationDTO.setParentUnique(false);
			validationDTO.setDupeParents(dupeParents);
			return validationDTO;
		}else{
			validationDTO.setParentUnique(true);
			validationDTO.setAffectedLots(parentLotService.getCodeTableLotsByParentCorpName(queryParent.getCorpName()));
			return validationDTO;
		}
	}

	@Override
	public Collection<CodeTableDTO> updateParent(Parent parent){
		Set<ParentAlias> parentAliases = parent.getParentAliases();
		parent = parentStructureService.update(parent);
		//save parent aliases
		logger.info("--------- Number of parentAliases to save: " + parentAliases.size());
		parent = parentAliasService.updateParentAliases(parent, parentAliases);
		if (logger.isDebugEnabled()) logger.debug("Parent aliases after save: "+ ParentAlias.toJsonArray(parent.getParentAliases()));
		Collection<CodeTableDTO> affectedLots = parentLotService.getCodeTableLotsByParentCorpName(parent.getCorpName());
		return affectedLots;
	}
	
	@Override
	public String updateParentMetaArray(String jsonInput, String modifiedByUser){
		int parentCounter = 0;
		Collection<ParentEditDTO> parentDTOs = ParentEditDTO.fromJsonArrayToParentEditDTO(jsonInput);
		for (ParentEditDTO parentDTO : parentDTOs){
			updateParentMeta(parentDTO, modifiedByUser);
			parentCounter++;
		}
		String returnMessage = "Number of parents updated: " + parentCounter;

		return  returnMessage;
	}
		
	
	@Override
	public Parent updateParentMeta(ParentEditDTO parentDTO, String modifiedByUser){

		// NON-EDITABLE fields via this service
		//		private Set<SaltFormDTO> saltForms = new HashSet<SaltFormDTO>();
		//		private String molStructure;
		//		private Double exactMass;
		//		private String molFormula;
		//		private int CdId;
		//	    private Double molWeight;
		
		Scientist modifiedUser = Scientist.checkValidUser(modifiedByUser);		
		Parent parent = null;
		if (parentDTO.getId() != null){
			parent = Parent.findParent(parentDTO.getId());
		} else {
			List<Parent> parents = Parent.findParentsByCorpNameEquals(parentDTO.getCorpName()).getResultList();
			if (parents.size() == 0){
				String errorMessage = "ERROR: Did not find requested query parent: " + parentDTO.getCorpName();
				logger.error(errorMessage);
				throw new RuntimeException(errorMessage);
			} else if (parents.size() > 1){
				String errorMessage = "ERROR: found multiple parents for: " + parentDTO.getCorpName();		
				logger.error(errorMessage);		
				throw new RuntimeException(errorMessage);
			} else {
				parent = parents.get(0);
			}
		}

		if (parentDTO.getComment() != null && parentDTO.getComment().length() > 0) parent.setComment(parentDTO.getComment());
		if (parentDTO.getStereoCategoryCode() != null && parentDTO.getStereoCategoryCode().length() > 0){
			parent.setStereoCategory(StereoCategory.findStereoCategorysByCodeEquals(parentDTO.getStereoCategoryCode()).getSingleResult());
		}
		if (parentDTO.getStereoComment() != null && parentDTO.getStereoComment().length() > 0) parent.setStereoComment(parentDTO.getStereoComment());

		ParentValidationDTO parentValidationDTO = null;
		try {
			parentValidationDTO = validateUniqueParent(parent);
		} catch (CmpdRegMolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!parentValidationDTO.isParentUnique()){
			String errorMessage = "ERROR: Changing stereocategory and/or comments will create a duplicate parent";
			logger.error(errorMessage );
			throw new RuntimeException(errorMessage);
		} 
		
		
		if (parentDTO.getChemistCode() != null && parentDTO.getChemistCode().length() > 0){
			parent.setChemist(Scientist.findScientistsByCodeEquals(parentDTO.getChemistCode()).getSingleResult());
		}
		if (parentDTO.getCommonName() != null && parentDTO.getCommonName().length() > 0) parent.setCommonName(parentDTO.getCommonName());
		if (parentDTO.getIgnore() != null) parent.setIgnore(parentDTO.getIgnore());
		if (parentDTO.getIsMixture() != null) parent.setIsMixture(parentDTO.getIsMixture());
		if (parentDTO.getCompoundTypeCode() != null && parentDTO.getCompoundTypeCode().length() > 0){
			parent.setCompoundType(CompoundType.findCompoundTypesByCodeEquals(parentDTO.getCompoundTypeCode()).getSingleResult());
		}
		if (parentDTO.getParentAnnotationCode() != null && parentDTO.getParentAnnotationCode().length() > 0){
			parent.setParentAnnotation(ParentAnnotation.findParentAnnotationsByCodeEquals(parentDTO.getParentAnnotationCode()).getSingleResult());
		}
		
		if (parentDTO.getLiveDesignAliases() != null && parentDTO.getLiveDesignAliases().length() > 0){
			parent = parentAliasService.updateParentLiveDesignAlias(parent, parentDTO.getLiveDesignAliases());
		}

		if (parentDTO.getCommonNameAliases() != null && parentDTO.getCommonNameAliases().length() > 0){
			parent = parentAliasService.updateParentCommonNameAlias(parent, parentDTO.getCommonNameAliases());
		}
		
		if (parentDTO.getDefaultAliases() != null && parentDTO.getDefaultAliases().length() > 0){
			parent = parentAliasService.updateParentDefaultAlias(parent, parentDTO.getDefaultAliases());
		}

		
		if (parentDTO.getParentAliases() != null && !parentDTO.getParentAliases().isEmpty()){
			parent = updateParentAliasSet(parent, parentDTO);
		}
		

		if (parentValidationDTO.isParentUnique()){
			parent.setModifiedDate(new Date());
			parent.setModifiedBy(modifiedUser);
			parent.merge();
		}

		return parent;
	}


	private Parent updateParentAliasSet(Parent parent, ParentEditDTO parentDTO) {
		Set<ParentAliasDTO> parentDTOAliases = parentDTO.getParentAliases();
		Map<String, ParentAliasDTO> parentDTOAliasMAP = new HashMap<String, ParentAliasDTO>();
		for (ParentAliasDTO parentDTOAlias : parentDTOAliases){
			parentDTOAliasMAP.put(parentDTOAlias.getAliasName(), parentDTOAlias);
		}
		
		List<ParentAlias> parentAliases = ParentAlias.findParentAliasesByParent(parent).getResultList();
		Map<String, ParentAlias> parentAliasMAP = new HashMap<String, ParentAlias>();
		for (ParentAlias parentAlias : parentAliases){
			parentAliasMAP.put(parentAlias.getAliasName(), parentAlias);
		}
		
		if (parentAliases != null && !parentAliases.isEmpty()){
			for (ParentAlias parentAlias : parentAliases){
				if (!parentDTOAliasMAP.containsKey(parentAlias.getAliasName())){
					parentAlias.setIgnored(true);
					parentAlias.merge();						
				}
			}
		}

		Set<ParentAlias> newParentAliases = new HashSet<ParentAlias>();
		for (ParentAliasDTO parentAliasDTO : parentDTO.getParentAliases()){
			if (parentAliasMAP.containsKey(parentAliasDTO.getAliasName())){
				
			} else {
				
			}
			ParentAlias newParentAlias = new ParentAlias();
			newParentAlias.setAliasName(parentAliasDTO.getAliasName());
			newParentAlias.setLsType(parentAliasDTO.getLsType());
			newParentAlias.setLsKind(parentAliasDTO.getLsKind());
			newParentAlias.setParent(parent);
			newParentAlias.persist();
			newParentAliases.add(newParentAlias);
		}
		
		parent.setParentAliases(newParentAliases);
		
		return parent;
	}

}

