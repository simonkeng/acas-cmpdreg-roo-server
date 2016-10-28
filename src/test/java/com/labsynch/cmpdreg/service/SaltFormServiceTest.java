package com.labsynch.cmpdreg.service;

import java.util.HashSet;
import java.util.Set;

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

import com.labsynch.cmpdreg.domain.IsoSalt;
import com.labsynch.cmpdreg.domain.SaltForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class SaltFormServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(SaltFormServiceTest.class);
	
	@Autowired
	private SaltFormService saltFormService;

	@Transactional
	@Test
	public void compareSaltForms(){
		SaltForm saltForm = SaltForm.findSaltFormEntries(0, 1).get(0);
		SaltForm testSaltForm = new SaltForm();
		Set<IsoSalt> testIsoSalts = new HashSet<IsoSalt>();
		for (IsoSalt isoSalt : saltForm.getIsoSalts()){
			IsoSalt testIsoSalt = new IsoSalt();
			testIsoSalt.setType(isoSalt.getType());
			testIsoSalt.setSalt(isoSalt.getSalt());
			testIsoSalt.setIsotope(isoSalt.getIsotope());
			testIsoSalt.getEquivalents();
			testIsoSalts.add(testIsoSalt);
		}
		testSaltForm.setIsoSalts(testIsoSalts);
		
		Assert.assertTrue(SaltFormServiceImpl.checkIsoSaltSetsSame(saltForm.getIsoSalts(), testIsoSalts));
	}

}
