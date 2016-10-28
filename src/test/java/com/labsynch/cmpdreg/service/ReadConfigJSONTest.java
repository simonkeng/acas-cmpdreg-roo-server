package com.labsynch.cmpdreg.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.utils.Configuration;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class ReadConfigJSONTest {

	private static final Logger logger = LoggerFactory.getLogger(ReadConfigJSONTest.class);
	
	@Test
	public void loadConfigTest(){
		
		MainConfigDTO mainConfig = Configuration.getConfigInfo();
		
		logger.debug("base Server URL: " + mainConfig.getServerConnection().getBaseServerURL());
		
		logger.debug("converted configration JSON line: " + mainConfig.toJson());

		logger.debug("saltBeforeLot: " + mainConfig.getMetaLot().isSaltBeforeLot());
		
		logger.debug("corpPrefix: " + mainConfig.getServerSettings().getCorpPrefix());
		
		logger.debug("isSaltBeforeLot: " + mainConfig.getMetaLot().isSaltBeforeLot());

	}


}


