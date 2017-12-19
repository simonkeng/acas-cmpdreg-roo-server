package com.labsynch.cmpdreg.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.labsynch.cmpdreg.chemclasses.jchem.LicenseException;
//import com.labsynch.cmpdreg.chemclasses.jchem.MolFormatException;
//import com.labsynch.cmpdreg.chemclasses.jchem.MolHandler;
//import com.labsynch.cmpdreg.chemclasses.jchem.Molecule;
//import com.labsynch.cmpdreg.chemclasses.jchem.Standardizer;
import com.labsynch.cmpdreg.domain.CorpName;
import com.labsynch.cmpdreg.domain.Lot;

@Service
public class LDStandardizerServiceImpl implements LDStandardizerService {

	@Override
	public String standardizeStructure(String molfile) throws IOException {
		// add the LD standardizer calls in here - should take in a molFile and output a molecule
		//POST with: actions, structures, auth_token, timeout
		// return status code, processed molecules will be tagged with "standardized", "failed" or "timed out"
		//molecule = LDSTANDARDIZE.standardize(molfile) < -- need to make the call to ld standardizer
		//molOut = MolExporter.exportToFormat(molecule, "mol");
		return null; //molOut
	}
}

