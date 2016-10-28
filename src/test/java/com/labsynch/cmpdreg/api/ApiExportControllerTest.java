package com.labsynch.cmpdreg.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.dto.SearchFormDTO;
import com.labsynch.cmpdreg.dto.SearchFormReturnDTO;
import com.labsynch.cmpdreg.dto.SearchResultExportRequestDTO;
import com.labsynch.cmpdreg.service.SearchFormService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config.xml"})
@Transactional
public class ApiExportControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiExportControllerTest.class);
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    
    @Autowired
    SearchFormService searchFormService;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
	@Test
	@Transactional
    public void exportSearchResults() throws Exception {
		SearchFormDTO searchParams = new SearchFormDTO();
		Scientist chemist = Scientist.findScientistsByCodeEquals("cchemist").getSingleResult();
		searchParams.setChemist(chemist);
		searchParams.setDateFrom("");
		searchParams.setDateTo("");
		searchParams.setSearchType("substructure");
		searchParams.setCorpNameFrom("");
		searchParams.setCorpNameTo("");
		searchParams.setAlias("");
		SearchFormReturnDTO searchResults = searchFormService.findQuerySaltForms(searchParams);
		String filePath = "src/test/resources/test_search_export.sdf";
		SearchResultExportRequestDTO exportRequest = new SearchResultExportRequestDTO(filePath, searchResults);
    	String json = exportRequest.toJson();  	
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/export/searchResults")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	
    }

}