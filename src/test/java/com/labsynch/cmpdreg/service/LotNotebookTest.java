package com.labsynch.cmpdreg.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.Lot;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class LotNotebookTest {

	private static final Logger logger = LoggerFactory.getLogger(LotNotebookTest.class);

//	@Before
//	public void loginTestUser() {
//		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("cchemist", "cchemist"));
//	}

	@Test
	@Transactional
	public void countLotByNotebookTest(){
		String notebookPage = "NOTEBOOK-LB-0001-001";
		List<Lot> lots = Lot.findLotsByNotebookPageEquals(notebookPage).getResultList();
		logger.debug("number of lots: " + lots.size());

	}




}
