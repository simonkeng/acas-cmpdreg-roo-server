package com.labsynch.cmpdreg.chemclasses.indigo;

import java.io.IOException;

import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoObject;
import com.labsynch.cmpdreg.chemclasses.CmpdRegMolecule;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFReader;

public class CmpdRegSDFReaderIndigoImpl implements CmpdRegSDFReader {
	
	private Indigo indigo = new Indigo();
	
	private IndigoObject reader;
	
	public CmpdRegSDFReaderIndigoImpl(String fileName) {
		this.reader = indigo.iterateSDFile(fileName);
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	@Override
	public CmpdRegMolecule readNextMol() throws IOException {
		CmpdRegMoleculeIndigoImpl molecule = new CmpdRegMoleculeIndigoImpl(reader.next());
		return molecule;
	}

}
