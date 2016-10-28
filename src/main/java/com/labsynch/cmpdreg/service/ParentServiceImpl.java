package com.labsynch.cmpdreg.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chemaxon.formats.MolFormatException;
import chemaxon.struc.Molecule;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.ParentAlias;
import com.labsynch.cmpdreg.domain.Project;
import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.dto.CodeTableDTO;
import com.labsynch.cmpdreg.dto.ParentDTO;
import com.labsynch.cmpdreg.dto.ParentValidationDTO;
import com.labsynch.cmpdreg.dto.SearchCompoundReturnDTO;
import com.labsynch.cmpdreg.dto.SearchFormReturnDTO;
import com.labsynch.cmpdreg.dto.SearchLotDTO;
import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.exceptions.DupeParentException;
import com.labsynch.cmpdreg.exceptions.MissingPropertyException;
import com.labsynch.cmpdreg.exceptions.SaltedCompoundException;
import com.labsynch.cmpdreg.utils.Configuration;
import com.labsynch.cmpdreg.utils.SecurityUtil;

@Service
public class ParentServiceImpl implements ParentService {

	Logger logger = LoggerFactory.getLogger(ParentServiceImpl.class);

	public static final MainConfigDTO mainConfig = Configuration.getConfigInfo();
	
	@Autowired
	public ChemStructureService chemStructureService;
	
	@Autowired
	public ParentLotService parentLotService;
	
	@Autowired
	public ParentStructureService parentStructureService;

	@Autowired
	public ParentAliasService parentAliasService;
	
	@Override
	@Transactional
	public ParentValidationDTO validateUniqueParent(Parent queryParent) throws MolFormatException {
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
}

