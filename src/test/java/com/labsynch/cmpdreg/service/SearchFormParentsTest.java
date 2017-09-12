package com.labsynch.cmpdreg.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.BulkLoadFile;
import com.labsynch.cmpdreg.domain.BulkLoadTemplate;
import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.dto.BulkLoadPropertiesDTO;
import com.labsynch.cmpdreg.dto.BulkLoadPropertyMappingDTO;
import com.labsynch.cmpdreg.dto.BulkLoadRegisterSDFRequestDTO;
import com.labsynch.cmpdreg.dto.BulkLoadRegisterSDFResponseDTO;
import com.labsynch.cmpdreg.dto.BulkLoadSDFPropertyRequestDTO;
import com.labsynch.cmpdreg.dto.PurgeFileResponseDTO;
import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;
import com.labsynch.cmpdreg.utils.Configuration;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class SearchFormParentsTest {

	private static final Logger logger = LoggerFactory.getLogger(SearchFormParentsTest.class);

	@Autowired
	private SearchFormService searchFormService;
	
	@Before
	public void loginTestUser() {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("cchemist", "cchemist"));
	}

	
	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();
	private static final boolean isUnitTestDB = mainConfig.getServerSettings().isUnitTestDB();
	private static final boolean isInitalDBLoad = mainConfig.getServerSettings().isInitalDBLoad();
	
	
	//options for outputFormat -- corpname, cdid, corpname-cdid, sdf ; default is cdid

	@Test
	public void findParents1() throws IOException, CmpdRegMolFormatException{
				
		Parent parent = Parent.findParent(50780L);

		String molStructure = parent.getMolStructure();
		Integer maxResults = 10;
		Float similarity = 0.1F;
		String searchType = "FULL";
		String outputFormat = "cdid";
		String results = searchFormService.findParentIds(molStructure, maxResults, similarity, searchType, outputFormat );
		logger.info("results\n" + results);
		
		results = searchFormService.findParentIds(molStructure, maxResults, similarity, searchType, outputFormat );
		logger.info("results\n" + results);

		results = searchFormService.findParentIds(molStructure, maxResults, similarity, searchType, outputFormat );
		logger.info("results\n" + results);

		results = searchFormService.findParentIds(molStructure, maxResults, similarity, searchType, outputFormat );
		logger.info("results\n" + results);

	}

}


