package com.labsynch.cmpdreg.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.cmpdreg.domain.CorpName;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class SearchCorpNameTest {
	
    private static final Logger logger = LoggerFactory.getLogger(SearchCorpNameTest.class);
	
	    @Test
	    public void convertCorpName() {
	    	//simple test to generate a new corpName
			String corpName1 = CorpName.prefix.concat(CorpName.separator).concat("0001");
			String corpName2 = "101";
			String corpName3 = CorpName.prefix.concat(CorpName.separator).concat("0001").concat(CorpName.separator).concat("TFA");			
			String corpName4 = CorpName.prefix.concat("-0001-TFA-01");
			String corpName5 = CorpName.prefix.concat("-0031-TFA");
			String corpName6 = "apple";
			
			long newName4 = CorpName.convertToParentNumber(corpName4);
			long newName5 = CorpName.convertToParentNumber(corpName5);
			long newName6 = CorpName.convertToParentNumber(corpName6);
			long newName2 = CorpName.convertToParentNumber(corpName2);
			
			Assert.assertEquals(1L, newName4);
			Assert.assertEquals(31L, newName5);
			Assert.assertEquals(0L, newName6);
			Assert.assertEquals(101L, newName2);
			
			logger.debug("corpName5: " + corpName5 + "   newName5: " + newName5);
			logger.debug("corpName6: " + corpName6 + "   newName6: " + newName6);
			logger.debug("corpNam2: " + corpName2 + "   newName2: " + newName2);

			
			
//			corpName1 = this.convertCorpNamePrefix(corpName1);
//			corpName2 = this.convertCorpNameNumber(corpName2);
			
			Long newNumber = this.convertCorpNameToNumber(corpName1);
			logger.debug("converted number: " + newNumber);

			
			logger.debug("is this a corpNumber format: " + CorpName.checkCorpNumber(corpName2));
			
			
			logger.debug("parent corp name: " + CorpName.checkParentCorpName(corpName1));
			logger.debug("parent corp name: " + CorpName.checkParentCorpName(corpName2));
			logger.debug("parent corp name: " + CorpName.checkParentCorpName(corpName3));
			logger.debug("parent corp name: " + CorpName.checkParentCorpName(corpName4));
			logger.debug("parent corp name: " + CorpName.checkParentCorpName(corpName5));
			
			logger.debug("checkSaltFormCorpName corp name: " + CorpName.checkSaltFormCorpName(corpName1));
			logger.debug("checkSaltFormCorpName corp name: " + CorpName.checkSaltFormCorpName(corpName2));
			logger.debug("checkSaltFormCorpName corp name: " + CorpName.checkSaltFormCorpName(corpName3));
			logger.debug("checkSaltFormCorpName corp name: " + CorpName.checkSaltFormCorpName(corpName4));
			logger.debug("checkSaltFormCorpName corp name: " + CorpName.checkSaltFormCorpName(corpName5));

			logger.debug("checkLotCorpName corp name: " + CorpName.checkLotCorpName(corpName1));
			logger.debug("checkLotCorpName corp name: " + CorpName.checkLotCorpName(corpName2));
			logger.debug("checkLotCorpName corp name: " + CorpName.checkLotCorpName(corpName3));
			logger.debug("checkLotCorpName corp name: " + CorpName.checkLotCorpName(corpName4));
			logger.debug("checkLotCorpName corp name: " + CorpName.checkLotCorpName(corpName5));
			
	    }

		private Long convertCorpNameToNumber(String corpName) {
			Pattern pattern = Pattern.compile(CorpName.separator);
	        // Split input with the pattern
	        String[] result = pattern.split(corpName);
	        String corpNumberString = result[1].trim();
	        Long corpNumber = Long.parseLong(corpNumberString);
			return corpNumber;
		}
		
		@Test
	    public void convertCorpName2() {
	    	//simple test to generate a new corpName
			String corpName1 = "cmpd123";
			String corpName2 = "101";
			
			if(checkCorpNumberFormat(corpName1)){
				logger.debug("found stringNumber");
				Long corpNumber = convertCorpNameToNumber2(corpName1);
				logger.debug("corp number");
			}
			
			
	    }
	    
		private boolean checkCorpNumberFormat(String corpName) {
	    	corpName = corpName.trim();
			Pattern corpNumberPattern = Pattern.compile("^" + CorpName.prefix + "[0-9]{1,9}$", Pattern.CASE_INSENSITIVE);
			Matcher matcher = corpNumberPattern.matcher(corpName);
	    	return matcher.find();
		}
		
		private Long convertCorpNameToNumber2(String corpName) {
			corpName = corpName.trim();
			Pattern pattern = Pattern.compile(CorpName.prefix, Pattern.CASE_INSENSITIVE);
	        String[] result = pattern.split(corpName);
	        String corpNumberString = result[1].trim();
	        Long corpNumber = Long.parseLong(corpNumberString);
			return corpNumber;
		}

	    
}
