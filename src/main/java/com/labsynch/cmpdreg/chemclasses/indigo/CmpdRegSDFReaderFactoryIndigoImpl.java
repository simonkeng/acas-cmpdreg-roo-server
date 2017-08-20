package com.labsynch.cmpdreg.chemclasses.indigo;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFReader;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFReaderFactory;
import com.labsynch.cmpdreg.chemclasses.indigo.CmpdRegSDFReaderIndigoImpl;
import com.labsynch.cmpdreg.chemclasses.jchem.CmpdRegSDFReaderJChemImpl;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

@Component
public class CmpdRegSDFReaderFactoryIndigoImpl implements CmpdRegSDFReaderFactory{
	
	public CmpdRegSDFReader getCmpdRegSDFReader(String fileName) throws FileNotFoundException, CmpdRegMolFormatException, IOException {
		return new CmpdRegSDFReaderIndigoImpl(fileName);
	}

}
