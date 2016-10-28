package com.labsynch.cmpdreg.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.CorpName;
import com.labsynch.cmpdreg.domain.IsoSalt;
import com.labsynch.cmpdreg.domain.Isotope;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.Salt;
import com.labsynch.cmpdreg.domain.SaltForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class SaltFormCorpNameTest {

//	@Test
	@Transactional
	public void generateCorpName() {
		//simple test to generate metalot test load object for John
		String corpName = "CMPD-0001";

		List<Parent> parents = Parent.findParentsByCorpNameEquals(corpName).getResultList();
		System.out.println("number of parents found: " + parents.size());
		Parent parent = parents.get(0);

		List<SaltForm> saltForms = SaltForm.findSaltFormsByParent(parent).getResultList();
		System.out.println("number of saltForms found: " + saltForms.size());
		SaltForm saltForm = saltForms.get(0);

		List<IsoSalt> isoSalts = IsoSalt.findIsoSaltsBySaltForm(saltForm).getResultList();
		System.out.println("number of isoSalts found: " + isoSalts.size());

		List<IsoSalt> isotopes = IsoSalt.findIsoSaltsBySaltFormAndType(saltForm, "isotope").getResultList();
		System.out.println("number of isotopes found: " + isotopes.size());

		ArrayList<String> isotopeAbbrevs = new ArrayList<String>();
		if (isotopes.size() > 0){
			for (IsoSalt iso : isotopes){
				Isotope isotope = Isotope.findIsotope(iso.getIsotope().getId());
				String abbrev = isotope.getAbbrev();
				isotopeAbbrevs.add(abbrev);
			}
			Collections.sort(isotopeAbbrevs);
		}


		List<IsoSalt> salts = IsoSalt.findIsoSaltsBySaltFormAndType(saltForm, "salt").getResultList();
		System.out.println("number of salts found: " + salts.size());
		ArrayList<String> saltAbbrevs = new ArrayList<String>();
		if (salts.size() > 0){
			for (IsoSalt isoSalt : salts){
				Salt salt = Salt.findSalt(isoSalt.getSalt().getId());
				String abbrev = salt.getAbbrev();
				saltAbbrevs.add(abbrev);
			}	    	
			Collections.sort(saltAbbrevs);
		}
		
		String saltFormName = "";
		System.out.println("length: " + saltFormName.length());

		for (String abbrev : isotopeAbbrevs){
			saltFormName = saltFormName.concat(abbrev);
		}
		for (String abbrev : saltAbbrevs){
			saltFormName = saltFormName.concat(abbrev);
		}
		System.out.println("length: " + saltFormName.length());
		
		
		
		corpName = corpName.concat(CorpName.separator).concat(saltFormName);
		System.out.println("corpName: " + corpName);



	}
	
	//@Test
	public void testCorpNameGenerator(){

		String corpName = "CMPD-0001";

		List<Parent> parents = Parent.findParentsByCorpNameEquals(corpName).getResultList();
		System.out.println("number of parents found: " + parents.size());
		Parent parent = parents.get(0);

		List<SaltForm> saltForms = SaltForm.findSaltFormsByParent(parent).getResultList();
		System.out.println("number of saltForms found: " + saltForms.size());
		SaltForm saltForm = saltForms.get(0);
		
		String saltCorpName = CorpName.generateSaltFormCorpName(parent.getCorpName(), saltForm.getIsoSalts());
		System.out.println("saltCorpName: " + saltCorpName);

		
	}

    @Test
    public void testMarkerMethod() {
        org.junit.Assert.assertTrue(true);
    }
}
