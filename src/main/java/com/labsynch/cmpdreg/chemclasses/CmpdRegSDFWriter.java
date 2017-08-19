package com.labsynch.cmpdreg.chemclasses;

public interface CmpdRegSDFWriter {

	public boolean writeMol(CmpdRegMolecule mol);
	
	public void close();
}
