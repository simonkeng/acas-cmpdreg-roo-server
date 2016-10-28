package com.labsynch.cmpdreg.service.initialsetup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;

import com.labsynch.cmpdreg.domain.Salt;
import com.labsynch.cmpdreg.service.ChemStructureService;
import com.labsynch.cmpdreg.utils.MoleculeUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class LoadSaltsTest {

	private static final Logger logger = LoggerFactory.getLogger(LoadSaltsTest.class);

	@Autowired
	private ChemStructureService saltStructServ;

	@Test
	public void loadSalts() {
		//simple utility to load salts

		if (Salt.countSalts() < 1L){

			String fileName = "src/test/resources/Initial_Salts.sdf";

			FileInputStream fis;	


			// Open an input stream
			try {
				fis = new FileInputStream (fileName);
				MolImporter mi = new MolImporter(fis);
				Molecule mol = null;

				while ((mol = mi.read()) != null) {
					
					saveSalt(mol);

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
		} else {
			logger.debug("Salts already loaded -- nothing to load");
		}
		

	}

	@Transactional
	private void saveSalt(Molecule mol) throws IOException {
		Salt salt = new Salt();
		salt.setMolStructure(MoleculeUtil.exportMolAsText(mol, "mol"));
		salt.setOriginalStructure(MoleculeUtil.exportMolAsText(mol, "mol"));
		salt.setAbbrev(MoleculeUtil.getMolProperty(mol, "code"));
		salt.setName(MoleculeUtil.getMolProperty(mol, "Name"));
		salt.setFormula(mol.getFormula());
		salt.setMolWeight(mol.getMass());

		logger.debug("salt code: " + salt.getAbbrev());
		logger.debug("salt name: " + salt.getName());
		logger.debug("salt structure: " + salt.getMolStructure());

		int[] queryHits = saltStructServ.searchMolStructures(salt.getMolStructure(), "Salt_Structure", "salt", "DUPLICATE_NO_TAUTOMER");
		Integer cdId = 0;
		if (queryHits.length > 0){
			cdId = 0;
		} else {
			cdId = saltStructServ.saveStructure(salt.getMolStructure(), "Salt_Structure");			
		}
		salt.setCdId(cdId);


		if (salt.getCdId() > 0 && salt.getCdId() != -1){
			salt.persist();
		} else {
			logger.error("Could not save the salt: " + salt.getAbbrev());
		}		
	}

}
