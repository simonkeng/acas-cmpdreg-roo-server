package com.labsynch.cmpdreg.service;

import java.io.IOException;

import com.labsynch.cmpdreg.dto.RegSearchDTO;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

public interface RegSearchService {

	public RegSearchDTO getParentsbyParams(String searchParamsString) throws IOException, CmpdRegMolFormatException;

}
