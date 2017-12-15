package com.labsynch.cmpdreg.chemclasses;

import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;


public interface CmpdRegMoleculeFactory {
	
	public CmpdRegMolecule getCmpdRegMolecule(String molStructure) throws CmpdRegMolFormatException;

}
