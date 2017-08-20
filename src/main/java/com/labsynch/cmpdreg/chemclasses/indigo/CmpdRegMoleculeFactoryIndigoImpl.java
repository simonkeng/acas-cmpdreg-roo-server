package com.labsynch.cmpdreg.chemclasses.indigo;

import org.springframework.stereotype.Component;

import com.labsynch.cmpdreg.chemclasses.CmpdRegMolecule;
import com.labsynch.cmpdreg.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.cmpdreg.chemclasses.indigo.CmpdRegMoleculeIndigoImpl;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

@Component
public class CmpdRegMoleculeFactoryIndigoImpl implements CmpdRegMoleculeFactory {
	
	public CmpdRegMolecule getCmpdRegMolecule(String molStructure) throws CmpdRegMolFormatException {
		return new CmpdRegMoleculeIndigoImpl(molStructure);
	}

}
