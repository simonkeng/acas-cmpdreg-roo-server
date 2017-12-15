package com.labsynch.cmpdreg.chemclasses.jchem;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFReader;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFReaderFactory;
import com.labsynch.cmpdreg.chemclasses.jchem.CmpdRegSDFReaderJChemImpl;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

@Component
public class CmpdRegSDFReaderFactoryJChemImpl implements CmpdRegSDFReaderFactory{
	
	@Override
	public CmpdRegSDFReader getCmpdRegSDFReader(String fileName) throws FileNotFoundException, CmpdRegMolFormatException, IOException {
		return new CmpdRegSDFReaderJChemImpl(fileName);
	}

}
