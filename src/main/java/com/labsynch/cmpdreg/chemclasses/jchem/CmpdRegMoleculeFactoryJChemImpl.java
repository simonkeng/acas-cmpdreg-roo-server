package com.labsynch.cmpdreg.chemclasses.jchem;

import org.springframework.stereotype.Component;

import com.labsynch.cmpdreg.chemclasses.CmpdRegMolecule;
import com.labsynch.cmpdreg.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.cmpdreg.chemclasses.jchem.CmpdRegMoleculeJChemImpl;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

import chemaxon.formats.MolFormatException;

@Component
public class CmpdRegMoleculeFactoryJChemImpl implements CmpdRegMoleculeFactory{
	
	@Override
	public CmpdRegMolecule getCmpdRegMolecule(String molStructure) throws CmpdRegMolFormatException {
		try {
			return new CmpdRegMoleculeJChemImpl(molStructure);
		} catch (MolFormatException e) {
			throw new CmpdRegMolFormatException(e);
		}
	}

}
