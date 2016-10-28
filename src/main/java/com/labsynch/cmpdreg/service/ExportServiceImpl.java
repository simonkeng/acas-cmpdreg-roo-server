package com.labsynch.cmpdreg.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import chemaxon.formats.MolConverter;
import chemaxon.formats.MolExporter;
import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.marvin.io.formats.MoleculeImporter;
import chemaxon.struc.Molecule;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.dto.ExportResultDTO;
import com.labsynch.cmpdreg.dto.LotDTO;
import com.labsynch.cmpdreg.dto.SearchCompoundReturnDTO;
import com.labsynch.cmpdreg.dto.SearchLotDTO;
import com.labsynch.cmpdreg.dto.SearchResultExportRequestDTO;
import com.labsynch.cmpdreg.utils.SimpleUtil;

@Service
public class ExportServiceImpl implements ExportService {

	Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);

	@Override
	public ExportResultDTO exportSearchResults(
			SearchResultExportRequestDTO searchResultExportRequestDTO) throws FileNotFoundException {
		Collection<String> lotCorpNames = new HashSet<String>();
		for( SearchCompoundReturnDTO compound: searchResultExportRequestDTO.getSearchFormResultsDTO().getFoundCompounds()){
			for (SearchLotDTO lot : compound.getLotIDs()){
				lotCorpNames.add(lot.getCorpName());
			}
		}
		ExportResultDTO exportResult = exportLots(searchResultExportRequestDTO.getFilePath(), lotCorpNames);
		return exportResult;
	}
	
	@Override
	public ExportResultDTO exportLots(String filePath, Collection<String> lotCorpNames) throws FileNotFoundException{
		ExportResultDTO result = new ExportResultDTO();
		result.setReportFilePath(filePath);
		Collection<Lot> foundLots = getLotsByCorpNames(lotCorpNames);
		Collection<LotDTO> lotDTOs = new HashSet<LotDTO>();
		for (Lot foundLot : foundLots){
			LotDTO lotDTO = new LotDTO(foundLot);
			lotDTOs.add(lotDTO);
		}
		logger.debug("Attempting to export "+lotDTOs.size()+" lots");
		FileOutputStream exportSDFOutStream = new FileOutputStream(filePath, false);
		int lotsLoaded;
		try{
			lotsLoaded = writeLotsToSDF(exportSDFOutStream, lotDTOs);
			String summary = "Successfully exported "+ lotsLoaded+" lots.";
			result.setSummary(summary);
			return result;
		}catch (Exception e){
			logger.error("Caught exception trying to write export sdf",e);
			result.setLevel("error");
			result.setMessage(e.getMessage());
			return result;
		}
	}
	
	private Collection<Lot> getLotsByCorpNames(Collection<String> lotCorpNames){
		EntityManager em = Lot.entityManager();
    	List<String> batchCodes = new ArrayList<String>();
    	batchCodes.addAll(lotCorpNames);
    	String queryString = "Select lot "
    			+ "FROM Lot lot "
    			+ "WHERE ";
    	Collection<Query> queries = SimpleUtil.splitHqlInClause(em, queryString, "lot.corpName", batchCodes);
    	Collection<Lot> results = new ArrayList<Lot>();
		logger.debug("Querying for "+batchCodes.size()+" lot corpnames");
		for (Query q : queries){
			if (logger.isDebugEnabled()) logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
			results.addAll(q.getResultList());
		}
		return results;
	}
	
	private int writeLotsToSDF(FileOutputStream exportSDFOutStream, Collection<LotDTO> lotDTOs) throws IllegalArgumentException, IOException{
		MolExporter exporter = new MolExporter(exportSDFOutStream, "sdf");
		for (LotDTO lotDTO : lotDTOs){
			Molecule mol = MolImporter.importMol(lotDTO.getParentStructure(), "mol");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if (lotDTO.getAbsorbance() != null) mol.setProperty("Absorbance", lotDTO.getAbsorbance().toString());
			if (lotDTO.getAmount() != null) mol.setProperty("Amount", lotDTO.getAmount().toString());
			if (lotDTO.getAmountUnitsCode() != null) mol.setProperty("Amount Units Code", lotDTO.getAmountUnitsCode());
			if (lotDTO.getBarcode() != null) mol.setProperty("Barcode", lotDTO.getBarcode());
			if (lotDTO.getBoilingPoint() != null) mol.setProperty("Boiling Point", lotDTO.getBoilingPoint().toString());
			if (lotDTO.getBuid() != null) mol.setProperty("Buid", lotDTO.getBuid().toString());
			if (lotDTO.getBulkLoadFile() != null) mol.setProperty("Bulk Load File", lotDTO.getBulkLoadFile());
			if (lotDTO.getChemist() != null) mol.setProperty("Chemist", lotDTO.getChemist());
			if (lotDTO.getColor() != null) mol.setProperty("Color", lotDTO.getColor());
			if (lotDTO.getComments() != null) mol.setProperty("Comments", lotDTO.getComments());
			if (lotDTO.getLotAliases() != null) mol.setProperty("Lot Aliases", lotDTO.getLotAliases());
			if (lotDTO.getLotCorpName() != null) mol.setProperty("Lot Corp Name", lotDTO.getLotCorpName());
			if (lotDTO.getIsVirtual() != null) mol.setProperty("Is Virtual", lotDTO.getIsVirtual().toString());
			if (lotDTO.getLambda() != null) mol.setProperty("Lambda", lotDTO.getLambda().toString());
			if (lotDTO.getLotMolWeight() != null) mol.setProperty("Lot Mol Weight", lotDTO.getLotMolWeight().toString());
			mol.setProperty("Lot Number", Integer.toString(lotDTO.getLotNumber()));
			if (lotDTO.getMeltingPoint() != null) mol.setProperty("Melting Point", lotDTO.getMeltingPoint().toString());
			if (lotDTO.getModifiedBy() != null) mol.setProperty("Modified By", lotDTO.getModifiedBy());
			if (lotDTO.getModifiedDate() != null) mol.setProperty("Modified Date", dateFormat.format(lotDTO.getModifiedDate()));
			if (lotDTO.getNotebookPage() != null) mol.setProperty("Notebook Page", lotDTO.getNotebookPage());
			if (lotDTO.getPercentEE() != null) mol.setProperty("Percent EE", lotDTO.getPercentEE().toString());
			if (lotDTO.getPhysicalStateCode() != null) mol.setProperty("Physical State Code", lotDTO.getPhysicalStateCode());
			if (lotDTO.getProject() != null) mol.setProperty("Project", lotDTO.getProject());
			if (lotDTO.getPurity() != null) mol.setProperty("Purity", lotDTO.getPurity().toString());
			if (lotDTO.getPurityMeasuredByCode() != null) mol.setProperty("Purity Measured By Code", lotDTO.getPurityMeasuredByCode());
			if (lotDTO.getPurityOperatorCode() != null) mol.setProperty("Purity Operator Code", lotDTO.getPurityOperatorCode());
			if (lotDTO.getRegistrationDate() != null) mol.setProperty("Registration Date", dateFormat.format(lotDTO.getRegistrationDate()));
			if (lotDTO.getRetain() != null) mol.setProperty("Retain", lotDTO.getRetain().toString());
			if (lotDTO.getRetainLocation() != null) mol.setProperty("Retain Location", lotDTO.getRetainLocation());
			if (lotDTO.getRetainUnitsCode() != null) mol.setProperty("Retain Units Code", lotDTO.getRetainUnitsCode());
			if (lotDTO.getLotRegisteredBy() != null) mol.setProperty("Lot Registered By", lotDTO.getLotRegisteredBy());
			if (lotDTO.getSolutionAmount() != null) mol.setProperty("Solution Amount", lotDTO.getSolutionAmount().toString());
			if (lotDTO.getSolutionAmountUnitsCode() != null) mol.setProperty("Solution Amount Units Code", lotDTO.getSolutionAmountUnitsCode());
			if (lotDTO.getStockLocation() != null) mol.setProperty("Stock Location", lotDTO.getStockLocation());
			if (lotDTO.getStockSolvent() != null) mol.setProperty("Stock Solvent", lotDTO.getStockSolvent());
			if (lotDTO.getSupplier() != null) mol.setProperty("Supplier", lotDTO.getSupplier());
			if (lotDTO.getSupplierID() != null) mol.setProperty("Supplier ID", lotDTO.getSupplierID());
			if (lotDTO.getSupplierLot() != null) mol.setProperty("Supplier Lot", lotDTO.getSupplierLot());
			if (lotDTO.getSynthesisDate() != null) mol.setProperty("Synthesis Date", dateFormat.format(lotDTO.getSynthesisDate()));
			if (lotDTO.getVendorCode() != null) mol.setProperty("Vendor Code", lotDTO.getVendorCode());
			if (lotDTO.getSaltFormCorpName() != null) mol.setProperty("Salt Form Corp Name", lotDTO.getSaltFormCorpName());
			if (lotDTO.getCasNumber() != null) mol.setProperty("CAS Number", lotDTO.getCasNumber());
			if (lotDTO.getSaltAbbrevs() != null) mol.setProperty("Salt Abbrevs", lotDTO.getSaltAbbrevs());
			if (lotDTO.getSaltEquivalents() != null) mol.setProperty("Salt Equivalents", lotDTO.getSaltEquivalents());
			if (lotDTO.getParentCorpName() != null) mol.setProperty("Parent Corp Name", lotDTO.getParentCorpName());
			mol.setProperty("Parent Number", Long.toString(lotDTO.getParentNumber()));
			if (lotDTO.getParentCommonName() != null) mol.setProperty("Parent Common Name", lotDTO.getParentCommonName());
			if (lotDTO.getParentStereoCategory() != null) mol.setProperty("Parent Stereo Category", lotDTO.getParentStereoCategory());
			if (lotDTO.getParentStereoComment() != null) mol.setProperty("Parent Stereo Comment", lotDTO.getParentStereoComment());
			if (lotDTO.getParentMolWeight() != null) mol.setProperty("Parent Mol Weight", lotDTO.getParentMolWeight().toString());
			if (lotDTO.getParentExactMass() != null) mol.setProperty("Parent Exact Mass", lotDTO.getParentExactMass().toString());
			if (lotDTO.getParentMolFormula() != null) mol.setProperty("Parent Mol Formula", lotDTO.getParentMolFormula());
			if (lotDTO.getParentRegistrationDate() != null) mol.setProperty("Parent Registration Date", dateFormat.format(lotDTO.getParentRegistrationDate()));
			if (lotDTO.getParentRegisteredBy() != null) mol.setProperty("Parent Registered By", lotDTO.getParentRegisteredBy());
			if (lotDTO.getParentModifiedDate() != null) mol.setProperty("Parent Modified Date", dateFormat.format(lotDTO.getParentModifiedDate()));
			if (lotDTO.getParentModifiedBy() != null) mol.setProperty("Parent Modified By", lotDTO.getParentModifiedBy());
			if (lotDTO.getParentAliases() != null) mol.setProperty("Parent Aliases", lotDTO.getParentAliases());
			if (lotDTO.getParentAnnotationCode() != null) mol.setProperty("Parent Annotation Code", lotDTO.getParentAnnotationCode());
			if (lotDTO.getParentCompoundTypeCode() != null) mol.setProperty("Parent Compound Type Code", lotDTO.getParentCompoundTypeCode());
			if (lotDTO.getParentComment() != null) mol.setProperty("Parent Comment", lotDTO.getParentComment());
			if (lotDTO.getParentIsMixture() != null) mol.setProperty("Parent Is Mixture", lotDTO.getParentIsMixture().toString());
			exporter.write(mol);
		}
		return lotDTOs.size();
	}

	
}

