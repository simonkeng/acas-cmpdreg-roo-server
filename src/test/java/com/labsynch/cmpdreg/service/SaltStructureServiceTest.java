package com.labsynch.cmpdreg.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.chemclasses.CmpdRegMolecule;
import com.labsynch.cmpdreg.domain.Salt;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class SaltStructureServiceTest {
	
	@Autowired
	private ChemStructureService chemService;
	
	@Autowired
	private SaltStructureService saltStructureService;
	
	private static Logger logger = Logger.getRootLogger();
	
//	@Test
	public void test_1() throws SQLException, IOException{
//		int count = jcs.getCount();
//		System.out.println("here is the count " + count);
//		
//		String molfile = "CCC";
//		MolHandler mh = new MolHandler(molfile);
//		Molecule mol = mh.getMolecule();
//		String saltStructure = mol.toFormat("mol");
//	
//		Integer cdId = jcs.saveStructure(saltStructure);
//		System.out.println("here is the new structure id: " + cdId);
//		
//		logger.info("logger: here is the new structure id: " + cdId);


	}
	
//	@Test
	public void test_2() throws SQLException, IOException{
//		int count = jcs.getCount();
//		System.out.println("here is the count " + count);
//		
//		String molfile = "CCC";
//		MolHandler mh = new MolHandler(molfile);
//		Molecule mol = mh.getMolecule();
//		String saltStructure = mol.toFormat("mol");
//	
//		Integer cdId = jcs.saveStructure(saltStructure);
//		System.out.println("here is the new structure id: " + cdId);
//		
//		logger.info("logger: here is the new structure id: " + cdId);


	}


	//@Test
	@Transactional
	@Rollback(false)
	public void updateTest() {
		List<Salt> salts = Salt.findAllSalts();
		Salt updatedSalt;
		for (Salt salt : salts){
			updatedSalt = saltStructureService.update(salt);
			if (updatedSalt == null){
				logger.error("unable to update the salt: " + salt.toJson());
			}
		}		
	}
	
    @Test
    public void testMarkerMethod() {
        org.junit.Assert.assertTrue(true);
    }
    
    @Test
	@Transactional
	@Rollback(false)
    public void updateSingleSalt() {
    
    Salt salt = Salt.findSalt(20702L);
	CmpdRegMolecule mol = chemService.toMolecule("C(=O)(C(F)(F)F)O");
	salt.setOriginalStructure(salt.getMolStructure());
	salt.setMolStructure(mol.getMolStructure());
	salt.setFormula(mol.getFormula());
	salt.setMolWeight(mol.getMass());
	salt.setCharge(mol.getTotalCharge());

	logger.debug("salt code: " + salt.getAbbrev());
	logger.debug("salt name: " + salt.getName());
	logger.debug("salt structure: " + salt.getMolStructure());

	boolean updated = chemService.updateStructure(mol, "Salt_Structure", salt.getCdId());
	
	logger.debug("Update status: " + updated);
	
    }
}
