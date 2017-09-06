package com.labsynch.cmpdreg.chemclasses.indigo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoObject;
import com.epam.indigo.IndigoRenderer;
import com.labsynch.cmpdreg.chemclasses.CmpdRegMolecule;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

public class CmpdRegMoleculeIndigoImpl implements CmpdRegMolecule {
	
	Logger logger = LoggerFactory.getLogger(CmpdRegMoleculeIndigoImpl.class);
	
	private Indigo indigo = new Indigo();
	
	IndigoObject molecule;
	
	public CmpdRegMoleculeIndigoImpl(String molStructure){
		this.molecule = indigo.loadMolecule(molStructure);
	}
	
	public CmpdRegMoleculeIndigoImpl(IndigoObject molecule){
		this.molecule = molecule;
	}
	
	public String getProperty(String key){
		return molecule.getProperty(key);
	}
	
	public void setProperty(String key, String value){
		this.molecule.setProperty(key, value);
	}

	@Override
	public String[] getPropertyKeys() {
		List<String> propertyKeys = new ArrayList<String>();
		for (IndigoObject prop:  this.molecule.iterateProperties()) {
			propertyKeys.add(prop.name());
		}
		String[] keys = new String[propertyKeys.size()];
		return propertyKeys.toArray(keys);
	}

	@Override
	public String getPropertyType(String key) {
		//Does not seem to exist
		return null;
	}

	@Override
	public String getMolStructure() {
		return this.molecule.molfile();
	}
	
	@Override
	public String getSmiles() {
		return this.molecule.smiles();
	}
	
	@Override
	public String getMrvStructure() {
		// not implemented in Indigo
		return null;
	}; 

	@Override
	public String getFormula() {
		return this.molecule.grossFormula();
	}

	@Override
	public Double getExactMass() {
		return (double) this.molecule.monoisotopicMass();
	}

	@Override
	public Double getMass() {
		return (double) this.molecule.molecularWeight();
	}

	@Override
	public int getTotalCharge() {
		return this.molecule.charge();
	}

	@Override
	public CmpdRegMolecule replaceStructure(String newStructure) throws CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] toBinary(CmpdRegMolecule molecule, String format) throws IOException {
		IndigoObject mol = ((CmpdRegMoleculeIndigoImpl) molecule).molecule;
		try {
			IndigoRenderer renderer = new IndigoRenderer(indigo);
			//sort out different formats later
			indigo.setOption("render-output-format", "png");
			return renderer.renderToBuffer(mol);
		}
		catch (Exception e) {
			logger.error("cannot render",e);
			return null;
		}
	}

	@Override
	public void dearomatize() {
		this.molecule.dearomatize();
	}

}
