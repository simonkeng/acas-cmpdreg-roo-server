package com.labsynch.cmpdreg.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.cmpdreg.domain.CorpName;
import com.labsynch.cmpdreg.domain.Parent;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class ParentCorpNameTest {
		
	   // @Test
	    public void generateParentCorpName() {
	    	//simple test to generate a new corpName
	    	CorpName corpName = new CorpName();
	    	corpName.setComment("test101");
	    	corpName.persist();
	    	long corpId = corpName.getId();
	    	
//	    	long corpId = 12L;
	    	String corpNameString = CorpName.formatCorpName(corpId);
	    	System.out.println("Current corpName = " + corpNameString);	
	

	    }
	    
	  //  @Test
	    public void generateSaltFormCorpName() {
	    	//simple test to generate a new corpName
	    	CorpName corpName = new CorpName();
	    	corpName.setComment("test101");
	    	corpName.persist();
	    	long corpId = corpName.getId();
	    	
//	    	long corpId = 12L;
	    	String corpNameString = CorpName.formatCorpName(corpId);
	    	System.out.println("Current corpName = " + corpNameString);	

	    	
	    	
	    	
	

	    }
	    

    @Test
    public void testMarkerMethod() {
        org.junit.Assert.assertTrue(true);
    }
}
