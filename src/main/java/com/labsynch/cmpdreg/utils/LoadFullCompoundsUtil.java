package com.labsynch.cmpdreg.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.Project;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.domain.StereoCategory;
import com.labsynch.cmpdreg.domain.Vendor;
import com.labsynch.cmpdreg.dto.Metalot;
import com.labsynch.cmpdreg.dto.MetalotReturn;
import com.labsynch.cmpdreg.service.ChemStructureService;
import com.labsynch.cmpdreg.service.ErrorMessage;
import com.labsynch.cmpdreg.service.MetalotService;

import chemaxon.formats.MolExporter;
import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;

@Service
public class LoadFullCompoundsUtil {

	static Logger logger = LoggerFactory.getLogger(LoadFullCompoundsUtil.class);

	@Autowired
	private MetalotService metalotServ;

	@Autowired
	private ChemStructureService chemService;

	public void loadCompounds(String inputFileName, String outputFileName, HashMap<String, String> propertiesMap){
		//simple utility to load compounds
		
		// I want to set up a hash map of properties
		// ALIAS --> parent.commonName (string 1000)
		// VENDORCATNO --> lot.supplierID (String 255)
		// PROJECT --> lot.project (Project --> findByCode)
		
		// vendor --> lot.supplier (String 255)
		// chemist --> lot.chemist (Scientist --> findByCode)

		FileInputStream fis;	

		try {
			fis = new FileInputStream (inputFileName);
			MolImporter mi = new MolImporter(fis);
			Molecule mol = null;

			int molCounter = 0;
			//			 && molCounter < 5
			while ((mol = mi.read()) != null) {
				mol.dearomatize();
				logger.debug("current molCounter: " + molCounter);
				molCounter++;
				if (molCounter > 0){
					loadCompoundsMol(mol, outputFileName, propertiesMap);					
				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Transactional
	private void loadCompoundsMol(Molecule mol, String outputFileName, HashMap<String, String> propertiesMap) throws IllegalArgumentException, IOException {
		//simple utility to load compound mol
		FileOutputStream fos = new FileOutputStream (outputFileName, true);
		MolExporter me = new MolExporter(fos, "sdf");

		boolean goodMolToProcess = true;
		mol.clearExtraLabels();

		//look for cmpd scientist. Create a new one if absent.
		String chemistCodeName = null;
		Scientist cmpdChemist = null;
		if (MoleculeUtil.validateMolProperty(mol, propertiesMap.get("compound_chemist"))){
			chemistCodeName = MoleculeUtil.getMolProperty(mol, propertiesMap.get("compound_chemist")).toLowerCase();
			logger.debug("query chemist = " + chemistCodeName);
			logger.debug("chemist is: " + cmpdChemist.toJson());
		} else if (propertiesMap.get("compound_chemist_code") != null) {
			//get chemist codename from the map
			chemistCodeName = propertiesMap.get("compound_chemist_code");
		} else {
			chemistCodeName = "cchemist"; //defaul value
		}
		try{
			cmpdChemist = Scientist.findScientistsByCodeEquals(chemistCodeName).getSingleResult();
		} catch (EmptyResultDataAccessException e){
			if (chemistCodeName.trim().equalsIgnoreCase("")){
				//default set cchemist as the chemist
				chemistCodeName = "cchemist";
				cmpdChemist = Scientist.findScientistsByCodeEquals(chemistCodeName).getSingleResult();
			} else {
				logger.debug("create the new chemist" + chemistCodeName);
				cmpdChemist = new Scientist();
				cmpdChemist.setCode(chemistCodeName);
				cmpdChemist.setName(chemistCodeName);
				cmpdChemist.setIsChemist(true);
				cmpdChemist.persist();							
			}
		}
		
		Scientist lotChemist = cmpdChemist;

		
    	Metalot metaLot = new Metalot();
    	Lot lot = new Lot();
    	SaltForm saltForm = new SaltForm();
    	Parent parent = new Parent();		
    	
    	lot.setAsDrawnStruct(mol.toFormat("mol"));

    	mol.clearExtraLabels();
    	parent.setMolStructure(mol.toFormat("mol"));
    	parent.setChemist(cmpdChemist);
    	
		if (MoleculeUtil.validateMolProperty(mol,  propertiesMap.get("compound_alias"))){
			String cmpdAlias = MoleculeUtil.getMolProperty(mol,  propertiesMap.get("compound_alias"));
			parent.setCommonName(cmpdAlias);
		}
    	
		String stereoCategoryCode =  null;
		if (propertiesMap.get("stereo_category") == null){
			stereoCategoryCode =  "unknown";
		} else {
			stereoCategoryCode =  propertiesMap.get("stereo_category");
		}
		try {
			StereoCategory stereoCategory = StereoCategory.findStereoCategorysByCodeEquals(stereoCategoryCode).getSingleResult();
			logger.info("found the following stereo category  " + stereoCategory.toJson());
			parent.setStereoCategory(stereoCategory);
		} catch (EmptyResultDataAccessException e){
			logger.error("Did not find the query stereoCategoryCode: " + stereoCategoryCode);
		}
		
    	saltForm.setParent(parent);
    	saltForm.setChemist(cmpdChemist);
    	saltForm.setMolStructure("");

    	lot.setSynthesisDate(new Date());
    	lot.setChemist(lotChemist);
    	lot.setSaltForm(saltForm);
    	lot.setLotMolWeight(mol.getMass());
    	lot.setIsVirtual(false);
		
    	String supplierCode;
		if (MoleculeUtil.validateMolProperty(mol, propertiesMap.get("supplier"))){
			supplierCode = MoleculeUtil.getMolProperty(mol,  propertiesMap.get("supplier"));
		} else {
			supplierCode =  propertiesMap.get("supplier");
		}
		if (supplierCode != null){
			try {
				Vendor vendor = Vendor.findVendorsByCodeEquals(supplierCode).getSingleResult();
				lot.setVendor(vendor);
			} catch (EmptyResultDataAccessException e){
				logger.error("Did not find the query supplierCode: " + supplierCode);
			}			
		}

		
		if (MoleculeUtil.validateMolProperty(mol, propertiesMap.get("notebook"))){
			String notebook = MoleculeUtil.getMolProperty(mol,  propertiesMap.get("notebook"));
			lot.setNotebookPage(notebook);
		}    	
		
		if (MoleculeUtil.validateMolProperty(mol, propertiesMap.get("supplier_id"))){
			String supplierId = MoleculeUtil.getMolProperty(mol,  propertiesMap.get("supplier_id"));
			lot.setSupplierID(supplierId);
		}

		if (MoleculeUtil.validateMolProperty(mol, propertiesMap.get("project"))){
			String projectCode = MoleculeUtil.getMolProperty(mol,  propertiesMap.get("project"));
			Project project = null;
			try {
				project = Project.findProjectsByCodeEquals(projectCode).getSingleResult();
			} catch (EmptyResultDataAccessException e){
				logger.error("Did not find the query Project: " + projectCode);
			}
			lot.setProject(project);
		}
    	
    	metaLot.setLot(lot);
    	
		if (goodMolToProcess){
			logger.debug("attempting to save the good mol");
			MetalotReturn results = metalotServ.save(metaLot);
			logger.debug("lot saved: " + results.toJson());
			if(results.getErrors().size() > 0){
				logger.error("Error while saving the metaLot." );
				ArrayList<ErrorMessage> errors = results.getErrors();
				String errorMessage = "";
				boolean firstError = true;
				for (ErrorMessage error : errors){
					if (firstError){
						errorMessage = error.getMessage();
						firstError = false;
					} else {
						errorMessage = errorMessage.concat(". ").concat(error.getMessage());						
					}
				}
				mol.setProperty("metalot-error", errorMessage);
				me.write(mol);
			}
		} else {
			logger.error("Unable to process the bad mol. " );
			me.write(mol);
		}

		me.close();
		fos.close();
	}

}