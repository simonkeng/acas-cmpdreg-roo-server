package com.labsynch.cmpdreg.chemclasses;

import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoObject;

public class CmpdRegMoleculeIndigoImpl implements CmpdRegMolecule {
	
	private Indigo indigo = new Indigo();
	
	private IndigoObject molecule;
	
	public CmpdRegMoleculeIndigoImpl(String molStructure){
		this.molecule = indigo.loadMolecule(molStructure);
	}
	
	public String getProperty(String key){
		return molecule.getProperty(key);
	}
	
	public void setProperty(String key, String value){
		this.molecule.setProperty(key, value);
	}; 

}
