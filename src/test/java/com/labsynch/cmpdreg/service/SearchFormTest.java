package com.labsynch.cmpdreg.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.chemclasses.CmpdRegMolecule;
import com.labsynch.cmpdreg.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.dto.SearchCompoundReturnDTO;
import com.labsynch.cmpdreg.dto.SearchLotDTO;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
@Transactional
public class SearchFormTest {

	@Autowired
	CmpdRegMoleculeFactory cmpdRegMoleculeFactory;

	//@Test
	public void basicParentSearch() {
		String searchName = "CMPD-1202";
		List<Parent> parents = Parent.findParentsByCorpNameEquals(searchName).getResultList();
		System.out.println("number of parents found: " + parents.size());
		Parent parent = parents.get(0);
		Set<SearchCompoundReturnDTO> foundCompounds = new HashSet<SearchCompoundReturnDTO>();

		List<SaltForm> saltForms = SaltForm.findSaltFormsByParent(parent).getResultList();
		for (SaltForm saltForm : saltForms){
			SearchCompoundReturnDTO searchCompound = new SearchCompoundReturnDTO();
			searchCompound.setCorpName(saltForm.getCorpName());
			searchCompound.setStereoCategoryName(saltForm.getParent().getStereoCategory().getCode());
			searchCompound.setStereoComment(saltForm.getParent().getStereoComment());
			if (saltForm.getCdId() == 0){
				searchCompound.setMolStructure(saltForm.getParent().getMolStructure());
			} else {
				searchCompound.setMolStructure(saltForm.getMolStructure());
			}
			List<Lot> lots = Lot.findLotsBySaltForm(saltForm).getResultList();
			for (Lot lot : lots){
				SearchLotDTO searchLot = new SearchLotDTO();
				searchLot.setCorpName(lot.getCorpName());
				searchLot.setLotNumber(lot.getLotNumber());
				searchCompound.getLotIDs().add(searchLot);
			}
			foundCompounds.add(searchCompound);
		}

		System.out.println("search results: " + SearchCompoundReturnDTO.toJsonArray(foundCompounds));
	}   

	@Autowired
	private ChemStructureService structureService;

	// @Test
	public void basicStructureSearch() throws CmpdRegMolFormatException{

		String corpName = "CMPD-0001";
		List<Parent> parents = Parent.findParentsByCorpNameEquals(corpName).getResultList();
		System.out.println("number of parents found: " + parents.size());
		Parent parent = null;
		if (parents.size() > 0){
			parent = parents.get(0);    	     		
		}

		CmpdRegMolecule mol = cmpdRegMoleculeFactory.getCmpdRegMolecule("C1=CC=CC=C1");
		System.out.println("search with structure: " + mol.getSmiles());
		String searchTerm = "SUBSTRUCTURE";
		float similarity = 0.0f;
		int[] parentHits = structureService.searchMolStructures(mol.getMolStructure(), "Parent_Structure", "Parent",searchTerm, similarity);
		System.out.println("number of parents found: " + parentHits.length);

		structureService.closeConnection();

	}

	@Test
	public void basicStructureSearch_2() throws CmpdRegMolFormatException{

		CmpdRegMolecule mol = cmpdRegMoleculeFactory.getCmpdRegMolecule("COC1=CC(Cl)=CC=C1");
		
		System.out.println("search with structure: " + mol.getSmiles());
		String searchTerm = "SUBSTRUCTURE";
		float similarity = 0.0f;
		int[] parentHits = structureService.searchMolStructures(mol.getMolStructure(), "Parent_Structure", "Parent",searchTerm, similarity);
		System.out.println("number of parents found: " + parentHits.length);

		structureService.closeConnection();

	}

}
