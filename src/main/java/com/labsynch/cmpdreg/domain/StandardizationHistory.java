package com.labsynch.cmpdreg.domain;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord
public class StandardizationHistory {

	private String settings;

	private int settingsHash;

	private Date dateOfStandardization;

	private int structuresStandardizedCount;

	private int newDuplicateCount;

	private int oldDuplicateCount;

	private int displayChangeCount;

	private int asDrawnDisplayChangeCount;

	private int changedStructureCount;

	public StandardizationHistory() {
	}

}
