package com.labsynch.cmpdreg.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.domain.StereoCategory;
import com.labsynch.cmpdreg.dto.Metalot;
import com.labsynch.cmpdreg.dto.MetalotReturn;
import com.labsynch.cmpdreg.service.MetalotService;
import com.labsynch.cmpdreg.service.SetupService;

import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;

@RequestMapping("/adminsetup")
@Controller
public class SetupDBController {

	private static final Logger logger = LoggerFactory.getLogger(SetupDBController.class);

	@Autowired
	private MetalotService metalotServ;
	
	@Autowired
	private SetupService setupServ;
	
//	@RequestMapping(value = "loadplainsdf", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<String> loadCorpNames(@RequestBody String fileNamePath) {
//		HttpHeaders headers= new HttpHeaders();
//        headers.add("Content-Type", "application/text");
//        
//        try {
//			setupServ.loadCorpNames();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//	        return new ResponseEntity<String>("error saving new corpNames. File not found.", headers, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//        
//        return new ResponseEntity<String>("saved new corpNames", headers, HttpStatus.OK);
//
//	}
//	
	@RequestMapping(value = "loadplainsdf", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> loadCmpdsFromSDF(@RequestBody String fileNamePath) {
		
    	//simple utility to load compounds without any properties

    	String fileName = fileNamePath;
    	
		FileInputStream fis;	
	    int molCount = 0;
	    
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

			    	lot.setSynthesisDate(new Date());
			    	lot.setChemist(chemist);
			    	lot.setNotebookPage(noteBookInfo);
			    	lot.setSaltForm(saltForm);
			    	lot.setLotMolWeight(mol.getMass());
			    	lot.setLotMolWeight(mol.getMass());
			    	lot.setIsVirtual(false);

			    	metaLot.setLot(lot);
			    	
			    	MetalotReturn results = metalotServ.save(metaLot);
			    	logger.debug("lot saved: " + results.toJson());
			    	
			    	molCount++;
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
		
		HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");

        return new ResponseEntity<String>("saved " + molCount + " new structures", headers, HttpStatus.CREATED);
    }


}
