package com.labsynch.cmpdreg.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findScientistsByCodeEquals" })
public class Scientist {

    private String code;

    private String name;

    private Boolean isChemist;

    private Boolean isAdmin;

    private Boolean ignore;

	
	
}
