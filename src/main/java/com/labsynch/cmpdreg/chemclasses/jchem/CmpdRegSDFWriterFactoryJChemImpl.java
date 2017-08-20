package com.labsynch.cmpdreg.chemclasses.jchem;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFWriter;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFWriterFactory;
import com.labsynch.cmpdreg.chemclasses.jchem.CmpdRegSDFWriterJChemImpl;

@Component
public class CmpdRegSDFWriterFactoryJChemImpl implements CmpdRegSDFWriterFactory{
	
	@Override
	public CmpdRegSDFWriter getCmpdRegSDFWriter(String fileName) throws IllegalArgumentException, IOException {
		return new CmpdRegSDFWriterJChemImpl(fileName);
	}

}
