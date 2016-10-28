package com.labsynch.cmpdreg.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.SaltForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
@Transactional
public class LotSearchByParentTest {

	private static final Logger logger = LoggerFactory.getLogger(LotSearchByParentTest.class);

	@Before
	public void loginTestUser() {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("cchemist", "cchemist"));
	}


	@Test
	public void searchLotsByParentTest1() {
		long id = 1;
		Parent testParent = Parent.findParent(id);
		logger.debug("test parent: " + testParent.getCorpName()); //CMPD-0001
		List<SaltForm> saltForms = SaltForm.findSaltFormsByParent(testParent).getResultList();
		for (SaltForm saltForm : saltForms){
			List<Lot> lots = Lot.findLotsBySaltForm(saltForm).getResultList();
			for (Lot lot : lots){
				logger.debug("found lot: " + lot.getCorpName());
			}
		}
	}
	
	@Test
	public void searchLotsByParentTest2() {
		long id = 1;
		Parent testParent = Parent.findParent(id);
		logger.debug("test parent: " + testParent.getCorpName()); //CMPD-0001
		List<Lot> lots = Lot.findLotsByParent(testParent).getResultList();
		for (Lot lot : lots){
			logger.debug("found lot: " + lot.getCorpName());
		}
	}	
	
	@Test
	public void countLotsByParentTest3() {
		long id = 1;
		Parent testParent = Parent.findParent(id);
		logger.debug("test parent: " + testParent.getCorpName()); //CMPD-0001
		Long lotCount = Lot.countLotsByParent(testParent);		
		logger.debug("number of lots: " + lotCount); 

	}	
	
}
