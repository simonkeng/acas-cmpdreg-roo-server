package com.labsynch.cmpdreg.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.cmpdreg.domain.FileList;
import com.labsynch.cmpdreg.domain.IsoSalt;
import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.dto.Metalot;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class MetaLotGetTest {
	    
	   @Test
	    public void generateExampleJSON() {
	    	//simple test to generate metalot test load object for John
	    	String corpName = "CMPD-0001-Na-01";
	    	Metalot metaLot = new Metalot();
	    	List<Lot> lots = Lot.findLotsByCorpNameEquals(corpName).getResultList(); 
	    	System.out.println("Number of lots found = " + lots.size());	

	    	if (lots.size() == 0) {
		    	System.out.println("Did not find a lot with corpName = " + corpName);	
	    	}
	    	
	    	else {
	    		Lot lot = lots.get(0);
		    	metaLot.setLot(lot);

		    	SaltForm saltForm = SaltForm.findSaltForm(metaLot.getLot().getSaltForm().getId());
		    	List<IsoSalt> isoSalts = IsoSalt.findIsoSaltsBySaltForm(saltForm).getResultList();
		    	metaLot.getIsosalts().addAll(isoSalts);

		    	List<FileList> fileLists = FileList.findFileListsByLot(lot).getResultList();
		    	metaLot.getFileList().addAll(fileLists);
		    		
		    	System.out.println(metaLot.toJson());	    		
	    	}
	    	
	    }

    @Test
    public void testMarkerMethod() {
        org.junit.Assert.assertTrue(true);
    }
}
