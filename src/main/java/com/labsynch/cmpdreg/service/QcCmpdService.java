package com.labsynch.cmpdreg.service;

import java.io.IOException;

import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;



public interface QcCmpdService {

	int qcCheckParentStructures() throws CmpdRegMolFormatException, IOException;

	int dupeCheckQCStructures();

	void exportQCReport(String csvFilePathName, String exportType) throws IOException;


}
