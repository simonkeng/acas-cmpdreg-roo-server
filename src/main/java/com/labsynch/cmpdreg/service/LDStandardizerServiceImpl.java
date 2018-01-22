package com.labsynch.cmpdreg.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.labsynch.cmpdreg.chemclasses.jchem.LicenseException;
//import com.labsynch.cmpdreg.chemclasses.jchem.MolFormatException;
//import com.labsynch.cmpdreg.chemclasses.jchem.MolHandler;
//import com.labsynch.cmpdreg.chemclasses.jchem.Molecule;
//import com.labsynch.cmpdreg.chemclasses.jchem.Standardizer;
import com.labsynch.cmpdreg.domain.CorpName;
import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.dto.LDStandardizerActionDTO;
import com.labsynch.cmpdreg.dto.LDStandardizerInputDTO;
import com.labsynch.cmpdreg.dto.LDStandardizerOutputDTO;
import com.labsynch.cmpdreg.utils.SimpleUtil;

@Service
public class LDStandardizerServiceImpl implements LDStandardizerService {
	private static final Logger logger = LoggerFactory.getLogger(LDStandardizerService.class);

	@Override
	public String standardizeStructure(String molfile) throws IOException {

	    	LDStandardizerInputDTO ldStandardizerDTO = new LDStandardizerInputDTO();
	    	LDStandardizerActionDTO ldStandardizerAction = new LDStandardizerActionDTO();
	    	ldStandardizerAction.setName("CLEAN_2D");
	    	Collection<LDStandardizerActionDTO> ldStandardizerActions = new ArrayList<LDStandardizerActionDTO>();
	    	ldStandardizerActions.add(ldStandardizerAction);
	    	ldStandardizerDTO.setActions(ldStandardizerActions);
	    	
	    	HashMap<String, String> structures = new HashMap<String, String>();
	    	structures.put("key1", molfile);
	    	ldStandardizerDTO.setStructures(structures);
	    	
	    	ldStandardizerDTO.setAuth_token("nonce");
	    	ldStandardizerDTO.setTimeout(10);
	    	ldStandardizerDTO.setOutput_format("MOL");
	    	
	    	logger.info(ldStandardizerDTO.toJson());
		String json = ldStandardizerDTO.toJson();
		
		String url = "https://mcneilco-standardizer-dev.onschrodinger.com/standardizer/api/v0/standardize";
		String responseJson = SimpleUtil.postRequestToExternalServer(url, json, logger);
		
		LDStandardizerOutputDTO response = LDStandardizerOutputDTO.fromJsonToLDStandardizerOutputDTO(responseJson);
		logger.info(response.toJson());

		return response.getStructures().get("key1").getStructure();
	
	}
}

