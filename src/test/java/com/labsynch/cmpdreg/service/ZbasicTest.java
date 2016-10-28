package com.labsynch.cmpdreg.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.cmpdreg.domain.FileList;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class ZbasicTest {

	private static final Logger logger = LoggerFactory.getLogger(ZbasicTest.class);
	

//	@Test
	public void simpleTest() {
		logger.debug("just get a fileList: ");
		 FileList fileList = FileList.findFileList(10L);
		 logger.debug("found one: " + fileList.getName());
		 fileList.remove();
		 logger.debug("deleted it: " );
	}

	@Test
	public void specialCharTest(){
		String inputString = "SMILES^@";
		logger.info("input string: " + inputString);
		String newString = inputString.replaceAll("(\\x00|\\^M|\\^\\@)$", "");
		logger.info("cleaned input string: " + newString);
		
	}
	
}


