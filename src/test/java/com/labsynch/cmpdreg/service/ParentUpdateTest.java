package com.labsynch.cmpdreg.service;

import java.util.List;

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

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class ParentUpdateTest {
	
	@Autowired
	private ParentStructureService parentService;

	@Autowired
	private ChemStructureService chemService;
	
	private static final Logger logger = LoggerFactory.getLogger(ParentUpdateTest.class);

	  @Transactional
	   //@Test
	    public void updateParent_Test1() {

		   Parent parent = Parent.findParent(50040L);
	    	parent.setCommonName("test common name");
	    	parent.merge();
	    	logger.debug("here is the test parent: " + parent.toJson());

	    }
	    
	  @Transactional
	  // @Test
	    public void updateParent_Test2() {

		   Parent parent = Parent.findParent(50040L);
	    	parent.setCommonName("test common name");
	    	
	    	Parent pp = parentService.update(parent);
	    	logger.debug("here is the test parent: " + pp.toJson());

	    }
	  
	  @Transactional
//	   @Test
	    public void updateParent_Test3() {

		   Parent parent = Parent.findParent(59544L);
	    	parent.setCommonName("test common name");
	    	//logger.debug("mol formula is :" + chemService.getMolFormula(parent.getMolStructure()));
	    	//logger.debug("mol file is :" + chemService.toMolfile(parent.getMolStructure()));
	    	Parent pp = parentService.update(parent);
	    	//logger.debug("here is the test parent: " + pp.toJson());
	    	logger.debug("the parent weight is: " + pp.getMolWeight());
	    	pp.merge();
	    	pp.flush();
	    	pp.clear();

	    }
	  
		@Transactional
		@Rollback(true)
		@Test
		public void updateAllParentsWeigt0Test(){
			List<Parent> parents = Parent.findParentsByLessMolWeight(1.0d);
			for (Parent parent : parents){
				parentService.update(parent);
			}
			
		}
		
		@Transactional
		@Rollback(false)
		@Test
		public void updateAllParentsWeightTest(){
			List<Parent> parents = Parent.findAllParents();
			for (Parent parent : parents){
				parentService.update(parent);
			}
			
		}

}
