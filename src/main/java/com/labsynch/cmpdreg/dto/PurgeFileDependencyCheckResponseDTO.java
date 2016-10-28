package com.labsynch.cmpdreg.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.cmpdreg.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class PurgeFileDependencyCheckResponseDTO {

    private String summary;
    
    private boolean canPurge;
    
    public PurgeFileDependencyCheckResponseDTO(){
    	
    }
    
    public PurgeFileDependencyCheckResponseDTO(String summary, boolean canPurge){
    	this.summary = summary;
    	this.canPurge = canPurge;
    }
    
    public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
}
