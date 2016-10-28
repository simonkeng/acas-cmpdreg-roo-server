package com.labsynch.cmpdreg.service;

import java.io.FileNotFoundException;
import java.util.Collection;

import com.labsynch.cmpdreg.dto.ExportResultDTO;
import com.labsynch.cmpdreg.dto.SearchResultExportRequestDTO;


public interface ExportService {

	ExportResultDTO exportSearchResults(SearchResultExportRequestDTO searchResultExportRequestDTO) throws FileNotFoundException;

	ExportResultDTO exportLots(String filePath, Collection<String> lotCorpNames) throws FileNotFoundException;



}
