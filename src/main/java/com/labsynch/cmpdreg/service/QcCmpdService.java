package com.labsynch.cmpdreg.service;

import java.io.IOException;

import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;
import com.labsynch.cmpdreg.exceptions.StandardizerException;

public interface QcCmpdService {

	int qcCheckParentStructures() throws CmpdRegMolFormatException, IOException, StandardizerException;

	int dupeCheckQCStructures() throws CmpdRegMolFormatException;

	void exportQCReport(String csvFilePathName, String exportType) throws IOException, CmpdRegMolFormatException, StandardizerException;

}
