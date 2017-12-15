package com.labsynch.cmpdreg.chemclasses;

import java.io.IOException;

import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

public interface CmpdRegSDFWriter {

	public boolean writeMol(CmpdRegMolecule mol) throws CmpdRegMolFormatException, IOException;
	
	public void close() throws IOException;
	
	public String getBufferString();
	
}
