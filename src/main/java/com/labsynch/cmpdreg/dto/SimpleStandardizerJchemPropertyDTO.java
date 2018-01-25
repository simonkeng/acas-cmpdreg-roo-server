package com.labsynch.cmpdreg.dto;

import java.util.Collection;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.cmpdreg.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class SimpleStandardizerJchemPropertyDTO {
    
    private String standardizerConfigFilePath;
    
    public int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(17, 37).
          append(standardizerConfigFilePath).
          toHashCode();
      }
    
    public boolean equals(Object obj) {
    	   if (obj == null) { return false; }
    	   if (obj == this) { return true; }
    	   if (obj.getClass() != getClass()) {
    	     return false;
    	   }
    	   SimpleStandardizerJchemPropertyDTO rhs = (SimpleStandardizerJchemPropertyDTO) obj;
    	   return new EqualsBuilder()
    	                 .appendSuper(super.equals(obj))
    	                 .append(standardizerConfigFilePath, rhs.standardizerConfigFilePath)
    	                 .isEquals();
    	  }
}
