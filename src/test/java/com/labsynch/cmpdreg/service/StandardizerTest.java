package com.labsynch.cmpdreg.service;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;
import com.labsynch.cmpdreg.exceptions.StandardizerException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
@Transactional
public class StandardizerTest {
	@Autowired
	private StandardizationService standardizationService;
	
	@Transactional
	@Rollback(false)
	@Test
	public void getStandardizationDryRunReport() throws IOException, CmpdRegMolFormatException, StandardizerException {
		

//		int json = standardizationService.executeStandardization();
//		System.out.println(json);

//		String json = standardizationService.getDryRunStats();
//		System.out.println(json);
		
//		String json = standardizationService.executeStandardization();
//		System.out.println(json);
		standardizationService.executeDryRun();
	}

}
