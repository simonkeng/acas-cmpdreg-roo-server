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

import com.labsynch.cmpdreg.domain.CorpName;
import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.SaltForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class LotCorpNameTest {

	private static final Logger logger = LoggerFactory.getLogger(LotCorpNameTest.class);

	@Before
	public void loginTestUser() {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("cchemist", "cchemist"));
	}

	@Test
	public void checkConvertLotName(){
		String corpName = "CMPD-0650-0-01";

		String convertedName = CorpName.convertLotToSaltFormCorpName(corpName);
		logger.debug("found convertedName: " + convertedName);
	}

	@Test
	public void checkConvertAltName(){
		String corpName = "CMPD-0650-0-01";
		logger.debug("checking:: " + corpName);
		logger.debug("looks like a number: " + CorpName.checkCorpNumber(corpName));
		
		if (CorpName.checkLotCorpName(corpName)){
			logger.debug("found a lot");
		} else if (CorpName.checkSaltFormCorpName(corpName)){
			logger.debug("found a salt Form");
		} else {
			logger.debug("looks like alt corp name: " + CorpName.checkParentCorpNameAlt(corpName));
			corpName = CorpName.removeCorpNamePrefix(corpName);
			corpName = CorpName.convertCorpNameNumber(corpName);
			Long maxParent = CorpName.convertCorpNameToNumber(corpName);	
			logger.debug("parentTo: " + maxParent);				  
		}


	}

	//@Test
	public void checkStringName(){
		String corpName = "gibberish";
		if(CorpName.checkStringName(corpName)){
			logger.debug("found string corp name: " + corpName);
		} else {
			logger.debug("did not find it");
		}
	}

	//@Test
	public void convertCorpName(){
		String corpName = "cmpd2";
		if(CorpName.checkParentCorpNameAlt(corpName)){
			logger.debug("found alt corp name");
			corpName = CorpName.removeCorpNamePrefix(corpName);
			logger.debug("after removal: " + corpName);
		} else {
			logger.debug("did not find it");
		}
	}

	//@Test
	public void countNonVirtualLots(){


		long saltFormId = 6l;
		boolean isVirtual = true;
		SaltForm saltForm = SaltForm.findSaltForm(saltFormId);
		long lotCount = Lot.countNonVirtualSaltFormLots(saltForm, isVirtual);
		logger.debug("number of non-virtual lots: " + lotCount);

		List<Lot> lots = Lot.findLotsBySaltForm(saltForm).getResultList();
		for (Lot lot : lots){
			logger.debug(lot.getCorpName());
		}

	}

	//@Test
	public void generateCorpName() {
		//simple test to generate a new corpName
		String corpName = "CMPD-0001";

		List<Parent> parents = Parent.findParentsByCorpNameEquals(corpName).getResultList();
		System.out.println("number of parents found: " + parents.size());
		Parent parent = null;
		if (parents.size() > 0) {
			parent = parents.get(0);

			List<SaltForm> saltForms = SaltForm.findSaltFormsByParent(parent).getResultList();
			System.out.println("number of saltForms found: " + saltForms.size());
			SaltForm saltForm = saltForms.get(0);

			corpName = saltForm.getCorpName();

			List<Lot> lots = Lot.findLotsBySaltForm(saltForm).getResultList();
			int lotCount = lots.size();

			int lotNumber = lotCount + 1;

			corpName = corpName.concat(CorpName.separator).concat(String.format("%02d", lotNumber));

			System.out.println("corpName: " + corpName);

		}


	}




}
