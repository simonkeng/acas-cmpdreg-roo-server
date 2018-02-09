package com.labsynch.cmpdreg.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord
public class StandardizationSettings {

    /**
     */
    private String currentSettings;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date modifiedDate;

    /**
     */
    private Boolean needsStandardization;

    /**
     */
    private int currentSettingsHash;
}
