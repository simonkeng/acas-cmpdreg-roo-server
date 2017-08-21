package com.labsynch.cmpdreg.chemclasses.indigo;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.annotation.Transactional;

import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoException;
import com.epam.indigo.IndigoObject;
import com.labsynch.cmpdreg.chemclasses.CmpdRegMolecule;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.Salt;
import com.labsynch.cmpdreg.dto.MolConvertOutputDTO;
import com.labsynch.cmpdreg.dto.StrippedSaltDTO;
import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;
import com.labsynch.cmpdreg.service.ChemStructureService;
import com.labsynch.cmpdreg.utils.Configuration;


public class ChemStructureServiceIndigoImpl implements ChemStructureService {

	Logger logger = LoggerFactory.getLogger(ChemStructureServiceIndigoImpl.class);

	private boolean shouldCloseConnection = false;

	public void setShouldCloseConnection(boolean shouldCloseConnection)
	{
		this.shouldCloseConnection = shouldCloseConnection;
	}

	public boolean getShouldCloseConnection()
	{
		return shouldCloseConnection;
	}

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate()
	{
		return jdbcTemplate;
	}

	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();
	private static String exactSearchDef = mainConfig.getServerSettings().getExactMatchDef();
	private static long maxSearchTime = mainConfig.getServerSettings().getMaxSearchTime();
	private static int maxSearchResults = mainConfig.getServerSettings().getMaxSearchResults();
	private static boolean useStandardizer = mainConfig.getServerSettings().isUseExternalStandardizerConfig();
	private static String standardizerConfigFilePath = mainConfig.getServerSettings().getStandardizerConfigFilePath();
	
	private Indigo indigo = new Indigo();
	
	@Override
	public int getCount(String structureTable) {
		String sql = "select count(*) from " + structureTable;
		int count;
		Integer countInt = jdbcTemplate.queryForObject(sql, Integer.class);
		if (countInt == null){
			count = 0;
		} else {
			count = countInt;
		}

		return count;
	}

