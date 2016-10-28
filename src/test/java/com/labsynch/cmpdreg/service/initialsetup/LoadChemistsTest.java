package com.labsynch.cmpdreg.service.initialsetup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.cmpdreg.domain.Scientist;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Configurable
public class LoadChemistsTest {

	private static final Logger logger = LoggerFactory.getLogger(LoadChemistsTest.class);

	@Test
	public void loadChemsists() throws IOException{

		String fileName = "src/test/resources/User_List.csv";

		if (Scientist.countScientists() < 3L){
			logger.debug("Load in the set of chemists");
			FileInputStream fis = new FileInputStream(fileName);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String header = br.readLine();
			logger.debug("Header line: " + header);
			
			String inputLine;
			while ((inputLine = br.readLine()) != null){
				logger.debug("incoming string: " + inputLine);
				String[] tempString = inputLine.split(",");
				for (String test : tempString){
					logger.debug("test string" + test);
				}
				String fullName = tempString[0];
				String codeName = tempString[3];

				try {
					Scientist chemist = Scientist.findScientistsByCodeEquals(codeName).getSingleResult();
					logger.debug("found an existing chemist: " + chemist.getCode());
				} catch (EmptyResultDataAccessException e){
					logger.debug("Create a new scientist");
					Scientist scientist = new Scientist();
					scientist.setCode(codeName);
					scientist.setName(fullName);
					scientist.setIsChemist(true);
					scientist.persist();
				} catch (Exception e){
					logger.error("Caught an error retrieving the scientist.");
				}
			}
			br.close();
		}
	}

}

