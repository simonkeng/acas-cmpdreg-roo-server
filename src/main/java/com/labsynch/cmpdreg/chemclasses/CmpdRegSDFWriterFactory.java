package com.labsynch.cmpdreg.chemclasses;

import java.io.IOException;

public interface CmpdRegSDFWriterFactory {
	
	public CmpdRegSDFWriter getCmpdRegSDFWriter(String fileName) throws IllegalArgumentException, IOException;

}
