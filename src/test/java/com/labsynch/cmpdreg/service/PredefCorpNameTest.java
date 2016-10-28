package com.labsynch.cmpdreg.service;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.cmpdreg.domain.PreDef_CorpName;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class PredefCorpNameTest {

	private static final Logger logger = LoggerFactory.getLogger(PredefCorpNameTest.class);


	@Test
	public void testPredefCorpName() throws FileNotFoundException {
		PreDef_CorpName preDefCorpName =  PreDef_CorpName.findNextCorpName();
		logger.debug("found this corpName: " + preDefCorpName.toJson());
		preDefCorpName.setUsed(true);
		preDefCorpName.merge();
		logger.debug("found this corpName: " + preDefCorpName.toJson());


	}



}


