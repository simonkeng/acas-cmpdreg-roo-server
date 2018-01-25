package com.labsynch.cmpdreg.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;
import com.labsynch.cmpdreg.exceptions.StandardizerException;

@Service
public interface LDStandardizerService {

	public String standardizeStructure(String molfile) throws CmpdRegMolFormatException, StandardizerException;

}
