package com.labsynch.cmpdreg.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.cmpdreg.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class SearchResultExportRequestDTO {

    private String filePath;
    
    private SearchFormReturnDTO searchFormResultsDTO;
    
    public SearchResultExportRequestDTO(){
    	
    }
    
    public SearchResultExportRequestDTO(String filePath, SearchFormReturnDTO searchFormResultsDTO){
    	this.filePath = filePath;
    	this.searchFormResultsDTO = searchFormResultsDTO;
    }
    
    public String toJson() {
        return new JSONSerializer().include("searchFormResultsDTO.foundCompounds.lotIDs").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
}
