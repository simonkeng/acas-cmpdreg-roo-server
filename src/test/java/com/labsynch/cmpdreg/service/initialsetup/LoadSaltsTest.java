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

import com.labsynch.cmpdreg.chemclasses.CmpdRegMolecule;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFReader;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFReaderFactory;
import com.labsynch.cmpdreg.domain.Salt;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;
import com.labsynch.cmpdreg.service.ChemStructureService;
import com.labsynch.cmpdreg.utils.MoleculeUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class LoadSaltsTest {

	private static final Logger logger = LoggerFactory.getLogger(LoadSaltsTest.class);

	@Autowired
	private ChemStructureService saltStructServ;
	
	@Autowired
	private CmpdRegSDFReaderFactory sdfReaderFactory;

	@Test
	public void loadSalts() {
		//simple utility to load salts

		if (Salt.countSalts() < 1L){

			String fileName = "src/test/resources/Initial_Salts.sdf";


			// Open an input stream
			try {
				CmpdRegSDFReader mi = sdfReaderFactory.getCmpdRegSDFReader(fileName);
				CmpdRegMolecule mol = null;

				while ((mol = mi.readNextMol()) != null) {
					
					saveSalt(mol);

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
		} else {
			logger.debug("Salts already loaded -- nothing to load");
		}
		

	}

	@Transactional
	private void saveSalt(CmpdRegMolecule mol) throws IOException, CmpdRegMolFormatException {
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
