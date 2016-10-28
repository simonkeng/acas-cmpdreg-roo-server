package com.labsynch.cmpdreg.dto;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/applicationContext.xml","/META-INF/spring/applicationContext-security.xml" })
public class PreferredNameDTOTest {
	
	@Test
	@Transactional
	public void getPreferredName(){
		PreferredNameDTO one = new PreferredNameDTO();
		one.setRequestName("CMPD-0000001-001");
		PreferredNameDTO two = new PreferredNameDTO();
		two.setRequestName("CMPD-0000002-001");
		PreferredNameDTO three = new PreferredNameDTO();
		three.setRequestName("Not-A-Lot-Corp-Name");
		Collection<PreferredNameDTO> preferredNameDTOs = new ArrayList<PreferredNameDTO>();
		preferredNameDTOs.add(one);
		preferredNameDTOs.add(two);
		preferredNameDTOs.add(three);
		preferredNameDTOs = PreferredNameDTO.getPreferredNames(preferredNameDTOs);
		for (PreferredNameDTO check : preferredNameDTOs){
			if (check.getRequestName().equals("CMPD-0000001-001")) Assert.assertEquals(check.getRequestName(), check.getPreferredName());
			if (check.getRequestName().equals("CMPD-0000002-001")) Assert.assertEquals(check.getRequestName(), check.getPreferredName());
			if (check.getRequestName().equals("Not-A-Lot-Corp-Name")) Assert.assertEquals("", check.getPreferredName());
		}
	}
	
	@Test
	@Transactional
	public void getParentPreferredName(){
		PreferredNameDTO one = new PreferredNameDTO();
		one.setRequestName("CMPD-0000001");
		PreferredNameDTO two = new PreferredNameDTO();
		two.setRequestName("CMPD-0000002");
		PreferredNameDTO three = new PreferredNameDTO();
		three.setRequestName("Not-A-Parent-Corp-Name");
		Collection<PreferredNameDTO> preferredNameDTOs = new ArrayList<PreferredNameDTO>();
		preferredNameDTOs.add(one);
		preferredNameDTOs.add(two);
		preferredNameDTOs.add(three);
		preferredNameDTOs = PreferredNameDTO.getParentPreferredNames(preferredNameDTOs);
		for (PreferredNameDTO check : preferredNameDTOs){
			if (check.getRequestName().equals("CMPD-0000001-01A")) Assert.assertTrue(check.getPreferredName().equals("CMPD-0000001-01A"));
			if (check.getRequestName().equals("CMPD-0000002-01A")) Assert.assertTrue(check.getPreferredName().equals("CMPD-0000002-01A"));
			if (check.getRequestName().equals("Not-A-Lot-Corp-Name")) Assert.assertTrue(check.getPreferredName().equals(""));
		}
	}
}
	
	
