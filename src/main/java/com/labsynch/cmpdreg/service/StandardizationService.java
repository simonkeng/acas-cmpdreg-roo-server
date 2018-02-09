package com.labsynch.cmpdreg.service;

import java.io.IOException;
import java.util.List;

import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;
import com.labsynch.cmpdreg.exceptions.StandardizerException;

public interface StandardizationService {

	void reset();

	int populateStanardizationDryRunTable() throws CmpdRegMolFormatException, IOException, StandardizerException;

	int dupeCheckStandardizationStructures() throws CmpdRegMolFormatException;

	String getStandardizationDryRunReport() throws IOException, CmpdRegMolFormatException, StandardizerException;
	
	String getDryRunStats();

	String executeStandardization() throws IOException, CmpdRegMolFormatException, StandardizerException;

	int restandardizeParentStructures(List<Long> parentIds) throws CmpdRegMolFormatException, IOException, StandardizerException;

	String standardizeSingleMol(String mol) throws CmpdRegMolFormatException, StandardizerException, IOException;
	
}