	@Override
	public boolean compareStructures(String preMolStruct, String postMolStruct, String searchType){


		//logger.info("SearchType is: " + searchType);
		boolean compoundsMatch = false;
		try {
			IndigoObject queryMol = indigo.loadMolecule(preMolStruct);
			IndigoObject targetMol = indigo.loadMolecule(preMolStruct);

			compoundsMatch = (indigo.exactMatch(queryMol, targetMol, "ALL") != null);
			if (!compoundsMatch){
				logger.info(queryMol.smiles());
				logger.info(targetMol.smiles());				
			}

		} catch (IndigoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return compoundsMatch;
	}



	@Override
	public String standardizeStructure(String molfile){
		//Call method below after parsing molfile
		CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(molfile);
		return standardizeMolecule(molWrapper.molecule).molfile();
	}


	public IndigoObject standardizeMolecule(IndigoObject molecule) {
		//Indigo standardizer configs
		logger.error("Standardizer with Indigo chemistry services is not implemented!");
//		indigo.setOption("standardize-stereo", true);
		molecule.standardize();		
		return molecule;
	}



	@Override
	public int saveStructure(String molfile, String structureTable) {
		boolean checkForDupes = false;
		return saveStructure(molfile, structureTable, checkForDupes);
	}

	@Override
	public int saveStructure(String molfile, String structureTable, boolean checkForDupes) {
		return 0;
	}

	@Override
	public void closeConnection() {
		//do nothing
	}


	@Override
	public int[] searchMolStructures(String molfile, String structureTable, String searchType) {
		String plainTable = null;
		if (structureTable.equalsIgnoreCase("Parent_Structure")){
			plainTable = "parent";
		} else if (structureTable.equalsIgnoreCase("SaltForm_Structure")){
			plainTable = "salt_form";
		} else if (structureTable.equalsIgnoreCase("Salt_Structure")){
			plainTable = "salt";
		}



		return searchMolStructures(molfile, structureTable, plainTable, searchType);	
	}

	@Override
	public int[] searchMolStructures(String molfile, String structureTable, String searchType, Float simlarityPercent) {
		return searchMolStructures(molfile, structureTable, null,searchType, simlarityPercent);	
	}

	@Override
	public int[] searchMolStructures(String molfile, String structureTable, String plainTable, String searchType) {
		return searchMolStructures(molfile, structureTable, plainTable, searchType, 0f);	
	}

	@Override
	public int[] searchMolStructures(String molfile, String structureTable, String plainTable, String searchType, Float simlarityPercent) {
		int maxResultCount = maxSearchResults;
		return searchMolStructures(molfile, structureTable, plainTable, searchType, simlarityPercent, maxResultCount);	

	}

	@Override
	@Transactional
	public int[] searchMolStructures(String molfile, String structureTable, String plainTable, String searchType, 
			Float simlarityPercent, int maxResults) {

		Connection conn = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());	
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		long maxTime = maxSearchTime;
		int maxResultCount = maxResults;
		//Indigo: the structures in the plainTable are being used. Ignore structureTable from here on out.
		logger.debug("Search table is  " + plainTable);		
		logger.debug("Search type is  " + searchType);		
		logger.debug("Max number of results is  " + maxResults);		


		if (searchType.equalsIgnoreCase("EXACT")){
			searchType = exactSearchDef;
		}

		try {
			CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(molfile);
			IndigoObject mol = molWrapper.molecule;
			
			String baseQuery = "SELECT id FROM " + plainTable + " WHERE mol_structure @ ";
			String argFormat = null;
			String bingoFunction = null;
			String orderBy = " ORDER BY id";
			
			if (useStandardizer){
				mol = standardizeMolecule(mol);
				mol.aromatize();				
			} else {
				mol.aromatize();				
			}

			logger.debug("definition of exact search: " + exactSearchDef);
			logger.debug("selected search type: " + searchType);
			
			if (searchType.equalsIgnoreCase("SUBSTRUCTURE")) {
				bingoFunction = "::bingo.sub";
				argFormat = "( :queryMol , :parameters )";
			}else if (searchType.equalsIgnoreCase("SIMILARITY")) {
				bingoFunction = "::bingo.sim";
				argFormat = "( :minSimilarity , :maxSimilarity , :queryMol , :metric )";
				orderBy = " ORDER BY "+argFormat+bingoFunction;
			}else { 
				bingoFunction = "::bingo.exact";
				argFormat = "( :queryMol , :parameters )";
			}
			
			EntityManager em = Parent.entityManager();
			Query query = em.createQuery(baseQuery + argFormat + bingoFunction + orderBy);
			
			query.setParameter("queryMol", mol.molfile());
			query.setMaxResults(maxResults);
			
			//May need additional research / decisions around which options to use
			//Basic Indigo search types corresponding to JChem search types
			//CReg: "DUPLICATE" or "DUPLICATE_NO_TAUTOMER" :: JChem: "DUPLICATE", "TAUTOMER_SEARCH_OFF" :: Bingo.Exact, "ALL"
			//CReg: "DUPLICATE_TAUTOMER" :: JChem: "DUPLICATE", "TAUTOMER_SEARCH_ON" :: Bingo.Exact, "TAU"
			//CReg: "STEREO_IGNORE" :: JChem: "STEREO_IGNORE" :: Bingo.Exact "ALL -STE"
			//(NOT SURE ABOUT THIS) CReg: "FULL_TAUTOMER", or default/unrecognized searchType :: JChem: "FULL", "CHARGE_MATCHING_IGNORE", "ISOTOPE_MATCHING_IGNORE", "STEREO_IGNORE", "TAUTOMER_SEARCH_ON" :: Bingo.Exact "TAU ALL -ELE -MAS -STE"
			//CReg: "SUBSTRUCTURE" :: JChem: "SUBSTRUCTURE" :: Bingo.Sub
			//CReg: "SIMILARITY" :: JChem "SIMILARITY" :: Bingo.Sim > $min
			//CReg: "FULL" :: JChem "FULL", "CHARGE_MATCHING_IGNORE", "ISOTOPE_MATCHING_IGNORE", "STEREO_IGNORE", "TAUTOMER_SEARCH_OFF" :: Bingo.Exact "ALL -ELE -MAS -STE"
			
			if (searchType.equalsIgnoreCase("SUBSTRUCTURE")) {
				query.setParameter("parameters", "");
			}else if (searchType.equalsIgnoreCase("SIMILARITY")) {
				query.setParameter("minSimilarity", simlarityPercent);
				query.setParameter("maxSimilarity", null);
				query.setParameter("metric", "Tanimoto");
			}else {
				String parameters = null;
				switch (searchType.toUpperCase()) {
				case "DUPLICATE":
					parameters = "ALL";
					break;
				case "DUPLICATE_TAUTOMER":
					parameters = "TAU";
					break;
				case "STEREO_IGNORE":
					parameters = "ALL -STE";
					break;
				case "FULL_TAUTOMER":
					parameters = "TAU";
					break;
				case "FULL":
					parameters = "ALL -ELE -MAS -STE";
					break;
				default:
					//TODO: this should match FULL_TAUTOMER if possible
					parameters = "ALL";
					break;
				}
				query.setParameter("parameters", parameters);
			}
			if (logger.isDebugEnabled()) logger.debug(query.unwrap(org.hibernate.Query.class).getQueryString());
			//TODO: should do an audit of the search types being used by CReg.
			
			List<Long> idHitList = query.getResultList();
			//TODO: Change return type of this service.
			
			if (hitList.length > 0){
				logger.debug("found a matching molecule!!!  " + hitList.length);
			}

			return hitList;
			
		} catch (CmpdRegMolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		
		return null;
	}


	@Override
	public boolean checkForSalt(String molfile) throws CmpdRegMolFormatException{
		IndigoObject mol = indigo.loadMolecule(molfile);
		int fragCount = mol.getFragCount(MoleculeGraph.FRAG_BASIC);
		boolean foundNonCovalentSalt = false;
		if (fragCount > 1){
			foundNonCovalentSalt = true;
		}

		return foundNonCovalentSalt;

	}

	@Override
	public StrippedSaltDTO stripSalts(CmpdRegMolecule inputStructure){
		Molecule clone = inputStructure.molecule.clone();
		Molecule[] rawFrags = clone.findFrags(Molecule.class, MoleculeGraph.FRAG_BASIC);
		List<Molecule> allFrags = new ArrayList<Molecule>();
		for (Molecule fragment : rawFrags){
			allFrags.add(fragment);
		}
		Map<Salt, Integer> saltCounts = new HashMap<Salt, Integer>();
		Set<Molecule> unidentifiedFragments = new HashSet<Molecule>();
		for (Molecule fragment : allFrags){
			int[] cdIdMatches = searchMolStructures(fragment.toFormat("mol"), "Salt_Structure", "DUPLICATE_TAUTOMER");
			if (cdIdMatches.length>0){
				Salt foundSalt = Salt.findSaltsByCdId(cdIdMatches[0]).getSingleResult();
				if (saltCounts.containsKey(foundSalt)) saltCounts.put(foundSalt, saltCounts.get(foundSalt)+1);
				else saltCounts.put(foundSalt, 1);
			}else{
				unidentifiedFragments.add(fragment);
			}
		}
		StrippedSaltDTO resultDTO = new StrippedSaltDTO();
		resultDTO.setSaltCounts(saltCounts);
		resultDTO.setUnidentifiedFragments(unidentifiedFragments);
		logger.debug("Identified stripped salts:");
		for (Salt salt : saltCounts.keySet()){
			logger.debug("Salt Abbrev: "+salt.getAbbrev());
			logger.debug("Salt Count: "+ saltCounts.get(salt));
		}
		return resultDTO;
	}

	@Override
	@Transactional
	public CmpdRegMolecule[] searchMols(String molfile, String structureTable, int[] inputCdIdHitList, String plainTable, String searchType, Float simlarityPercent) {

		Connection conn = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());	
		ConnectionHandler ch = new ConnectionHandler();
		ch.setConnection(conn);
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		CacheRegistrationUtil cru = null;

		long maxTime = maxSearchTime;
		int maxResultCount = maxSearchResults;

		logger.debug("Search table is  " + structureTable);		
		logger.debug("Search type is  " + searchType);	
		logger.debug("search mol is: " + molfile);

		if (searchType.equalsIgnoreCase("EXACT")){
			searchType = exactSearchDef;
		}

		MolHandler mh = null;
		JChemSearch searcher = null;
		Molecule[] hitList = null;

		try {
			cru = new CacheRegistrationUtil(ch);
			if (structureTable.equalsIgnoreCase("SaltForm_Structure")){
				String cacheIdentifier = "labsynch_saltform_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)){
					cru.registerPermanentCache(cacheIdentifier);	
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);	
			} else if (structureTable.equalsIgnoreCase("Salt_Structure")) {
				String cacheIdentifier = "labsynch_salt_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)){
					cru.registerPermanentCache(cacheIdentifier);	
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);		
			} else if (structureTable.equalsIgnoreCase("QC_Compound_Structure")) {
				String cacheIdentifier = "labsynch_qc_cmpd_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)){
					cru.registerPermanentCache(cacheIdentifier);	
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else {
				String cacheIdentifier = "labsynch_cmpd_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)){
					cru.registerPermanentCache(cacheIdentifier);	
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);				
			}

