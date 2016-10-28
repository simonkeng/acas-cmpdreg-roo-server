package com.labsynch.cmpdreg.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.cmpdreg.domain.CorpName;
import com.labsynch.cmpdreg.utils.LoadCompoundsUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class SetCorpNameNumberTest {

	private static final Logger logger = LoggerFactory.getLogger(SetCorpNameNumberTest.class);

    @Test
    public void testMarkerMethod() {
    }

	//@Test
	public void setCorpNumber_1() {
		logger.debug("current corpName number: " + CorpName.countCorpNames());
		
		
		
		for (int i = 0; i < 4800; i++){
			CorpName corpName = new CorpName();
			corpName.persist();
		}

		logger.debug("new corpName number: " + CorpName.countCorpNames());
		
	}

}


