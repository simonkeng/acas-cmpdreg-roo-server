package com.labsynch.cmpdreg.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.domain.StereoCategory;
import com.labsynch.cmpdreg.dto.Metalot;
import com.labsynch.cmpdreg.dto.MetalotReturn;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class LoadNciCmpdsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(LoadNciCmpdsTest.class);

	@Autowired
	private MetalotService metalotServ;
		
    @Test
    public void loadCmpds() {
    	//simple utility to load compounds without any properties

    	String fileName = "src/test/resources/nci1000.sdf";
    	
		FileInputStream fis;	
		
		    // Open an input stream
		    try {
				fis = new FileInputStream (fileName);
			    MolImporter mi = new MolImporter(fis);
			    Molecule mol = null;

			    while ((mol = mi.read()) != null) {
			    	
			    	Metalot metaLot = new Metalot();
			    	Lot lot = new Lot();
			    	SaltForm saltForm = new SaltForm();
			    	Parent parent = new Parent();		
			    	lot.setLotNumber(1);
			    	lot.setAsDrawnStruct(mol.toFormat("mol"));

			    	mol.clearExtraLabels();
			    	parent.setMolStructure(mol.toFormat("mol"));
			    	Scientist chemist = Scientist.findScientistsByCodeEquals("cchemist").getSingleResult();
			    	String noteBookInfo = "1234-123";
			    	StereoCategory stereoCategory = StereoCategory.findStereoCategorysByCodeEquals("achiral").getSingleResult();
			    	
			    	parent.setChemist(chemist);
			    	parent.setStereoCategory(stereoCategory);
			    	
			    	saltForm.setParent(parent);
			    	saltForm.setChemist(chemist);
			    	saltForm.setMolStructure("");

//			    	Set<IsoSalt> isoSalts = new HashSet<IsoSalt>();
//
//			    	double saltCount = Double.parseDouble(mol.getProperty("Count").trim());
//			    	if (saltCount > 0){
//				    	IsoSalt isoSalt = new IsoSalt();
//				    	isoSalt.setType("salt");
//				    	Salt salt = Salt.findSaltsByAbbrevEquals(mol.getProperty("SaltsRemoved")).getSingleResult();
//				    	isoSalt.setSalt(salt);
//				    	isoSalt.setEquivalents(saltCount);
//				    	isoSalt.setSaltForm(saltForm);
//				    	isoSalts.add(isoSalt);
//			    	}
			    	//DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			    	lot.setSynthesisDate(new Date());
			    	lot.setChemist(chemist);
			    	lot.setNotebookPage(noteBookInfo);
			    	lot.setSaltForm(saltForm);
			    	lot.setLotMolWeight(mol.getMass());
//			    	lot.setNotebookPage(mol.getProperty("Notebook").trim());
//			    	lot.setComments(mol.getProperty("Synonyms_notes").trim());
			    	lot.setLotMolWeight(mol.getMass());
			    	lot.setIsVirtual(false);

			    	metaLot.setLot(lot);
//			    	metaLot.setIsosalts(isoSalts);
			    	
			    	MetalotReturn results = metalotServ.save(metaLot);
			    	logger.debug("lot saved: " + results.toJson());
			    	
			    }	
			    
			    mi.close();
			    fis.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MolFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

    	
    }

}
