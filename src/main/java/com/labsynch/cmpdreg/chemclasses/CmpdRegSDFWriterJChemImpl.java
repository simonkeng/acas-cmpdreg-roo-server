package com.labsynch.cmpdreg.chemclasses;

import chemaxon.struc.Molecule;

public class CmpdRegSDFWriterJChemImpl implements CmpdRegSDFWriter {

	private MolImporter molExporter;
	
	public CmpdRegSDFWriterJChemImpl(String fileName){
		FileOutputStream outStream = new FileOutputStream (fileName);
		this.molExporter = new MolExporter(outStream, "sdf");
	};
	
	public CmpdRegSDFWriterJChemImpl(FileOutputStream outStream){
		this.molExporter = new MolExporter(outStream, "sdf");
	}
	
	public void close(){
		this.molImporter.close();
	}
	
	public boolean writeMol(CmpdRegMoleculeJChemImpl molecule){
		Molecule mol = molecule.molecule;
		return this.molExporter.write(mol);
	}
}
