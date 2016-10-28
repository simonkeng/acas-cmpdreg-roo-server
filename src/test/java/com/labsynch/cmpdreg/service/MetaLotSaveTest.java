package com.labsynch.cmpdreg.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.FileList;
import com.labsynch.cmpdreg.domain.IsoSalt;
import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.LotAlias;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.Salt;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.dto.Metalot;
import com.labsynch.cmpdreg.dto.MetalotReturn;
import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.utils.Configuration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml"})
@Configurable
public class MetaLotSaveTest {

	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();

	private static final Logger logger = LoggerFactory.getLogger(MetaLotSaveTest.class);

	@Autowired
	private MetalotService metalotService;

	@Test
	public void hydrateMetLotJSON() {

		String json = "{\"lot\":{\"corpName\":\"\",\"asDrawnStruct\":\"CCCC\",\"lotMolWeight\":null,\"synthesisDate\":\"10/15/2012\",\"registrationDate\":null,\"color\":\"\",\"physicalState\":{\"code\":\"solid\",\"id\":1,\"name\":\"solid\",\"version\":0},\"notebookPage\":\"A1234-044\",\"amount\":22,\"amountUnits\":{\"code\":\"mg\",\"id\":26,\"name\":\"mg\",\"version\":0},\"solutionAmount\":null,\"solutionAmountUnits\":{\"code\":\"uL\",\"id\":42,\"name\":\"uL\",\"version\":0},\"vendor\":{\"code\":\"notSpecified\",\"id\":46,\"name\":\"Not Specified\",\"version\":0},\"purity\":null,\"purityMeasuredBy\":{\"code\":\"HPLC\",\"id\":22,\"name\":\"HPLC\",\"version\":0},\"purityOperator\":{\"code\":\"=\",\"id\":16,\"name\":\"=\",\"version\":0},\"percentEE\":null,\"comments\":\"\",\"chemist\":{\"code\":\"cchemist\",\"id\":31,\"ignore\":null,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Corey Chemist\",\"version\":0},\"project\":{\"code\":\"Project 1\",\"id\":19,\"name\":\"Project 1\",\"version\":0},\"supplierLot\":null,\"meltingPoint\":null,\"buid\":null,\"isVirtual\":false,\"registeredBy\":{\"code\":\"bbiologist\",\"id\":32,\"ignore\":false,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Ben Biologist\",\"version\":1},\"saltForm\":{\"corpName\":\"\",\"chemist\":{\"code\":\"cchemist\",\"id\":31,\"ignore\":null,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Corey Chemist\",\"version\":0},\"casNumber\":\"\",\"molStructure\":\"\"},\"parent\":{\"stereoCategory\":{\"code\":\"scalemic\",\"id\":7,\"name\":\"Scalemic\",\"version\":0},\"stereoComment\":\"\",\"commonName\":\"\",\"molStructure\":\"CCCC\",\"corpName\":\"\",\"chemist\":{\"code\":\"cchemist\",\"id\":31,\"ignore\":null,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Corey Chemist\",\"version\":0}}},\"fileList\":[],\"isosalts\":[]}";
				
		logger.debug("initial json: " + json);
		
		Metalot metaLot = null;		
		try {
			metaLot = Metalot.fromJsonToMetalot(json);				
			logger.debug("metaLot: " + metaLot.toString());
			logger.debug("json metaLot: " + metaLot.toJson());
			logger.debug("lot parent: " + metaLot.getLot().getParent());
			
			MetalotReturn mr = metalotService.save(metaLot);
			mr.toJson();
		} catch (Exception e){
			logger.error("Unable to parse metaLot JSON. " + e);
		}
	}

	//@Test
	public void saveMetalotCalculateLotWeigth(){

	String json = "{\"lot\":{\"corpName\":\"\",\"asDrawnStruct\":\"CCCCC\",\"lotMolWeight\":null,\"synthesisDate\":\"02/07/2012\",\"color\":\"\",\"physicalState\":{\"code\":\"solid\",\"id\":1,\"name\":\"solid\",\"version\":0},\"notebookPage\":\"EPI-LB-0001-789\",\"amount\":null,\"amountUnits\":{\"code\":\"mg\",\"id\":26,\"name\":\"mg\",\"version\":0},\"supplier\":\"\",\"supplierID\":\"\",\"purity\":null,\"purityMeasuredBy\":{\"code\":\"HPLC\",\"id\":22,\"name\":\"HPLC\",\"version\":0},\"purityOperator\":{\"code\":\"=\",\"id\":16,\"name\":\"=\",\"version\":0},\"percentEE\":null,\"comments\":\"\",\"chemist\":{\"code\":\"cchemist\",\"id\":31,\"ignore\":null,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Corey Chemist\",\"version\":0},\"project\":{\"code\":\"Project 1\",\"id\":19,\"name\":\"Project 1\",\"version\":0},\"supplierLot\":\"\",\"meltingPoint\":null,\"boilingPoint\":null,\"buid\":null,\"isVirtual\":false,\"registeredBy\":{\"code\":\"cchemist\",\"id\":31,\"ignore\":null,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Corey Chemist\",\"version\":0},\"saltForm\":{\"corpName\":\"\",\"chemist\":{\"code\":\"cchemist\",\"id\":31,\"ignore\":null,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Corey Chemist\",\"version\":0},\"casNumber\":\"\",\"molStructure\":\"\"},\"parent\":{\"stereoCategory\":{\"code\":\"scalemic\",\"id\":7,\"name\":\"Scalemic\",\"version\":0},\"stereoComment\":\"\",\"commonName\":\"\",\"molStructure\":\"CCCCC\",\"corpName\":\"\",\"chemist\":{\"code\":\"cchemist\",\"id\":31,\"ignore\":null,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Corey Chemist\",\"version\":0}}},\"fileList\":[],\"isosalts\":[]}"; 
			
		Metalot metaLot = Metalot.fromJsonToMetalot(json);
		logger.debug("json metaLot: " + metaLot.toJson());
		logger.debug("lot buid: " + metaLot.getLot().getBuid());
		logger.debug("lot parent: " + metaLot.getLot().getParent());
		
		MetalotReturn mr = metalotService.save(metaLot);
		mr.toJson();

	}
	
