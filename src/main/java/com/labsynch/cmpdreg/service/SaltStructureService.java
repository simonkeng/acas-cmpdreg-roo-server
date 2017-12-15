package com.labsynch.cmpdreg.service;

import com.labsynch.cmpdreg.domain.Salt;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;



public interface SaltStructureService {

	public Salt saveStructure(Salt salt) throws CmpdRegMolFormatException;

	public Salt update(Salt salt) throws CmpdRegMolFormatException;


}
