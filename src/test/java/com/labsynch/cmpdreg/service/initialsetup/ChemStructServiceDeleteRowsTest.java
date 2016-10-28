package com.labsynch.cmpdreg.service.initialsetup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.service.ChemStructureService;
import com.labsynch.cmpdreg.utils.DeleteAllParents;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class ChemStructServiceDeleteRowsTest {

	private static final Logger logger = LoggerFactory.getLogger(ChemStructServiceDeleteRowsTest.class);

	@Autowired
	private ChemStructureService chemStructServ;


	@Test
	public void dropAllJChemTableRowsTest() {

		chemStructServ.deleteAllJChemTableRows("Salt_Structure");
		chemStructServ.deleteAllJChemTableRows("SaltForm_Structure");
		chemStructServ.deleteAllJChemTableRows("Parent_Structure");
		
//		List<FileList> fileLists = FileList.findAllFileLists();
//		for (FileList fileList : fileLists){
//			fileList.remove();
//		}
//		
//		List<Lot> lots = Lot.findAllLots();
//		for (Lot lot : lots){
//			lot.remove();
//		}
//
//		 List<IsoSalt> isoSalts = IsoSalt.findAllIsoSalts();
//		for (IsoSalt isoSalt : isoSalts){
//			isoSalt.remove();
//		}
//		
//		List<SaltForm> saltForms = SaltForm.findAllSaltForms();
//		for (SaltForm saltForm : saltForms){
//			saltForm.remove();
//		}
//		List<Parent> parents = Parent.findAllParents();
//		for (Parent parent:parents){
//			logger.debug("parent: " + parent.toJson());
//		}
		//Parent.deleteAllParents();
		logger.debug("removed all structures");

	}
	
	//@Test
	@Transactional
	public void dropAllParents() {

//		List<Parent> parents = Parent.findAllParents();
//		for (Parent parent:parents){
//			logger.debug("parent: " + parent.toJson());
//		}
//		Parent.deleteAllParents();
		DeleteAllParents.deleteAllParents();
		logger.debug("removed all structures");

	}


}

//DROP TABLE compound.parent_structure;
//DROP TABLE compound.parent_structure_ul;
//DROP TABLE compound.salt_structure;
//DROP TABLE compound.salt_structure_ul;
//DROP TABLE compound.saltform_structure;
//DROP TABLE compound.saltform_structure_ul;
//DROP TABLE compound.jchemproperties;
//DROP TABLE compound.jchemproperties_cr;

