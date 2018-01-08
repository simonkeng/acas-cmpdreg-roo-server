package com.labsynch.cmpdreg.dto;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@RooJavaBean
@RooToString
@RooJson
public class LDStandardizerInputDTO {
    private Collection<LDStandardizerActionDTO> actions;
    
    private HashMap<String, String> structures;
    
    private String auth_token;
    
    private Integer timeout;
    
    private String output_format;
    
	public String toJson() {
        return new JSONSerializer().include("actions")
        		.exclude("*.class")
        		.serialize(this);
    }

}