package com.labsynch.cmpdreg.dto;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.PhysicalState;

@RooJavaBean
@RooToString
//@RooEntity
@RooJson
public class PreferredNameDTO {

    private String requestName;
    
    private String preferredName;
    
    public PreferredNameDTO(){
    	
    }
    
    @Transactional
    public static Collection<PreferredNameDTO> getPreferredNames(Collection<PreferredNameDTO> preferredNameDTOs){
    	for (PreferredNameDTO preferredNameDTO : preferredNameDTOs){
    		String preferredName;
    		try {
    			preferredName = Lot.findLotsByCorpNameEquals(preferredNameDTO.getRequestName()).getSingleResult().getCorpName();
    		} catch (EmptyResultDataAccessException e){
    			preferredName = "";
    		}
    		preferredNameDTO.setPreferredName(preferredName);
    	}
    	return preferredNameDTOs;
    }

	public static Collection<PreferredNameDTO> getParentPreferredNames(Collection<PreferredNameDTO> preferredNameDTOs) {
		for (PreferredNameDTO preferredNameDTO : preferredNameDTOs){
    		String preferredName;
    		try {
    			preferredName = Parent.findParentsByCorpNameEquals(preferredNameDTO.getRequestName()).getSingleResult().getCorpName();
    		} catch (EmptyResultDataAccessException e){
    			preferredName = "";
    		}
    		preferredNameDTO.setPreferredName(preferredName);
    	}
    	return preferredNameDTOs;
	}
}
