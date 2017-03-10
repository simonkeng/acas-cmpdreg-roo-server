package com.labsynch.cmpdreg.service;

import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.StereoCategory;
import com.labsynch.cmpdreg.dto.CodeTableDTO;
import com.labsynch.cmpdreg.dto.ParentDTO;
import com.labsynch.cmpdreg.dto.ParentEditDTO;
import com.labsynch.cmpdreg.dto.ParentValidationDTO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ParentServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(ParentServiceTest.class);
	
	@Autowired
	private ParentService parentService;
	
//	@Test
	@Transactional
	public void validateParent_noOp() throws Exception{
		List<Parent> parents = Parent.findParentEntries(0, 2);
    	Parent firstParent = parents.get(0);
    	Parent secondParent = parents.get(1);
    	logger.debug(firstParent.toJson());
    	ParentValidationDTO validationDTO = parentService.validateUniqueParent(firstParent);
    	logger.info(validationDTO.toJson());
    	Assert.assertTrue(validationDTO.isParentUnique());
    	Assert.assertFalse(validationDTO.getAffectedLots().isEmpty());
    	Assert.assertNull(validationDTO.getDupeParents());
	}
	
//	@Test
	@Transactional
	public void validateParent_obviousDupe() throws Exception{
		List<Parent> parents = Parent.findParentEntries(0, 2);
    	Parent firstParent = parents.get(0);
    	Parent secondParent = parents.get(1);
    	Parent testParent = new Parent();
    	testParent.setMolStructure(firstParent.getMolStructure());
    	testParent.setStereoCategory(firstParent.getStereoCategory());
    	testParent.setStereoComment(firstParent.getStereoComment());
    	testParent.setCorpName(secondParent.getCorpName());
    	logger.debug(testParent.toJson());
    	ParentValidationDTO validationDTO = parentService.validateUniqueParent(testParent);
    	logger.info(validationDTO.toJson());
    	Assert.assertFalse(validationDTO.isParentUnique());
    	Assert.assertNull(validationDTO.getAffectedLots());
    	Assert.assertFalse(validationDTO.getDupeParents().isEmpty());
	}
	
//	@Test
	@Transactional
	public void validateParent_changeStereoCategory() throws Exception{
		List<Parent> parents = Parent.findParentEntries(0, 2);
    	Parent firstParent = parents.get(0);
    	Parent secondParent = parents.get(1);
    	Parent testParent = new Parent();
    	testParent.setMolStructure(firstParent.getMolStructure());
    	if (firstParent.getStereoCategory().getCode().equals("unknown")){
    		testParent.setStereoCategory(StereoCategory.findStereoCategorysByCodeEquals("achiral").getSingleResult());
    	}else{
    		testParent.setStereoCategory(StereoCategory.findStereoCategorysByCodeEquals("unknown").getSingleResult());
    	}
    	testParent.setStereoComment(firstParent.getStereoComment());
    	testParent.setCorpName(firstParent.getCorpName());
    	logger.debug(testParent.toJson());
    	ParentValidationDTO validationDTO = parentService.validateUniqueParent(testParent);
    	logger.info(validationDTO.toJson());
    	Assert.assertTrue(validationDTO.isParentUnique());
    	Assert.assertFalse(validationDTO.getAffectedLots().isEmpty());
    	Assert.assertNull(validationDTO.getDupeParents());
	}
	
