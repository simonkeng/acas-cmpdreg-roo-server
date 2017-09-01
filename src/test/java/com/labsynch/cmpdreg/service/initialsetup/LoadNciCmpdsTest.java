package com.labsynch.cmpdreg.service.initialsetup;

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
import org.springframework.transaction.annotation.Transactional;

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
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class LoadNciCmpdsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(LoadNciCmpdsTest.class);

	@Autowired
	private MetalotService metalotServ;
	
	@Autowired
	CmpdRegMoleculeFactory cmpdRegMoleculeFactory;

	@Autowired
	CmpdRegSDFReaderFactory sdfReaderFactory;
		
    @Test
    public void loadCmpds() {
    	//simple utility to load compounds without any properties

    	String fileName = "src/test/resources/nciSample10.sdf";
    	
		
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
