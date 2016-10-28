package com.labsynch.cmpdreg.domain.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.dto.SearchFormDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class SaltFormTest {

	private static final Logger logger = LoggerFactory.getLogger(SaltFormTest.class);

//	@Before
//	public void loginTestUser() {
//		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("cchemist", "cchemist"));
//	}

	@SuppressWarnings("deprecation")
	@Test
	@Transactional
	public void searchSaltFormTest(){

		SearchFormDTO searchParams = new SearchFormDTO();
		Parent parent = Parent.findParent(100036L);
		List<SaltForm> results = SaltForm.findSaltFormsByParentAndMeta(parent, searchParams).getResultList();

		logger.info("number of results found by searchParams and parent: " + results.size());
		
		logger.info("number of results found by parent:" + SaltForm.findSaltFormsByParent(parent).getResultList().size());

		Assert.assertEquals(1, results.size());

	}




}
