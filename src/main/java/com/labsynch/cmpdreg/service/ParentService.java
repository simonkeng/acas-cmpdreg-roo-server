package com.labsynch.cmpdreg.service;

import java.util.Collection;

import chemaxon.formats.MolFormatException;

import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.dto.CodeTableDTO;
import com.labsynch.cmpdreg.dto.ParentValidationDTO;
import com.labsynch.cmpdreg.exceptions.MissingPropertyException;



public interface ParentService {

	ParentValidationDTO validateUniqueParent(Parent queryParent) throws MolFormatException;

	Collection<CodeTableDTO> updateParent(Parent parent);


}
