package com.labsynch.cmpdreg.service;

import java.io.IOException;
import java.util.List;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.QcCompound;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class ChemStructStandardizerServicesTest {

	private static final Logger logger = LoggerFactory.getLogger(ChemStructStandardizerServicesTest.class);

	@Autowired
	private ChemStructureService chemStructServ;

	@Autowired
	private QcCmpdService qcCmpdServ;


	@Autowired
	private ParentService parentServ;

	@Test
	public void molconvertInchiTest() throws IOException, CmpdRegMolFormatException {
		String structure = "CCC";
		String result = chemStructServ.standardizeStructure(structure);
		logger.info(result);
	}


	@Test
	public void getFirstMolTest(){
		Parent testParent = Parent.findParentEntries(1, 1).get(0);
		logger.info(testParent.toJson());
		List<Lot> lots = Lot.findLotsByParent(testParent).getResultList();
		logger.info("found number of lots: " + lots.size());
		Lot queryLot = Lot.findLotByParentAndLotNumber(testParent, 1).getSingleResult();
		logger.info(queryLot.toJson());
	}

	//	@Test
	public void standardizeAllParents() throws CmpdRegMolFormatException, IOException {
		int numberOfParents = parentServ.restandardizeAllParentStructures();
		logger.info("number of parents standardized: " + numberOfParents);
	}

	//	@Test
	public void checkParentDupes(){
		parentServ.findDupeParentStructures(null);
	}

	//@Test
	public void checkPotentialParentDupes(){
		parentServ.findPotentialDupeParentStructures(null);
	}


	//@Test
	//@Transactional
	public void checkMaxRunNumber(){
		Integer result = QcCompound.findMaxRunNumber().getSingleResult();
		if (result == null) logger.info("------------ the result is null!!!");
		logger.info("Max Run number: " + result);
	}


	//@Test
	//@Transactional
	public void qcCheckParents() throws CmpdRegMolFormatException, IOException{
		//		parentServ.qcCheckParentStructures();
		qcCmpdServ.qcCheckParentStructures();
	}

	//@Test
	public void qcDupeCheckParents() throws CmpdRegMolFormatException{
		qcCmpdServ.dupeCheckQCStructures();
	}

	@Test
	public void qcGenerateQCReport() throws IOException, CmpdRegMolFormatException{
		String outputFilePathName = "/tmp/qcReport.csv";
		String exportType = "csv";
		qcCmpdServ.exportQCReport(outputFilePathName, exportType );
		outputFilePathName = "/tmp/qcReport.sdf";
		exportType = "sdf";
		qcCmpdServ.exportQCReport(outputFilePathName, exportType );
	}
	
	//@Test
	@Transactional
	public void resetQCTables(){
		chemStructServ.dropJChemTable("qc_compound_structure");
		//chemStructServ.createJChemTable("qc_compound_structure", true);
		QcCompound.truncateTable();
	}
}
