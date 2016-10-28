package com.labsynch.cmpdreg.service.parentservice;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.IsoSalt;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.domain.StereoCategory;
import com.labsynch.cmpdreg.dto.ParentDTO;
import com.labsynch.cmpdreg.dto.RegSearchDTO;
import com.labsynch.cmpdreg.dto.SaltFormDTO;
import com.labsynch.cmpdreg.service.ChemStructureService;
import com.labsynch.cmpdreg.service.ParentStructureService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
@Transactional
public class RegSearchTest {
	
	private static final Logger logger = LoggerFactory.getLogger(RegSearchTest.class);

	@Autowired
	private ChemStructureService structureService;
	
	@Autowired
	private ParentStructureService parentService;

	@Test
	public void basicParentSearch() {
		RegSearchDTO regSearchDTO = new RegSearchDTO();

		String searchName = "CMPD-0545";

		//TODO foramt corpName. Could be parent or lot. Different search behavior


		List<Parent> parents = Parent.findParentsByCorpNameEquals(searchName).getResultList();
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
		
//		Parent singleParent = parents.get(0);
//		singleParent.getStereoCategory();
//		StereoCategory newStereoCategory = StereoCategory.findStereoCategorysByCodeEquals("achiral").getSingleResult();
//		singleParent.setStereoCategory(newStereoCategory);
//		logger.debug("current stereoCategory " + singleParent.getStereoCategory());
//		parentService.update(singleParent);

	}   


}
