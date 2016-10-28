package com.labsynch.cmpdreg.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.CorpName;
import com.labsynch.cmpdreg.domain.Lot;

import org.junit.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Configurable
public class CustomCorpNameTest {

	private static final Logger logger = LoggerFactory.getLogger(CustomCorpNameTest.class);

//	@Before
//	public void loginTestUser() {
//		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("cchemist", "cchemist"));
//	}

	@Test
	@Transactional
	public void countLotByNotebookTest(){
		String notebookPage = "NOTEBOOK-LB-0001-001";
		List<Lot> lots = Lot.findLotsByNotebookPageEquals(notebookPage).getResultList();
		logger.info("number of lots: " + lots.size());

	}

	@Test
	@Transactional
	public void funkyNameTest(){
		String inputCorpDigits = "489";
		Assert.assertEquals(7, Lot.generateCasCheckDigit("489"));
		Assert.assertEquals(2, Lot.generateCasCheckDigit("125"));
		int casCheckDigit = Lot.generateCasCheckDigit(inputCorpDigits);
		logger.info("cas check digit:" + casCheckDigit);

		String fullName =String.format("%09d", Long.parseLong(inputCorpDigits));
		logger.info(fullName);
		// 125 ==> 3*1 + 2*2 + 5*1 = 3 + 4 + 5 = 12 -> %10 = 2
	}

	@Test
	@Transactional
	public void testFormat(){
		String inputCorpDigits = "1252489";
		String fullName =String.format("%09d", Long.parseLong(inputCorpDigits));
		logger.info(fullName);

		String regexPattern = "(\\d{3})(\\d{3})(\\d{3})";
		Pattern p = Pattern.compile(regexPattern);
		Matcher m = p.matcher(fullName);
		if (m.find()){
			logger.info("first: " + m.group(1));
			logger.info("second: " + m.group(2));
			logger.info("third: " + m.group(3));
		}
	}

	@Test
	@Transactional
	public void testLotSeq(){
		List<Long> seqList = Lot.generateCustomLotSequence();
		logger.info("custom lot seqeunce: " + seqList.get(0));

	}

	@Test
	@Transactional
	public void testLotName(){
		logger.info("custom lot name: " + Lot.generateCasStyleLotName());

	}

	@Test
	@Transactional
	public void testLicensePlateName(){
		List<Object> seqList = CorpName.generateCustomParentSequence();
		String blah = String.valueOf(seqList.get(0));
		logger.info("custom parent seqeunce: " + blah);
		int testInput = 7890;
		List<String> output = CorpName.generateLicensePlate(Integer.parseInt(blah));
		
		logger.info("custom license plate: " + output.get(0));

	}

	@Test
	@Transactional
	public void testLicensePlateNameFull(){
		String output = CorpName.generateCorpLicensePlate();
		logger.info("custom license plate: " + output);

	}
}
