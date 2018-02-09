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
import com.labsynch.cmpdreg.exceptions.StandardizerException;

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
	public void molconvertInchiTest() throws IOException, CmpdRegMolFormatException, StandardizerException {
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

}
