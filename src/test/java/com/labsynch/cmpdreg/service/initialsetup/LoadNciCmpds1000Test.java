package com.labsynch.cmpdreg.service.initialsetup;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.cmpdreg.chemclasses.CmpdRegMolecule;
import com.labsynch.cmpdreg.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFReader;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFReaderFactory;
import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.domain.StereoCategory;
import com.labsynch.cmpdreg.dto.Metalot;
import com.labsynch.cmpdreg.dto.MetalotReturn;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;
import com.labsynch.cmpdreg.service.MetalotService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Configurable
public class LoadNciCmpds1000Test {
	
	private static final Logger logger = LoggerFactory.getLogger(LoadNciCmpds1000Test.class);

	@Autowired
	private MetalotService metalotServ;
	
	@Autowired
	CmpdRegMoleculeFactory cmpdRegMoleculeFactory;

	@Autowired
	CmpdRegSDFReaderFactory sdfReaderFactory;
		
	@Transactional
    @Test
    public void loadCmpds() {
    	//simple utility to load compounds without any properties

    	String fileName = "src/test/resources/nci1000.sdf";
		
		    // Open an input stream
		    try {
		    		CmpdRegSDFReader mi = sdfReaderFactory.getCmpdRegSDFReader(fileName);
			    CmpdRegMolecule mol = null;

			    while ((mol = mi.readNextMol()) != null) {
			    	
			    	Metalot metaLot = new Metalot();
			    	Lot lot = new Lot();
			    	SaltForm saltForm = new SaltForm();
			    	Parent parent = new Parent();		
			    	
			    	lot.setAsDrawnStruct(mol.getMolStructure());

//			    	mol.clearExtraLabels();
			    	parent.setMolStructure(mol.getMolStructure());
			    	Scientist chemist = Scientist.findScientistsByCodeEquals("cchemist").getSingleResult();
			    	String noteBookInfo = "JM-001-006";
			    	StereoCategory stereoCategory = StereoCategory.findStereoCategorysByCodeEquals("achiral").getSingleResult();
			    	
			    	parent.setChemist(chemist);
			    	parent.setStereoCategory(stereoCategory);
			    	
			    	saltForm.setParent(parent);
			    	saltForm.setChemist(chemist);
			    	saltForm.setMolStructure("");

			    	lot.setSynthesisDate(new Date());
			    	lot.setChemist(chemist);
			    	lot.setNotebookPage(noteBookInfo);
			    	lot.setSaltForm(saltForm);
			    	lot.setLotMolWeight(mol.getMass());
			    	lot.setLotMolWeight(mol.getMass());
			    	lot.setIsVirtual(false);

			    	metaLot.setLot(lot);
			    	
			    	MetalotReturn results = metalotServ.save(metaLot);
//			    	logger.debug("lot saved: " + results.toJson());
			    	
			    }	
			    
			    mi.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CmpdRegMolFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

    	
    }

}