			String cacheID = CacheRegistrationUtil.getCacheID();
			logger.debug("current cache ID: " + cacheID);
			logger.debug("cache status: " + cru.isCacheIDRegistered(cacheID));

			mh = new MolHandler(molfile);
			Molecule mol = mh.getMolecule();
			mol.aromatize();
			searcher = new JChemSearch();
			searcher.setQueryStructure(mol);
			searcher.setConnectionHandler(ch);
			searcher.setStructureTable(structureTable);
			JChemSearchOptions searchOptions = null;

			logger.debug("definition of exact search: " + exactSearchDef);
			logger.debug("selected search type: " + searchType);


			HitColoringAndAlignmentOptions hitColorOptions = null;
			if (searchType.equalsIgnoreCase("DUPLICATE")){
				searchOptions = new JChemSearchOptions(SearchConstants.DUPLICATE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);
				logger.debug("selected DUPLICATE search for " + searchType);

			}else if (searchType.equalsIgnoreCase("DUPLICATE_TAUTOMER")){
				System.out.println("Search type is DUPLICATE_TAUTOMER");		
				searchOptions = new JChemSearchOptions(SearchConstants.DUPLICATE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_ON);

			}else if (searchType.equalsIgnoreCase("DUPLICATE_NO_TAUTOMER")){
				System.out.println("Search type is DUPLICATE_NO_TAUTOMER");		
				searchOptions = new JChemSearchOptions(SearchConstants.DUPLICATE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);

			}else if (searchType.equalsIgnoreCase("STEREO_IGNORE")){
				System.out.println("Search type is  no stereo");		
				searchOptions = new JChemSearchOptions(SearchConstants.STEREO_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);

			} else if (searchType.equalsIgnoreCase("FULL_TAUTOMER")){
				System.out.println("Search type is exact FULL_TAUTOMER");		
				searchOptions = new JChemSearchOptions(SearchConstants.FULL);
				searchOptions.setChargeMatching(JChemSearchOptions.CHARGE_MATCHING_IGNORE);
				searchOptions.setIsotopeMatching(JChemSearchOptions.ISOTOPE_MATCHING_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_ON);

			} else if (searchType.equalsIgnoreCase("SUBSTRUCTURE")){
				System.out.println("Search type is substructure");	
				searchOptions = new JChemSearchOptions(SearchConstants.SUBSTRUCTURE);
				// One can also specify coloring and alignment options 
				hitColorOptions = new HitColoringAndAlignmentOptions();
				boolean coloringEnabled = true;
				Color hitColor = Color.blue;
				Color nonHitColor = Color.black;
				hitColorOptions.coloring = true;
				hitColorOptions.hitColor = hitColor;
				hitColorOptions.nonHitColor = nonHitColor;						
				//				hitColorOptions.setColoringEnabled(coloringEnabled);
				//				hitColorOptions.setHitColor(hitColor);
				//				hitColorOptions.setNonHitColor(nonHitColor);

			} else if (searchType.equalsIgnoreCase("SIMILARITY")){
				searchOptions = new JChemSearchOptions(SearchConstants.SIMILARITY);
				searchOptions.setDissimilarityThreshold(simlarityPercent);
				//				searchOptions.setMaxTime(maxTime);
				hitColorOptions = new HitColoringAndAlignmentOptions();	
				boolean coloringEnabled = true;		
				Color hitColor = Color.blue;
				Color nonHitColor = Color.black;
				//				hitColorOptions.coloring = true;
				//				hitColorOptions.hitColor = hitColor;
				//				hitColorOptions.nonHitColor = nonHitColor;					
				hitColorOptions.setColoringEnabled(coloringEnabled);
				hitColorOptions.setHitColor(hitColor);
				hitColorOptions.setNonHitColor(nonHitColor);

			} else if (searchType.equalsIgnoreCase("FULL")){
				logger.debug("Default Search type is full with no tautomer search");		
				searchOptions = new JChemSearchOptions(SearchConstants.FULL);
				searchOptions.setChargeMatching(JChemSearchOptions.CHARGE_MATCHING_IGNORE);
				searchOptions.setIsotopeMatching(JChemSearchOptions.ISOTOPE_MATCHING_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);
			} else {
				logger.debug("Default Search type is exact");		
				searchOptions = new JChemSearchOptions(SearchConstants.FULL);
				searchOptions.setChargeMatching(JChemSearchOptions.CHARGE_MATCHING_IGNORE);
				searchOptions.setIsotopeMatching(JChemSearchOptions.ISOTOPE_MATCHING_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);
			}

