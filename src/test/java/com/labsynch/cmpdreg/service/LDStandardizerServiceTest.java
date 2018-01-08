package com.labsynch.cmpdreg.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.dto.BulkLoadPropertiesDTO;
import com.labsynch.cmpdreg.dto.BulkLoadSDFPropertyRequestDTO;
import com.labsynch.cmpdreg.dto.LDStandardizerActionDTO;
import com.labsynch.cmpdreg.dto.LDStandardizerInputDTO;
import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.utils.Configuration;
import com.labsynch.cmpdreg.utils.SimpleUtil;

import flexjson.JSONSerializer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable

public class LDStandardizerServiceTest {
	
    // private static boolean ldServerURL = Configuration.getConfigInfo().getServerSettings()
	
	private static final Logger logger = LoggerFactory.getLogger(LDStandardizerServiceTest.class);
	
	@Autowired
	private LDStandardizerService ldStandardizerService;
	
	@Test
	@Transactional
	public void ldStandardizerServiceTest() throws MalformedURLException, IOException{
    	LDStandardizerInputDTO ldStandardizerDTO = new LDStandardizerInputDTO();
    	LDStandardizerActionDTO ldStandardizerAction = new LDStandardizerActionDTO();
    	ldStandardizerAction.setName("CLEAN_2D");
    	Collection<LDStandardizerActionDTO> ldStandardizerActions = new ArrayList<LDStandardizerActionDTO>();
    	ldStandardizerActions.add(ldStandardizerAction);
    	ldStandardizerDTO.setActions(ldStandardizerActions);
    	
    	HashMap<String, String> structures = new HashMap<String, String>();
    	structures.put("CMPD-0000001", "\n  Mrv1621 12131714292D          \n\n  9  9  0  0  0  0            999 V2000\n   -1.2202    0.4084    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n   -1.9347   -0.0042    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n   -1.9347   -0.8292    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n   -1.2202   -1.2417    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.5058   -0.8292    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.5058   -0.0042    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.2087    0.4083    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n   -1.2202    1.2334    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n   -1.1097   -0.0042    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n  1  2  1  0  0  0  0\n  2  3  2  0  0  0  0\n  3  4  1  0  0  0  0\n  4  5  2  0  0  0  0\n  5  6  1  0  0  0  0\n  1  6  2  0  0  0  0\n  6  7  1  0  0  0  0\n  1  8  1  0  0  0  0\n  2  9  1  0  0  0  0\nM  END\n$$$$");
    	ldStandardizerDTO.setStructures(structures);
    	
    	ldStandardizerDTO.setAuth_token("nonce");
    	ldStandardizerDTO.setTimeout(10);
    	ldStandardizerDTO.setOutput_format("MOL");
    	
    	logger.info(ldStandardizerDTO.toJson());
		String json = ldStandardizerDTO.toJson();
    	
		String url = "https://mcneilco-standardizer-dev.onschrodinger.com/standardizer/api/v0/standardize";
		String responseJson = SimpleUtil.postRequestToExternalServer(url, json, logger);
		
		logger.info(responseJson);
//		logger.info(mainConfig.getServerSettings().get)
   	
	}


}
