package com.labsynch.cmpdreg.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.cmpdreg.domain.ParentAlias;
import com.labsynch.cmpdreg.domain.SaltFormAlias;

@RooJavaBean
@RooToString
@RooJson
public class SaltFormAliasDTO {

    private String saltFormCorpName;
    
    private String lsType;
    
    private String lsKind;
    
    private String aliasName;
    
    private boolean preferred;
    
    public SaltFormAliasDTO(){
    	
    }
    
    public SaltFormAliasDTO(SaltFormAlias saltFormAlias){
    	this.saltFormCorpName = saltFormAlias.getSaltForm().getCorpName();
    	this.lsType = saltFormAlias.getLsType();
    	this.lsKind = saltFormAlias.getLsKind();
    	this.aliasName = saltFormAlias.getAliasName();
    	this.preferred = saltFormAlias.isPreferred();
    }
    
    public boolean getPreferred(){
    	return this.preferred;
    }
    
   
}
