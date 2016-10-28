package com.labsynch.cmpdreg.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chemaxon.formats.MolExporter;
import chemaxon.formats.MolFormatException;
import chemaxon.marvin.io.MPropHandler;
import chemaxon.marvin.io.MolExportException;
import chemaxon.struc.Molecule;
import chemaxon.util.MolHandler;

public class MoleculeUtil {

	static Logger logger = LoggerFactory.getLogger(MoleculeUtil.class);

	public static boolean validateMolProperty(Molecule mol, String propName){
		
		String jchemVersion = Configuration.getConfigInfo().getServerSettings().getJchemVersion();
		
		// note: new method introduced in 5.7 - deprecates the method below
		// (MPropHandler.convertToString(mol.properties(), "date_submitted")
		boolean validProperty = false;
		String molProperty = null;

		if (propName != null && mol.getProperty(propName) != null){
			molProperty = mol.getProperty(propName).trim();
			if (!molProperty.equalsIgnoreCase("")){
				validProperty = true;
			}
		} 
		
		return validProperty;
	}
	
	public static String getMolProperty(Molecule mol, String propName){
		// note: new method introduced in 5.7 - deprecates the method below
		// (MPropHandler.convertToString(mol.properties(), "date_submitted")
//		MPropHandler.convertToString(mol.properties(), propName.trim());			
		
		String molProperty = null;
		if (mol.getProperty(propName) != null){
			molProperty = mol.getProperty(propName).trim();
		} else {
			logger.error("the requested property is null: " + propName);
		}
		
		return molProperty;
	}
	
	public static double getMolWeight(String molStructure) {
		MolHandler mh = null;
		boolean badStructureFlag = false;
		Molecule mol = null;
		try {
			mh = new MolHandler(molStructure);
			mol = mh.getMolecule();			
		} catch (MolFormatException e) {
			badStructureFlag = true;
		}

		if (!badStructureFlag){
			return mol.getMass();
		} else {
			return 0d;
		}
	}

	public static Molecule setMolProperty(Molecule mol, String key, String value){
		mol.setProperty(key, value);
		return mol;
	}
	

	public static byte[] exportMolAsBin(Molecule mol, String exportFormat) throws IOException{
		return MolExporter.exportToBinFormat(mol, exportFormat);
	}

	public static String exportMolAsText(Molecule mol, String exportFormat) throws IOException{
		return MolExporter.exportToFormat(mol, exportFormat);

	}
	
	
}