	//@Test
	public void saveVirtualLot(){

	String json = "{\"lot\":{\"corpName\":\"\",\"asDrawnStruct\":\"CC1=C(C)\",\"lotMolWeight\":null,\"synthesisDate\":null,\"color\":\"\",\"physicalState\":null,\"notebookPage\":null,\"amount\":null,\"amountUnits\":null,\"supplier\":\"\",\"supplierID\":\"\",\"purity\":null,\"purityMeasuredBy\":null,\"purityOperator\":null,\"percentEE\":null,\"comments\":\"\",\"chemist\":{\"code\":\"cchemist\",\"id\":31,\"ignore\":null,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Corey Chemist\",\"version\":0},\"project\":{\"code\":\"Project 1\",\"id\":19,\"name\":\"Project 1\",\"version\":0},\"supplierLot\":\"\",\"meltingPoint\":null,\"boilingPoint\":null,\"buid\":null,\"isVirtual\":true,\"registeredBy\":{\"code\":\"cchemist\",\"id\":31,\"ignore\":null,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Corey Chemist\",\"version\":0},\"saltForm\":{\"corpName\":\"\",\"chemist\":{\"code\":\"cchemist\",\"id\":31,\"ignore\":null,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Corey Chemist\",\"version\":0},\"casNumber\":\"\",\"molStructure\":\"\"},\"parent\":{\"stereoCategory\":{\"code\":\"scalemic\",\"id\":7,\"name\":\"Scalemic\",\"version\":0},\"stereoComment\":\"\",\"commonName\":\"\",\"molStructure\":\"CC1=C(C)\",\"corpName\":\"\",\"chemist\":{\"code\":\"cchemist\",\"id\":31,\"ignore\":null,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Corey Chemist\",\"version\":0}}},\"fileList\":[],\"isosalts\":[]}";
			
		Metalot metaLot = Metalot.fromJsonToMetalot(json);
		Lot mlot = metaLot.getLot();
		mlot.setIsVirtual(true);
		metaLot.setLot(mlot);
		
		if (mainConfig.getServerSettings().isUnitTestDB()){
			MetalotReturn mr = metalotService.save(metaLot);
			logger.debug("return response: " + mr.toJson());
			logger.debug("json metaLot: " + metaLot.toJson());
			logger.debug("lot buid: " + metaLot.getLot().getBuid());
			logger.debug("lot parent: " + metaLot.getLot().getParent());
			logger.debug("lot virtual: " + metaLot.getLot().getIsVirtual());
			logger.debug("lot notebook: " + metaLot.getLot().getNotebookPage());
			
			if(mr.getMetalot() != null){
				Lot lot = mr.getMetalot().getLot();
				Parent parent = mr.getMetalot().getLot().getParent();
				logger.debug("Lot id = " + lot.getId());
				Assert.assertTrue(lot.getId() > 0);
				lot.remove();
				parent.remove();				
			}
		}
		
		
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
	public void editLotAliases(){
		
    	Metalot metaLot = new Metalot();
		Lot lot = Lot.findLotEntries(0, 1).get(0);
    	metaLot.setLot(lot);
    	SaltForm saltForm = SaltForm.findSaltForm(metaLot.getLot().getSaltForm().getId());
    	List<IsoSalt> isoSalts = IsoSalt.findIsoSaltsBySaltForm(saltForm).getResultList();
    	metaLot.getIsosalts().addAll(isoSalts);
    	List<FileList> fileLists = FileList.findFileListsByLot(lot).getResultList();
    	metaLot.getFileList().addAll(fileLists);
    	
		LotAlias lotAlias = new LotAlias(metaLot.getLot(), "default", "default", "LOT-1234", false);
    	if (metaLot.getLot().getLotAliases() == null){
    		Set<LotAlias> lotAliases = new HashSet<LotAlias>();
    		lotAliases.add(lotAlias);
    		metaLot.getLot().setLotAliases(lotAliases);
    	}else{
    		metaLot.getLot().getLotAliases().add(lotAlias);
    	}
    	MetalotReturn mr = metalotService.save(metaLot);
    	logger.info(mr.toJson());

	}
	
	@Test
	@Transactional
	@Rollback(value=true)
	public void editSaltForm(){
		Metalot metaLot = new Metalot();
		Lot lot = Lot.findLotEntries(0, 1).get(0);
    	metaLot.setLot(lot);
    	SaltForm saltForm = SaltForm.findSaltForm(metaLot.getLot().getSaltForm().getId());
    	List<IsoSalt> isoSalts = IsoSalt.findIsoSaltsBySaltForm(saltForm).getResultList();
    	metaLot.getIsosalts().addAll(isoSalts);
    	List<FileList> fileLists = FileList.findFileListsByLot(lot).getResultList();
    	metaLot.getFileList().addAll(fileLists);
    	
    	IsoSalt newIsoSalt = new IsoSalt();
    	newIsoSalt.setEquivalents(1.0);
    	newIsoSalt.setType("salt");
    	newIsoSalt.setSalt(Salt.findSaltEntries(0, 1).get(0));
    	metaLot.getIsosalts().add(newIsoSalt);
    	
    	MetalotReturn mr = metalotService.save(metaLot);
    	logger.info(mr.toJson());
	}

	
}
