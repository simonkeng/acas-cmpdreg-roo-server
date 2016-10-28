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

import chemaxon.formats.MolFormatException;
import chemaxon.struc.Molecule;
import chemaxon.util.MolHandler;

import com.labsynch.cmpdreg.domain.IsoSalt;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.dto.ParentDTO;
import com.labsynch.cmpdreg.dto.RegSearchDTO;
import com.labsynch.cmpdreg.dto.SaltFormDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
@Transactional
public class RegSearchTest {
	@Autowired
	private ChemStructureService structureService;

	//    @Test
	public void basicParentSearch() {
		RegSearchDTO regSearchDTO = new RegSearchDTO();

		String searchName = "CMPD-00";

		//TODO foramt corpName. Could be parent or lot. Different search behavior


		List<Parent> parents = Parent.findParentsByCorpNameLike(searchName).getResultList();
		System.out.println("number of parents found: " + parents.size());
		for (Parent parent : parents){
			System.out.println("name of parents found: " + parent.getCorpName());
			ParentDTO parentDTO = new ParentDTO();
			parentDTO.setParent(parent);
			List<SaltForm> saltForms = SaltForm.findSaltFormsByParent(parent).getResultList();
			for (SaltForm saltForm: saltForms){
				List<IsoSalt> isoSalts = IsoSalt.findIsoSaltsBySaltForm(saltForm).getResultList();
				SaltFormDTO saltFormDTO = new SaltFormDTO();
				saltFormDTO.setSaltForm(saltForm);
				saltFormDTO.getIsosalts().addAll(isoSalts);
				parentDTO.getSaltForms().add(saltFormDTO);
			}
			regSearchDTO.getParents().add(parentDTO);
		}    
		//        System.out.println("number of parents found in the DTO: " + regSearchDTO.getParentDTOs().size());
		//        System.out.println("parents found in the DTO: " + Parent.toJsonArray(regSearchDTO.getParentDTOs());
		System.out.println("parents found in the DTO: " + regSearchDTO);
		System.out.println("parents found in the DTO: " + regSearchDTO.toJson());
	}   

	@Test
	public void searchCmpds() {

		RegSearchDTO regSearchDTO = new RegSearchDTO();
		Set<Parent> parents = new HashSet<Parent>();
		String molStructure = "CC1=C(C)"; 
//		"CC";
		MolHandler mh = null;
		try {
			mh = new MolHandler(molStructure);
		} catch (MolFormatException e) {
			System.out.println("bad structure error: " + molStructure);
		}

		Molecule mol = mh.getMolecule();
		regSearchDTO.setAsDrawnStructure(molStructure);
		regSearchDTO.setAsDrawnMolFormula(mol.getFormula());
		regSearchDTO.setAsDrawnMolWeight(mol.getMass());
		
		System.out.println("search mol: " + mol.toFormat("smiles"));


		int[] parentHits = structureService.searchMolStructures(mol.toFormat("mol"), "Parent_Structure", "FULL", 0f);
		for (int hit : parentHits){
			System.out.println("hit - cd_id: " + hit);

			List<Parent> parentList = Parent.findParentsByCdId(hit).getResultList();
			parents.addAll(parentList);
		}

		System.out.println("number of parents found: " + parents.size());


	}

}
