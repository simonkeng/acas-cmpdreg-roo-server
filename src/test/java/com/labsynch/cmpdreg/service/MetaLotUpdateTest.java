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
public class MetaLotUpdateTest {

	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();

	private static final Logger logger = LoggerFactory.getLogger(MetaLotUpdateTest.class);

	@Autowired
	private MetalotService metalotService;
	
	@Test
	@Transactional
	@Rollback(value=true)
	public void editSaltForm(){
    	
    	String json = "{\"lot\":{\"corpName\":\"CMPD-0000003-HClNaf-01\",\"asDrawnStruct\":null,\"lotMolWeight\":653.725,\"synthesisDate\":\"07/21/2016\",\"color\":\"blue\",\"physicalState\":null,\"notebookPage\":\"E1223\",\"amount\":null,\"amountUnits\":null,\"supplier\":\"\",\"supplierID\":\"\",\"purity\":null,\"purityOperator\":{\"ignore\":false,\"code\":\"=\",\"id\":4,\"name\":\"=\",\"version\":0},\"absorbance\":null,\"percentEE\":null,\"lambda\":null,\"stockSolvent\":\"\",\"stockLocation\":\"\",\"retainLocation\":\"\",\"comments\":\"\",\"chemist\":{\"ignore\":null,\"code\":\"cchemist\",\"id\":2,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Corey Chemist\",\"version\":0},\"isVirtual\":false,\"lotNumber\":null,\"retain\":null,\"retainUnits\":null,\"barcode\":null,\"boilingPoint\":null,\"buid\":21,\"bulkLoadFile\":null,\"id\":21,\"ignore\":null,\"lotAliases\":[],\"lotAsDrawnCdId\":0,\"meltingPoint\":null,\"modifiedBy\":null,\"modifiedDate\":null,\"parent\":{\"bulkLoadFile\":null,\"cdId\":3,\"chemist\":{\"code\":\"cchemist\",\"id\":2,\"ignore\":null,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Corey Chemist\",\"version\":0},\"comment\":\"\",\"commonName\":null,\"compoundType\":null,\"corpName\":\"CMPD-0000003\",\"exactMass\":323.142248,\"id\":3,\"ignore\":null,\"isMixture\":null,\"modifiedBy\":null,\"modifiedDate\":null,\"molFormula\":\"C22H17N3\",\"molWeight\":323.399,\"parentAnnotation\":null,\"parentNumber\":3,\"registeredBy\":null,\"registrationDate\":\"07/18/2016\",\"stereoCategory\":{\"code\":\"scalemic\",\"id\":6,\"name\":\"Scalemic\",\"version\":0},\"stereoComment\":\"\",\"version\":0},\"project\":null,\"purityMeasuredBy\":null,\"registeredBy\":{\"code\":\"cchemist\",\"id\":2,\"ignore\":null,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Corey Chemist\",\"version\":0},\"registrationDate\":\"07/21/2016\",\"saltForm\":{\"corpName\":\"CMPD-0000003-HClNaf\",\"chemist\":null,\"casNumber\":\"\",\"id\":42,\"molStructure\":\"\",\"parent\":{\"stereoCategory\":{\"ignore\":false,\"code\":\"scalemic\",\"id\":6,\"name\":\"Scalemic\",\"version\":0},\"stereoComment\":\"\",\"compoundType\":null,\"parentAnnotation\":null,\"comment\":\"\",\"corpName\":\"CMPD-0000003\",\"chemist\":{\"ignore\":null,\"code\":\"cchemist\",\"id\":2,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Corey Chemist\",\"version\":0},\"isMixture\":false,\"id\":3,\"commonName\":null,\"exactMass\":323.142248,\"parentAliases\":[],\"registrationDate\":\"07/18/2016\",\"cdId\":3,\"parentNumber\":3}},\"solutionAmount\":null,\"solutionAmountUnits\":null,\"supplierLot\":null,\"vendor\":null,\"version\":6},\"fileList\":[],\"isosalts\":[{\"equivalents\":5,\"type\":\"salt\",\"id\":36,\"salt\":{\"name\":\"HCl\",\"abbrev\":\"HCl\",\"cdId\":1,\"charge\":0,\"formula\":\"ClH\",\"id\":1,\"ignore\":null,\"molWeight\":36.46,\"version\":0},\"isotope\":null},{\"equivalents\":2,\"type\":\"salt\",\"id\":34,\"salt\":{\"name\":\"Sodium\",\"abbrev\":\"Na\",\"cdId\":3,\"charge\":0,\"formula\":\"HNa\",\"id\":3,\"ignore\":null,\"molWeight\":23.998,\"version\":0},\"isotope\":null},{\"equivalents\":5,\"type\":\"salt\",\"id\":35,\"salt\":{\"name\":\"f\",\"abbrev\":\"f\",\"cdId\":2,\"charge\":0,\"formula\":\"FH\",\"id\":2,\"ignore\":null,\"molWeight\":20.006,\"version\":0},\"isotope\":null}]}";
    	Metalot metaLot = Metalot.fromJsonToMetalot(json);
    	MetalotReturn mr = metalotService.save(metaLot);
    	logger.info(mr.toJson());
	}

	
}
