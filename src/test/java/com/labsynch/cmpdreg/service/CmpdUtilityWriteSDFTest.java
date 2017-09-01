package com.labsynch.cmpdreg.service;

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

import com.labsynch.cmpdreg.chemclasses.CmpdRegMolecule;
import com.labsynch.cmpdreg.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFWriterFactory;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFWriter;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class CmpdUtilityWriteSDFTest {

	private static final Logger logger = LoggerFactory.getLogger(CmpdUtilityWriteSDFTest.class);
	
	@Autowired
	CmpdRegMoleculeFactory cmpdRegMoleculeFactory;

	@Autowired
	CmpdRegSDFWriterFactory cmpdRegSDFWriterFactory;

	@Test
	public void loadCmpds() {
		//simple utility to load compounds

		String fileName = "src/test/resources/newSalts.sdf";
		// Open an input stream
		try {

			CmpdRegSDFWriter me = cmpdRegSDFWriterFactory.getCmpdRegSDFWriter(fileName);
			CmpdRegMolecule mol = null;

			mol = cmpdRegMoleculeFactory.getCmpdRegMolecule("[Na]");
			mol.setProperty("Name", "Na");
			mol.setProperty("code", "Na");
			me.writeMol(mol);
			
			
			mol = cmpdRegMoleculeFactory.getCmpdRegMolecule("OS(O)(O)O");
			mol.setProperty("Name", "Sulfate");
			mol.setProperty("code", "SO4");
			me.writeMol(mol);


			me.close();

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