//	@Test
	@Transactional
	public void validateAndUpdateParent_changeStereoCategory() throws Exception{
		List<Parent> parents = Parent.findParentEntries(0, 2);
    	Parent firstParent = parents.get(0);
    	Parent secondParent = parents.get(1);
    	Parent testParent = new Parent();
    	testParent.setMolStructure(firstParent.getMolStructure());
    	if (firstParent.getStereoCategory().getCode().equals("unknown")){
    		testParent.setStereoCategory(StereoCategory.findStereoCategorysByCodeEquals("achiral").getSingleResult());
    	}else{
    		testParent.setStereoCategory(StereoCategory.findStereoCategorysByCodeEquals("unknown").getSingleResult());
    	}
    	testParent.setId(firstParent.getId());
    	testParent.setStereoComment(firstParent.getStereoComment());
    	testParent.setCorpName(firstParent.getCorpName());
    	testParent.setChemist(firstParent.getChemist());
    	testParent.setCommonName(firstParent.getCommonName());
    	testParent.setIgnore(firstParent.getIgnore());
    	testParent.setParentNumber(firstParent.getParentNumber());
    	testParent.setRegistrationDate(firstParent.getRegistrationDate());
    	testParent.setCompoundType(firstParent.getCompoundType());
    	testParent.setParentAnnotation(firstParent.getParentAnnotation());
    	logger.debug(testParent.toJson());
    	ParentValidationDTO validationDTO = parentService.validateUniqueParent(testParent);
    	logger.info(validationDTO.toJson());
    	Assert.assertTrue(validationDTO.isParentUnique());
    	Assert.assertFalse(validationDTO.getAffectedLots().isEmpty());
    	Assert.assertNull(validationDTO.getDupeParents());
    	
    	Collection<CodeTableDTO> affectedLots = parentService.updateParent(testParent);
    	logger.info(CodeTableDTO.toJsonArray(affectedLots));
    	Assert.assertFalse(affectedLots.isEmpty());

	}
	
//	@Test
	@Transactional
	public void validateAndUpdateParent_changeStereoCategoryAndStructure() throws Exception{
		List<Parent> parents = Parent.findParentEntries(0, 2);
    	Parent firstParent = parents.get(0);
    	Parent secondParent = parents.get(1);
    	Parent testParent = new Parent();
    	testParent.setMolStructure(secondParent.getMolStructure());
    	if (firstParent.getStereoCategory().getCode().equals("unknown")){
    		testParent.setStereoCategory(StereoCategory.findStereoCategorysByCodeEquals("achiral").getSingleResult());
    	}else{
    		testParent.setStereoCategory(StereoCategory.findStereoCategorysByCodeEquals("unknown").getSingleResult());
    	}
    	testParent.setId(firstParent.getId());
    	testParent.setStereoComment(firstParent.getStereoComment());
    	testParent.setCorpName(firstParent.getCorpName());
    	testParent.setChemist(firstParent.getChemist());
    	testParent.setCommonName(firstParent.getCommonName());
    	testParent.setIgnore(firstParent.getIgnore());
    	testParent.setParentNumber(firstParent.getParentNumber());
    	testParent.setRegistrationDate(firstParent.getRegistrationDate());
    	testParent.setCompoundType(firstParent.getCompoundType());
    	testParent.setParentAnnotation(firstParent.getParentAnnotation());
    	logger.debug(testParent.toJson());
    	ParentValidationDTO validationDTO = parentService.validateUniqueParent(testParent);
    	logger.info(validationDTO.toJson());
    	Assert.assertTrue(validationDTO.isParentUnique());
    	Assert.assertFalse(validationDTO.getAffectedLots().isEmpty());
    	Assert.assertNull(validationDTO.getDupeParents());
    	
    	Collection<CodeTableDTO> affectedLots = parentService.updateParent(testParent);
    	logger.info(CodeTableDTO.toJsonArray(affectedLots));
    	Assert.assertFalse(affectedLots.isEmpty());

	}

	
	@Test
	@Transactional
	@Rollback(false)
	public void updateParentMeta() throws Exception{

		ParentEditDTO parentDTO = new ParentEditDTO();
		parentDTO.setCorpName("CMPD-0000001");
		parentDTO.setComment("parent comment update");
		parentDTO.setChemistCode("cchemist");
		parentDTO.setCommonNameAliases("apple;banana-pepper;pear");
		
		Parent parent = parentService.updateParentMeta(parentDTO, "cchemist" );
		logger.info(parent.toJson());
		
	}
}


