package com.labsynch.cmpdreg.chemclasses;

public interface CmpdRegMolecule {
		
	public void setProperty(String key, String value);
	
	public String getProperty(String key);
	
	public String[] getPropertyKeys();
	
	public String getPropertyType(String key);

}
