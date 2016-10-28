package com.labsynch.cmpdreg.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import chemaxon.formats.MolExporter;
import chemaxon.formats.MolFormatException;
import chemaxon.struc.Molecule;
import chemaxon.util.MolHandler;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class CmpdUtilityWriteSDFTest {

	private static final Logger logger = LoggerFactory.getLogger(CmpdUtilityWriteSDFTest.class);

	@Test
	public void loadCmpds() {
		//simple utility to load compounds

		String fileName = "src/test/resources/newSalts.sdf";
		FileOutputStream fos;

		// Open an input stream
		try {

			fos = new FileOutputStream (fileName, true);
			MolExporter me = new MolExporter(fos, "sdf");
			Molecule mol = null;

			MolHandler mh = new MolHandler("[Na]");
			mol = mh.getMolecule();
			mol.setProperty("Name", "Na");
			mol.setProperty("code", "Na");
			me.write(mol);
			
			
			MolHandler mh2 = new MolHandler("OS(O)(O)O");
			mol = mh2.getMolecule();
			mol.setProperty("Name", "Sulfate");
			mol.setProperty("code", "SO4");
			me.write(mol);


			me.close();
			fos.close();

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


