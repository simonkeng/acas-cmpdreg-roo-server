package com.labsynch.cmpdreg.service;

import java.io.IOException;
import java.util.List;

import com.labsynch.cmpdreg.dto.SearchCompoundReturnDTO;
import com.labsynch.cmpdreg.dto.SearchFormDTO;
import com.labsynch.cmpdreg.dto.SearchFormReturnDTO;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

public interface SearchFormService {


	public SearchFormReturnDTO  findQuerySaltForms(SearchFormDTO searchParams) throws CmpdRegMolFormatException;


	public String findParentIds(String molStructure,
			int maxResults, Float similarity, String searchType,
			String outputFormat) throws IOException, CmpdRegMolFormatException;


}
