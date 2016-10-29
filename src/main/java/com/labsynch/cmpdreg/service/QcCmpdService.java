package com.labsynch.cmpdreg.service;

import java.io.IOException;
import java.util.Collection;

import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.dto.CodeTableDTO;
import com.labsynch.cmpdreg.dto.ParentValidationDTO;

import chemaxon.formats.MolFormatException;



public interface QcCmpdService {

	int qcCheckParentStructures() throws MolFormatException, IOException;

	int dupeCheckQCStructures();

	void exportQCReport(String csvFilePathName, String exportType) throws IOException;


}