			if (inputCdIdHitList != null && inputCdIdHitList.length > 0){
				searcher.setFilterIDList(inputCdIdHitList);		
			} 
			else if (plainTable != null && inputCdIdHitList == null && searchType.equalsIgnoreCase("SUBSTRUCTURE")){
				searchOptions.setFilterQuery("select cd_id from "+ plainTable + " where id > 0");				
			}

			logger.debug("max result count is: " + maxResultCount );
			logger.debug("JChemSearch.ORDERING_BY_ID_OR_SIMILARITY: " + JChemSearch.ORDERING_BY_ID_OR_SIMILARITY );
			logger.debug("JChemSearch.RUN_MODE_SYNCH_COMPLETE: " + JChemSearch.RUN_MODE_SYNCH_COMPLETE );

			searchOptions.setMaxResultCount(maxResultCount);
			searcher.setOrder(JChemSearch.ORDERING_BY_ID_OR_SIMILARITY);
			searcher.setSearchOptions(searchOptions);
			searcher.setRunMode(JChemSearch.RUN_MODE_SYNCH_COMPLETE);
			searcher.run();

			// Specifying database fields to retrieve
			List<String> fieldNames = new ArrayList<String>(); 
			fieldNames.add("cd_id"); 
			fieldNames.add("cd_formula"); 
			fieldNames.add("cd_molweight");

