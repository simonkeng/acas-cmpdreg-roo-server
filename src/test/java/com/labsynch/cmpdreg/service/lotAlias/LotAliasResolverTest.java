package com.labsynch.cmpdreg.service.lotAlias;

import java.util.HashSet;
import java.util.Set;

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
import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.Salt;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.service.CorpNameService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
@Transactional
public class LotAliasResolverTest {

	private static final Logger logger = LoggerFactory.getLogger(LotAliasResolverTest.class);

	@Autowired
	private CorpNameService corpNameService;

	@Test
	public void lotAlias_test1() {
		//get lot preferred name -- official Lot CorpName when queried with BUID
		// BUID format = 123 or BUID1059
		// to test via curl: curl -i -X POST -H Accept:application/text -H Content-Type:application/text -d 'BUID1059, buid-1062' http://localhost:8080/cmpdreg/api/compound/convertBuid
		
		String aliasList = "BUID1059, buid-1062, 32,899,777,1064,, 1044,1050,1057, 1059";
		String outputString = corpNameService.getPreferredNameFromBuid(aliasList);

		logger.debug("post outputString: " + outputString);

	}

	//@Test
	@Transactional
	public void lotName_test1() {
		//get lot preferred name -- official Lot CorpName when queried with BUID
		// BUID format = 123 or BUID1059
		// to test via curl: curl -i -X POST -H Accept:application/text -H Content-Type:application/text -d 'BUID1059, buid-1062' http://localhost:8080/cmpdreg/api/compound/convertBuid
		
		Lot lot = new Lot();
		lot.setIsVirtual(false);
		lot.setIgnore(false);
		lot.setLotNumber(1);
		Parent parent = new Parent();
		parent.setCorpName("TESTNAME");
		parent.persist();
		SaltForm saltForm = new SaltForm();
		IsoSalt isoSalt = new IsoSalt();
		Salt salt = new Salt();
		salt.setAbbrev("B");
		isoSalt.setSalt(salt);
		isoSalt.setType("salt");
		Set<IsoSalt> isoSalts = new HashSet<IsoSalt>();
		isoSalts.add(isoSalt);
		saltForm.setIsoSalts(isoSalts);
		saltForm.setParent(parent);
		Set<SaltForm> saltForms = new HashSet<SaltForm>();
		saltForms.add(saltForm);
		lot.setSaltForm(saltForm);
		String lotCorpName = lot.generateCorpName();

		logger.debug("lotCorpName: " + lotCorpName);
		logger.debug("lot: " + lot.toJson());

	}

}
