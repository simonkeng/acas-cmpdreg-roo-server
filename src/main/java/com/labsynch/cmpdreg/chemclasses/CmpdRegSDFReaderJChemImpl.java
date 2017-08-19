package com.labsynch.cmpdreg.chemclasses;

import chemaxon.struc.Molecule;

public class CmpdRegSDFReaderJChemImpl implements CmpdRegSDFReader {

	private MolImporter molImporter;
	
	public CmpdRegSDFReaderJChemImpl(String fileName){
		FileInputStream fis;
		fis = new FileInputStream (inputFileName);
		this.molImporter = new MolImporter(fis);
	};
	
	public void close(){
		this.molImporter.close();
	}
	
	public CmpdRegMoleculeJChemImpl readNextMol(){
		Molecule mol = this.molImporter.read();
		CmpdRegMoleculeJChemImpl molecule = new CmpdRegMoleculeJChemImpl(mol);
		return molecule;
	}
}
