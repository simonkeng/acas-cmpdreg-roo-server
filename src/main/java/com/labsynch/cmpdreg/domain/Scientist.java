package com.labsynch.cmpdreg.domain;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findScientistsByCodeEquals" })
public class Scientist {

	private static final Logger logger = LoggerFactory.getLogger(Scientist.class);

    private String code;

    private String name;

    private Boolean isChemist;

    private Boolean isAdmin;

    private Boolean ignore;

	public static Scientist checkValidUser(String modifiedByUser) {
		List<Scientist> chemists = Scientist.findScientistsByCodeEquals(modifiedByUser).getResultList();
		Scientist validUser;		
		if (chemists.size() == 0){
			String errorMessage = "ERROR: Unable to find modifedByUser: " + modifiedByUser;
			logger.error(errorMessage);
			throw new RuntimeException(errorMessage);
		} else if (chemists.size() > 1){
			String errorMessage = "ERROR: Found multiple modifedByUser(s): " + modifiedByUser;
			logger.error("ERROR: Found multiple modifedByUser(s): " + modifiedByUser);				
			throw new RuntimeException(errorMessage);
		} else {
			validUser = chemists.get(0);
		}
		return validUser;
	}
	
}
