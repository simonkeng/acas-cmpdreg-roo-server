package com.labsynch.cmpdreg.service;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.cmpdreg.dto.MolConvertOutputDTO;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class ChemStructServicesTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ChemStructServicesTest.class);

	@Autowired
	private ChemStructureService chemStructServ;
	
	//@Test
    public void molconvertSmilesTest() throws IOException, CmpdRegMolFormatException {
		String structure = "CCC";
		String inputFormat = "smiles";
		String outputFormat = "mol";
		MolConvertOutputDTO result = chemStructServ.toFormat(structure, inputFormat, outputFormat);
		logger.debug(result.toJson());
    }
	
	@Test
    public void molconvertInchiTest() throws IOException, CmpdRegMolFormatException {
		String structure = "CCC";
		String inputFormat = "smiles";
		String outputFormat = "inchi";
		MolConvertOutputDTO result = chemStructServ.toFormat(structure, inputFormat, outputFormat);
		logger.debug(result.toJson());
    }
     

}
