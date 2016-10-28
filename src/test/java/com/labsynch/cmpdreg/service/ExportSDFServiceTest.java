package com.labsynch.cmpdreg.service;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import chemaxon.formats.MolExporter;
import chemaxon.formats.MolFormatException;
import chemaxon.marvin.io.MolExportException;
import chemaxon.struc.Molecule;
import chemaxon.util.MolHandler;

import com.labsynch.cmpdreg.dto.SearchCompoundReturnDTO;
import com.labsynch.cmpdreg.utils.MoleculeUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class ExportSDFServiceTest {

	Logger logger = LoggerFactory.getLogger(ExportSDFServiceTest.class);


	@Autowired
	private StructureImageService structureImageService;

	//@Test
	public void exportSDFileTest(){

		String testJSON = "[{\"corpName\":\"CMPD-0001-0\",\"lotIDs\":[{\"corpName\":\"CMPD-0001-0-01\",\"lotNumber\":1,\"registrationDate\":\"12/10/2011\",\"synthesisDate\":\"12/10/2011\"}],\"molStructure\":\"CCCCCC\",\"stereoCategoryName\":null,\"stereoComment\":null},{\"corpName\":\"CMPD-0002-0\",\"lotIDs\":[{\"corpName\":\"CMPD-0002-0-01\",\"lotNumber\":1,\"registrationDate\":\"12/10/2011\"}],\"molStructure\":\"CCCCCCCC\",\"stereoCategoryName\":null,\"stereoComment\":null}]";
		String output = structureImageService.convertMolfilesToSDFile(testJSON);
		logger.debug(output);

	}

	//@Test
	public void convertJSONToArrayTest(){

		String testJSON = "[{\"corpName\":\"CMPD-0001-0\",\"lotIDs\":[{\"corpName\":\"CMPD-0001-0-01\",\"lotNumber\":1,\"registrationDate\":\"12/10/2011\",\"synthesisDate\":\"12/10/2011\"}],\"molStructure\":\"CCCCCC\",\"stereoCategoryName\":null,\"stereoComment\":null},{\"corpName\":\"CMPD-0002-0\",\"lotIDs\":[{\"corpName\":\"CMPD-0002-0-01\",\"lotNumber\":1,\"registrationDate\":\"12/10/2011\"}],\"molStructure\":\"CCCCCCCC\",\"stereoCategoryName\":null,\"stereoComment\":null}]";
		logger.debug(testJSON);

		Collection<SearchCompoundReturnDTO> compounds = SearchCompoundReturnDTO.fromJsonArrayToSearchCompoes(testJSON);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		MolHandler mh = null;
		MolExporter molExporter = null;
		try {
			molExporter = new MolExporter(outputStream, "sdf");
		} catch (MolExportException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		for (SearchCompoundReturnDTO compound : compounds){
			logger.debug("compound: " + compound.getCorpName());
			try {
				mh = new MolHandler(compound.getMolStructure());
				Molecule mol = mh.getMolecule();
				logger.debug("here is the imported mol: " + MoleculeUtil.exportMolAsText(mol, "smiles"));
				mol.setProperty("corpName", compound.getCorpName());
				mol.setProperty("stereoCategoryName", compound.getStereoCategoryName());
				mol.setProperty("stereoComment", compound.getStereoComment());
				mol.setProperty("lotIDs", compound.getLotIDs().toString());
				molExporter.write(mol);

			} catch (MolFormatException e) {
				System.out.println("bad structure error: " + compound.getMolStructure());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		try {
			molExporter.close();
			outputStream.close();
		} catch (MolExportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("output SDF: " + outputStream.toString());
	}
	
	@Test
	public void convertMolToImageTest(){
		
		
		
	}

}
