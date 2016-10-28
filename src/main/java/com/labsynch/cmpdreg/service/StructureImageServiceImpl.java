package com.labsynch.cmpdreg.service;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import chemaxon.formats.MolExporter;
import chemaxon.formats.MolFormatException;
import chemaxon.marvin.io.MolExportException;
import chemaxon.struc.Molecule;
import chemaxon.util.MolHandler;

import com.labsynch.cmpdreg.dto.SearchCompoundReturnDTO;
import com.labsynch.cmpdreg.utils.MoleculeUtil;


@Service
public class StructureImageServiceImpl implements StructureImageService {
	
	Logger logger = LoggerFactory.getLogger(StructureImageServiceImpl.class);
	
	
	

	@Override
	public byte[] convertMolToImage(String molStructure, Integer hSize, Integer wSize, String format) {
		
		if (format == null){
			format = "png";
		}
		if (hSize == null){
			hSize = 200;
		}
		if (wSize == null){
			wSize = 200;
		}
		
		String imageFormat = format + ":" + "h" + hSize + ",w" + wSize + ",maxScale28";
		
		MolHandler mh = null;
		try {
			mh = new MolHandler(molStructure);
		} catch (MolFormatException e) {
			logger.error("molformat exception. bad structure:" + molStructure);
			e.printStackTrace();
		}
		Molecule mol = mh.getMolecule();
		mol.dearomatize();

		byte[] d4 = null;
		try {
			d4 = MoleculeUtil.exportMolAsBin(mol, imageFormat);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //just defaults

		return d4;
	}
	
	
	@Override
	public byte[] displayImage(String molStructure) {
		
		MolHandler mh = null;
		try {
			mh = new MolHandler(molStructure);
		} catch (MolFormatException e) {
			logger.error("molformat exception. bad structure:" + molStructure);
			e.printStackTrace();
		}
		Molecule mol = mh.getMolecule();
		mol.dearomatize();

		 byte[] d4 = null;
		try {
			d4 = MoleculeUtil.exportMolAsBin(mol, "png");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //just defaults

		return d4;
	}
	
	@Override
	public String convertMolfilesToSDFile(String molfileJsonArray){
		
    	Collection<SearchCompoundReturnDTO> compounds = SearchCompoundReturnDTO.fromJsonArrayToSearchCompoes(molfileJsonArray);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		MolHandler mh = null;
		MolExporter molExporter = null;
		try {
			molExporter = new MolExporter(outputStream, "sdf");
		} catch (MolExportException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
    	for (SearchCompoundReturnDTO compound : compounds){
    		logger.debug("compound: " + compound.getCorpName());
    		try {
    			mh = new MolHandler(compound.getMolStructure());
        		Molecule mol = mh.getMolecule();
				logger.debug("here is the imported mol: " + MoleculeUtil.exportMolAsText(mol, "smiles"));
				mol.setProperty("corpName", compound.getCorpName());
				mol.setProperty("stereoCategoryName", compound.getStereoCategoryName());
				mol.setProperty("stereoComment", compound.getStereoComment());
				mol.setProperty("lotIDs", compound.getLotIDs().toString());
				molExporter.write(mol);

    		} catch (MolFormatException e) {
    			logger.error("bad structure error: " + compound.getMolStructure());
    		} catch (IOException e) {
    			logger.error("IO error reading in molfile");
    			e.printStackTrace();
			}
    		
    	}
    	try {
			molExporter.close();
	    	outputStream.close();
		} catch (MolExportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	logger.debug("output SDF: " + outputStream.toString());
		return outputStream.toString();
		
	}
	



}
