package com.labsynch.cmpdreg.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.cmpdreg.chemclasses.CmpdRegMolecule;
import com.labsynch.cmpdreg.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFReader;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFReaderFactory;
import com.labsynch.cmpdreg.domain.Compound;
import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;
import com.labsynch.cmpdreg.utils.Configuration;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class DupeCompoundsUtilTest {

	private static final Logger logger = LoggerFactory.getLogger(DupeCompoundsUtilTest.class);

	@Autowired
	private ChemStructureService chemStructServ;
	
	@Autowired
	CmpdRegMoleculeFactory cmpdRegMoleculeFactory;

	@Autowired
	CmpdRegSDFReaderFactory sdfReaderFactory;


	//create the jchem table to store the compounds
  //@Test
	public void createCmpdStructureTest() {
		MainConfigDTO mainConfig = Configuration.getConfigInfo();
		if (mainConfig.getServerSettings().isInitalDBLoad()){
			chemStructServ.createJChemTable("compound.compound_structure", true);			
		}
	}

	//@Test
	@Rollback(false)
	public void dropCmpdTable() {
		// clear the table
		boolean output = chemStructServ.dropJChemTable("compound.compound_structure");
		logger.info("Results: " + output);
	}
	
	
	//@Test
	@Rollback(false)
	public void deleteCmpds() {
		// clear the table
		boolean output = chemStructServ.deleteAllJChemTableRows("compound.compound_structure");
		logger.info("Results: " + output);
	}
	
	
	// load compounds into the table


	// query SDF with query compounds to check agains the compound_structure table
	// return a list of matching cd_id to the query cd_id. Full tautomer dupe search


	@Test
	public void loadCmpds() {
		//simple utility to load compounds without any properties
		String fileName = "src/test/resources/nci1000.sdf";

		// Open an input stream
		try {
			CmpdRegSDFReader mi = sdfReaderFactory.getCmpdRegSDFReader(fileName);
			CmpdRegMolecule mol = null;

			int count = 0;
			int cdid = 0;
			while ((mol = mi.readNextMol()) != null) {
				
				logger.info("attempting to save: "  + mol.getSmiles());
				cdid = chemStructServ.saveStructure(mol.getSmiles(), "Compound_Structure");
				logger.info("current mol count is: " + count + "    cd_id: " + cdid);
				Compound cmpd = new Compound();
				cmpd.setCdId(cdid);
				cmpd.setExternal_id(mol.getProperty("CD_ID"));
				cmpd.setCreatedDate(new Date());
				cmpd.persist();
				
				count++;

			//add in batch update here to speed it up
				
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
	
	//@Test
	public void searchCmpds() {
		String fileName = "queryCmpds.sdf";


		StringWriter outputWriter = new StringWriter();


		// Open an input stream
		try {
			CmpdRegSDFReader mi = sdfReaderFactory.getCmpdRegSDFReader(fileName);
			CmpdRegMolecule mol = null;
			
			
			//write header
			String headerLine  = "query cd_id, target cd_id\n";
			outputWriter.write(headerLine);

			int count = 0;
			while ((mol = mi.readNextMol()) != null) {
				
				//logger.info("attempting to search: "  + MolExporter.exportToFormat(mol, "smiles"));
				int[] results = chemStructServ.searchMolStructures(mol.getMolStructure(), "Compound_Structure", "DUPLICATE_TAUTOMER");
				//logger.info("current mol count is: " + count + "    results: " + results.length);
				
				if (results.length > 0){
					logger.info("############ FOUND A MATCH ######  " + results.length);
					for (int result : results){
						//logger.info("Query mol is : " + MolExporter.exportToFormat(mol, "smiles") + "   query cd_id: " + mol.getProperty("cd_id") + "    Hit is: " + result);	
						Compound target = Compound.findCompoundsByCdId(result).getSingleResult();
						logger.info("Query mol is : " + mol.getSmiles() + "   query cd_id: " + mol.getProperty("cd_id") + "    Hit is: " + target.getExternal_id());	

						outputWriter.append(mol.getProperty("cd_id"));
						outputWriter.append(",");
						outputWriter.append(target.getExternal_id());
						outputWriter.append("\n");
						
					}
				
				}
				
				count++;
				logger.debug("count is " + count);
			}	
			
		
			
		outputWriter.close();

		mi.close();
		
		logger.info(outputWriter.toString());

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


