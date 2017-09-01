package com.labsynch.cmpdreg.chemclasses.indigo;

import java.io.IOException;

import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoObject;
import com.labsynch.cmpdreg.chemclasses.CmpdRegMolecule;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFWriter;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

public class CmpdRegSDFWriterIndigoImpl implements CmpdRegSDFWriter {
	
	private Indigo indigo = new Indigo();
	
	private IndigoObject writer;

	public CmpdRegSDFWriterIndigoImpl(String fileName) {
		this.writer = indigo.writeFile(fileName);
	}
	
	public CmpdRegSDFWriterIndigoImpl() {
		this.writer = indigo.writeBuffer();
	}

	@Override
	public boolean writeMol(CmpdRegMolecule molecule) throws CmpdRegMolFormatException, IOException {
		CmpdRegMoleculeIndigoImpl molWrapper = (CmpdRegMoleculeIndigoImpl) molecule;
		try{
			writer.sdfAppend(molWrapper.molecule);
		}catch (Exception e){
			return false;
		}
		return true;
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}
	
	@Override
	public String getBufferString() {
		return this.writer.toString();
	}

}
