package com.labsynch.cmpdreg.dto;

import java.util.Collection;
import java.util.HashMap;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class LDStandardizerInputDTO {
    private Collection<LDStandardizerActionDTO> actions;
    
    private HashMap<String, String> structures;
    
    private String auth_token;
    
    private Integer timeout;
    
    private String output_format;

}