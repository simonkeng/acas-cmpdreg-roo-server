package com.labsynch.cmpdreg.chemclasses;

import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

public interface CmpdRegMolecule {
		
	public void setProperty(String key, String value);
	
	public String getProperty(String key);
	
	public String[] getPropertyKeys();
	
	public String getPropertyType(String key);
	
	public String getMolStructure();

	public String getFormula();

	public Double getExactMass();

	public Double getMass();

	public int getTotalCharge();

	public String getSmiles();
	
	public CmpdRegMolecule replaceStructure(String newStructure) throws CmpdRegMolFormatException;

	public String getMrvStructure();

}
