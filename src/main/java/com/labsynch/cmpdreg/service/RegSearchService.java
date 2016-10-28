package com.labsynch.cmpdreg.service;

import java.io.IOException;

import com.labsynch.cmpdreg.dto.RegSearchDTO;

public interface RegSearchService {

	public RegSearchDTO getParentsbyParams(String searchParamsString) throws IOException;

}
