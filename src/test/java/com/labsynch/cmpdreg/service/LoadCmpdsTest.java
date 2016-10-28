package com.labsynch.cmpdreg.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.cmpdreg.utils.LoadCompoundsUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class LoadCmpdsTest {

	private static final Logger logger = LoggerFactory.getLogger(LoadCmpdsTest.class);
	
	@Autowired
	private LoadCompoundsUtil lcu;

	@Test
	public void loadCmpds_1() {
		//test simple utility to load compounds
		String inputfileName = "src/test/resources/cmpd_sample5.sdf.sdf";
		String errorFileName = "src/test/resources/cmpd_sample5_errorMols.sdf"; 
		
		lcu.loadCompounds(inputfileName, errorFileName);		
	}

}


