package com.labsynch.cmpdreg.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.cmpdreg.utils.Configuration;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class ConfigJsonTest {

	private static final Logger logger = LoggerFactory.getLogger(ConfigJsonTest.class);
	

	@Test
	public void simpleTest3() {
		logger.info("simple test3 ----- just get the config full config file: " + Configuration.getConfigInfo());
		
	}
	

}


