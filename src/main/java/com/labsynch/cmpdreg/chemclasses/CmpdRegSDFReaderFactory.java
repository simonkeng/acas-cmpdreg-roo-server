package com.labsynch.cmpdreg.chemclasses;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

public interface CmpdRegSDFReaderFactory {
	
	public CmpdRegSDFReader getCmpdRegSDFReader(String fileName) throws FileNotFoundException, CmpdRegMolFormatException, IOException;

}
