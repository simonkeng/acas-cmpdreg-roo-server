package com.labsynch.cmpdreg.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.BulkLoadTemplate;
import com.labsynch.cmpdreg.domain.CorpName;
import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.PhysicalState;
import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.service.ErrorMessage;
import com.labsynch.cmpdreg.utils.Configuration;
import com.labsynch.cmpdreg.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class BulkLoadTemplateDTO {
    
    private String templateName;
    
    private String recordedBy;
    
    private boolean ignored;
    
    private Collection<BulkLoadPropertyMappingDTO> mappings;
    
    public BulkLoadTemplateDTO(){
    }

}
