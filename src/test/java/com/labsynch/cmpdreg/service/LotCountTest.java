package com.labsynch.cmpdreg.service;

import java.util.List;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class LotCountTest {

	private static final Logger logger = LoggerFactory.getLogger(LotCountTest.class);

//	@Before
//	public void loginTestUser() {
//		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("cchemist", "cchemist"));
//	}

	@Test
	@Transactional
	public void countLotBySaltFormTest(){
		String corpName = "CMPD-587526";
		List<SaltForm> saltForms = SaltForm.findSaltFormsByCorpNameEquals(corpName).getResultList();
		logger.debug("total number of saltForms found: " + saltForms.size());
		
		Parent parent2 = Parent.findParent(5110L);
		logger.debug("here is paren2: " + parent2.getCorpName());
		Integer maxParentLot2 = Lot.getMaxParentLotNumber(parent2);
		logger.debug("max lot number by parent2: " + maxParentLot2);	
		
		
		if (saltForms.size() > 1){
			SaltForm saltForm = saltForms.get(0);
			if (saltForm != null){
				logger.debug("here is the saltForm: " + saltForm.getCorpName());
			} else {
				logger.debug("saltForm is null");
			}
			
			Integer maxSaltFormLot = Lot.getMaxSaltFormLotNumber(saltForm);
			logger.debug("max lot number by saltForm: " + maxSaltFormLot);


			Parent parent = saltForm.getParent();
			logger.debug("here is the parent: " + parent.getCorpName());

			//broken -- need to fix
			Integer maxParentLot = Lot.getMaxParentLotNumber(parent);
			logger.debug("max lot number by parent: " + maxParentLot);			
		}



	}




}
