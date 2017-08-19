package com.labsynch.cmpdreg.chemclasses;

import chemaxon.formats.MolFormatException;
import chemaxon.struc.Molecule;
import chemaxon.util.MolHandler;


public class CmpdRegMoleculeJChemImpl implements CmpdRegMolecule {
	
	public Molecule molecule;
	
	public CmpdRegMoleculeJChemImpl(String molStructure) throws MolFormatException{
		MolHandler mh = null;
		boolean badStructureFlag = false;
		Molecule mol = null;
		String lineEnd = System.getProperty("line.separator");
		try {
			mh = new MolHandler(molStructure);
			mol = mh.getMolecule();		
		} catch (MolFormatException e1) {
			logger.debug("failed first attempt: bad mol structure: " + molStructure);
			// clean up the molString and try again
			try {
				molStructure = new StringBuilder().append(lineEnd).append(molStructure).append(lineEnd).toString();
				mh = new MolHandler(molStructure);
				mol = mh.getMolecule();
			} catch (MolFormatException e2) {
				logger.debug("failed second attempt: bad mol structure: " + molStructure);
				badStructureFlag = true;
				logger.error("bad mol structure: " + molStructure);
			}
		}	

		if (!badStructureFlag){
			this.molecule = mol;
		} else {
			throw MolFormatException("bad mol structure: " + molStructure);
		}
	}
	
	public CmpdRegMoleculeJChemImpl(Molecule mol){
		this.molecule = mol;
	}
	
	public String getProperty(String key){
		return molecule.getProperty(key);
	}
	
	public void setProperty(String key, String value){
		this.molecule.setProperty(key, value);
	}; 
	
	public String[] getPropertyKeys(){
		return this.molecule.properties().getKeys();
	};

	public String getPropertyType(String key){
		MProp prop = molecule.properties().get(key);
		return prop.getPropType();
	};
}