			// ArrayList for returned database field values
			//			List<Object[]> fieldValues = new ArrayList<Object[]>();
			List<Object[]> fieldValues = new ArrayList<Object[]>();

			// Retrieving result molecules // filedValues will be also filled!

			int[] hitList_cdId = searcher.getResults();

			logger.info("Found number of hits: " + hitList_cdId.length);

			hitList = searcher.getHitsAsMolecules(hitList_cdId, hitColorOptions, fieldNames, fieldValues);

			for (int i = 0; i < hitList_cdId.length; i++) { 
				System.out.println("ID: " + hitList_cdId[i]);
				hitList[i].setProperty("cd_id", Integer.toString(hitList_cdId[i]));
				Object[] values = (Object[]) fieldValues.get(i); 
				for (int j = 0; j < values.length; j++) {
					System.out.println(fieldNames.get(j) + ": " + values[j]);
				}
				System.out.println();
			}


			if (hitList.length > 0){
				logger.debug("found a matching molecule!!!  " + hitList.length);
			}

			if (this.shouldCloseConnection) {
				ch.close();
				conn.close();
			}

		} catch (MolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseSearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SupergraphException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




		return hitList;
	}

	@Override
	@Transactional
	public CmpdRegMolecule[] searchMols(String molfile, String structureTable, int[] inputCdIdHitList, 
			String plainTable, String searchType, Float simlarityPercent, int maxResults) {

		Connection conn = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());	
		ConnectionHandler ch = new ConnectionHandler();
		ch.setConnection(conn);
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		CacheRegistrationUtil cru = null;

		long maxTime = maxSearchTime;
		int maxResultCount = maxResults;

		logger.debug("Search table is  " + structureTable);		
		logger.debug("Search type is  " + searchType);	
		logger.debug("search mol is: " + molfile);

		if (searchType.equalsIgnoreCase("EXACT")){
			searchType = exactSearchDef;
		}

		MolHandler mh = null;
		JChemSearch searcher = null;
		Molecule[] hitList = null;

		try {
			cru = new CacheRegistrationUtil(ch);
			if (structureTable.equalsIgnoreCase("SaltForm_Structure")){
				String cacheIdentifier = "labsynch_saltform_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)){
					cru.registerPermanentCache(cacheIdentifier);	
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);	
			} else if (structureTable.equalsIgnoreCase("Salt_Structure")) {
				String cacheIdentifier = "labsynch_salt_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)){
					cru.registerPermanentCache(cacheIdentifier);	
				}	
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);				
			} else if (structureTable.equalsIgnoreCase("QC_Compound_Structure")) {
				String cacheIdentifier = "labsynch_qc_cmpd_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)){
					cru.registerPermanentCache(cacheIdentifier);	
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else {
				String cacheIdentifier = "labsynch_cmpd_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)){
					cru.registerPermanentCache(cacheIdentifier);	
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);				
			}

			String cacheID = CacheRegistrationUtil.getCacheID();
			logger.debug("current cache ID: " + cacheID);
			logger.debug("cache status: " + cru.isCacheIDRegistered(cacheID));

			mh = new MolHandler(molfile);
			Molecule mol = mh.getMolecule();
			mol.aromatize();
			searcher = new JChemSearch();
			searcher.setQueryStructure(mol);
			searcher.setConnectionHandler(ch);
			searcher.setStructureTable(structureTable);
			JChemSearchOptions searchOptions = null;

			logger.debug("definition of exact search: " + exactSearchDef);
			logger.debug("selected search type: " + searchType);


			HitColoringAndAlignmentOptions hitColorOptions = null;
			if (searchType.equalsIgnoreCase("DUPLICATE")){
				searchOptions = new JChemSearchOptions(SearchConstants.DUPLICATE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);
				logger.debug("selected DUPLICATE search for " + searchType);

			}else if (searchType.equalsIgnoreCase("DUPLICATE_TAUTOMER")){
				System.out.println("Search type is DUPLICATE_TAUTOMER");		
				searchOptions = new JChemSearchOptions(SearchConstants.DUPLICATE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_ON);

			}else if (searchType.equalsIgnoreCase("DUPLICATE_NO_TAUTOMER")){
				System.out.println("Search type is DUPLICATE_NO_TAUTOMER");		
				searchOptions = new JChemSearchOptions(SearchConstants.DUPLICATE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);

			}else if (searchType.equalsIgnoreCase("STEREO_IGNORE")){
				System.out.println("Search type is  no stereo");		
				searchOptions = new JChemSearchOptions(SearchConstants.STEREO_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);

			} else if (searchType.equalsIgnoreCase("FULL_TAUTOMER")){
				System.out.println("Search type is exact FULL_TAUTOMER");		
				searchOptions = new JChemSearchOptions(SearchConstants.FULL);
				searchOptions.setChargeMatching(JChemSearchOptions.CHARGE_MATCHING_IGNORE);
				searchOptions.setIsotopeMatching(JChemSearchOptions.ISOTOPE_MATCHING_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_ON);

			} else if (searchType.equalsIgnoreCase("SUBSTRUCTURE")){
				System.out.println("Search type is substructure");	
				searchOptions = new JChemSearchOptions(SearchConstants.SUBSTRUCTURE);
				// One can also specify coloring and alignment options 
				hitColorOptions = new HitColoringAndAlignmentOptions();
				boolean coloringEnabled = true;
				Color hitColor = Color.blue;
				Color nonHitColor = Color.black;
				hitColorOptions.coloring = true;
				hitColorOptions.hitColor = hitColor;
				hitColorOptions.nonHitColor = nonHitColor;						
				//				hitColorOptions.setColoringEnabled(coloringEnabled);
				//				hitColorOptions.setHitColor(hitColor);
				//				hitColorOptions.setNonHitColor(nonHitColor);

			} else if (searchType.equalsIgnoreCase("SIMILARITY")){
				searchOptions = new JChemSearchOptions(SearchConstants.SIMILARITY);
				searchOptions.setDissimilarityThreshold(simlarityPercent);
				//				searchOptions.setMaxTime(maxTime);
				hitColorOptions = new HitColoringAndAlignmentOptions();	
				boolean coloringEnabled = true;		
				Color hitColor = Color.blue;
				Color nonHitColor = Color.black;
				//				hitColorOptions.coloring = true;
				//				hitColorOptions.hitColor = hitColor;
				//				hitColorOptions.nonHitColor = nonHitColor;					
				hitColorOptions.setColoringEnabled(coloringEnabled);
				hitColorOptions.setHitColor(hitColor);
				hitColorOptions.setNonHitColor(nonHitColor);

			} else if (searchType.equalsIgnoreCase("FULL")){
				logger.debug("Default Search type is full with no tautomer search");		
				searchOptions = new JChemSearchOptions(SearchConstants.FULL);
				searchOptions.setChargeMatching(JChemSearchOptions.CHARGE_MATCHING_IGNORE);
				searchOptions.setIsotopeMatching(JChemSearchOptions.ISOTOPE_MATCHING_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);
			} else {
				logger.debug("Default Search type is exact");		
				searchOptions = new JChemSearchOptions(SearchConstants.FULL);
				searchOptions.setChargeMatching(JChemSearchOptions.CHARGE_MATCHING_IGNORE);
				searchOptions.setIsotopeMatching(JChemSearchOptions.ISOTOPE_MATCHING_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);
			}

			if (inputCdIdHitList != null && inputCdIdHitList.length > 0){
				searcher.setFilterIDList(inputCdIdHitList);		
			} 
			else if (plainTable != null && inputCdIdHitList == null && searchType.equalsIgnoreCase("SUBSTRUCTURE")){
				searchOptions.setFilterQuery("select cd_id from "+ plainTable + " where id > 0");				
			}

			logger.debug("max result count is: " + maxResultCount );
			logger.debug("JChemSearch.ORDERING_BY_ID_OR_SIMILARITY: " + JChemSearch.ORDERING_BY_ID_OR_SIMILARITY );
			logger.debug("JChemSearch.RUN_MODE_SYNCH_COMPLETE: " + JChemSearch.RUN_MODE_SYNCH_COMPLETE );

			searchOptions.setMaxResultCount(maxResultCount);
			searcher.setOrder(JChemSearch.ORDERING_BY_ID_OR_SIMILARITY);
			searcher.setSearchOptions(searchOptions);
			searcher.setRunMode(JChemSearch.RUN_MODE_SYNCH_COMPLETE);
			searcher.run();

			// Specifying database fields to retrieve
			List<String> fieldNames = new ArrayList<String>(); 
			fieldNames.add("cd_id"); 
			fieldNames.add("cd_formula"); 
			fieldNames.add("cd_molweight");

			// ArrayList for returned database field values
			//			List<Object[]> fieldValues = new ArrayList<Object[]>();
			List<Object[]> fieldValues = new ArrayList<Object[]>();

			// Retrieving result molecules // filedValues will be also filled!

			int[] hitList_cdId = searcher.getResults();


			hitList = searcher.getHitsAsMolecules(hitList_cdId, hitColorOptions, fieldNames, fieldValues);

			for (int i = 0; i < hitList_cdId.length; i++) { 
				System.out.println("ID: " + hitList_cdId[i]);
				hitList[i].setProperty("cd_id", Integer.toString(hitList_cdId[i]));
				Object[] values = (Object[]) fieldValues.get(i); 
				for (int j = 0; j < values.length; j++) {
					System.out.println(fieldNames.get(j) + ": " + values[j]);
				}
				System.out.println();
			}


			if (hitList.length > 0){
				logger.debug("found a matching molecule!!!  " + hitList.length);
			}

			if (this.shouldCloseConnection) {
				ch.close();
				conn.close();
			}

		} catch (MolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseSearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SupergraphException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




		return hitList;
	}


	@Override
	public CmpdRegMolecule toMolecule(String molStructure) {
		boolean badStructureFlag = false;
		IndigoObject mol = null;
		String lineEnd = System.getProperty("line.separator");
		try {
			mol = indigo.loadMolecule(molStructure);	
		} catch (IndigoException e1) {
			logger.debug("failed first attempt: bad mol structure: " + molStructure);
			// clean up the molString and try again
			try {
				molStructure = new StringBuilder().append(lineEnd).append(molStructure).append(lineEnd).toString();
				mol = indigo.loadMolecule(molStructure);
			} catch (IndigoException e2) {
				logger.debug("failed second attempt: bad mol structure: " + molStructure);
				badStructureFlag = true;
				logger.error("bad mol structure: " + molStructure);
			}
		}	

		if (!badStructureFlag){
			return (CmpdRegMolecule) mol;
		} else {
			return null;
		}
	}

	@Override
	public String toMolfile(String molStructure) {
		logger.debug("here is the incoming molStructure: " +  molStructure);
		MolHandler mh = null;
		boolean badStructureFlag = false;
		Molecule mol = null;
		String lineEnd = System.getProperty("line.separator");
		try {
			//molStructure = new StringBuilder().append(lineEnd).append(molStructure).append(lineEnd).toString();
			mh = new MolHandler(molStructure);
			mol = mh.getMolecule();
			mol.dearomatize();
		} catch (MolFormatException e1) {
			logger.debug("failed first attempt: bad mol structure: " + molStructure);
			// clean up the molString and try again
			try {
				molStructure = new StringBuilder().append(lineEnd).append(molStructure).append(lineEnd).toString();
				mh = new MolHandler(molStructure);
				mol = mh.getMolecule();
				mol.dearomatize();				
			} catch (MolFormatException e2) {
				logger.debug("failed second attempt: bad mol structure: " + molStructure);
				badStructureFlag = true;
				logger.error("bad mol structure: " + molStructure);
			}
		}
		logger.debug("The badStructureFlag " + badStructureFlag);
		logger.debug("The molfile " + mol.toFormat("mol"));

		if (!badStructureFlag){
			return mol.toFormat("mol");
		} else {
			return molStructure;
		}
	}

	@Override
	public String getCipStereo(String structure) throws IOException{	
		//specific MarvinJS method
		return null;
	}

	@Override
	public String hydrogenizeMol(String structure, String inputFormat, String method) throws IOException{	
		//specific MarvinJS method
		return null;
	}

	@Override
	public MolConvertOutputDTO cleanStructure(String structure, int dim, String opts) throws IOException {				
		//specific MarvinJS method
		return null;
	}

	@Override
	public MolConvertOutputDTO toFormat(String structure, String inputFormat, String outputFormat) throws IOException {				
		//specific MarvinJS method
		//TODO: see if this might serve both Ketcher and MarvinJS
		return null;
	}

	@Override
	public String toInchi(String molStructure) {
		boolean badStructureFlag = false;
		IndigoObject mol = null;
		try {
			mol = indigo.loadMolecule(molStructure);			
		} catch (IndigoException e) {
			badStructureFlag = true;
		}

		if (!badStructureFlag){
			return mol.toFormat("inchi");
		} else {
			return molStructure;
		}
	}

	@Override
	public String toSmiles(String molStructure) {
		boolean badStructureFlag = false;
		IndigoObject mol = null;
		try {
			mol = indigo.loadMolecule(molStructure);			
		} catch (IndigoException e) {
			badStructureFlag = true;
		}

		if (!badStructureFlag){
			return mol.smiles();
		} else {
			return molStructure;
		}
	}

	@Override
	public double getMolWeight(String molStructure) {
		CmpdRegMoleculeIndigoImpl mol = new CmpdRegMoleculeIndigoImpl(molStructure);
		return mol.getMass();
	}

	@Override
	public double getExactMass(String molStructure) {
		CmpdRegMoleculeIndigoImpl mol = new CmpdRegMoleculeIndigoImpl(molStructure);
		return mol.getExactMass();
	}

	@Override
	public  String getMolFormula(String molStructure) {
		CmpdRegMoleculeIndigoImpl mol = new CmpdRegMoleculeIndigoImpl(molStructure);
		return mol.getFormula();
	}

	@Override
	public boolean createJChemTable(String tableName, boolean tautomerDupe) {
		//no-op
		return false;
	}	

	@Override
	@Transactional
	public boolean dropJChemTable(String tableName) {
		//no-op
		return false;
	}	

	@Override
	@Transactional
	public boolean deleteAllJChemTableRows(String tableName) {
		//no-op
		return false;
	}	

	@Override
	public boolean deleteJChemTableRows(String tableName, int[] cdIds) {
		//no-op
		return false;
	}	

	@Override
	public boolean createJchemPropertyTable() {
		//no-op
		return false;
	}

	@Override
	public int[] checkDupeMol(String molStructure, String structureTable, String plainTable) {

		return searchMolStructures(molStructure, structureTable, plainTable, "DUPLICATE_TAUTOMER"); 
	}

	@Override
	public boolean updateStructure(String molStructure, String structureTable, int cdId) {
		//no-op
		return true;
	}	

	@Override
	public boolean updateStructure(CmpdRegMolecule mol, String structureTable, int cdId) {
		//no-op
		return true;
	}

	@Override
	public boolean deleteStructure(String structureTable, int cdId){
		//no-op
		return true;
	}

	@Override
	public boolean standardizedMolCompare(String queryMol, String targetMol) {
		// TODO Auto-generated method stub
		return false;
	}
}

