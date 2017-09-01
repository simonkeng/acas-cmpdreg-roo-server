package com.labsynch.cmpdreg.chemclasses.indigo;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFWriter;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFWriterFactory;
import com.labsynch.cmpdreg.chemclasses.indigo.CmpdRegSDFWriterIndigoImpl;

@Component
public class CmpdRegSDFWriterFactoryIndigoImpl implements CmpdRegSDFWriterFactory{
	
	public CmpdRegSDFWriter getCmpdRegSDFWriter(String fileName) throws IllegalArgumentException, IOException {
		return new CmpdRegSDFWriterIndigoImpl(fileName);
	}

	@Override
	public CmpdRegSDFWriter getCmpdRegSDFBufferWriter() {
		return new CmpdRegSDFWriterIndigoImpl();
	}

}
