package com.labsynch.cmpdreg.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.labsynch.cmpdreg.dto.BulkLoadPropertiesDTO;
import com.labsynch.cmpdreg.dto.BulkLoadPropertyMappingDTO;
import com.labsynch.cmpdreg.dto.BulkLoadRegisterSDFRequestDTO;
import com.labsynch.cmpdreg.dto.BulkLoadSDFPropertyRequestDTO;
import com.labsynch.cmpdreg.service.BulkLoadService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config.xml"})
@Transactional
public class ApiBulkLoadControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiBulkLoadControllerTest.class);
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    
    @Autowired
    BulkLoadService bulkLoadService;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Test
//    @Transactional
//    @Rollback(value=false)
    public void registerComponentParentTest() throws Exception {
    	String fileName = "src/test/resources/nciSample9_with_props.sdf";
    	Collection<BulkLoadPropertyMappingDTO> mappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	mappings.add(new BulkLoadPropertyMappingDTO("CAS Number", "Formula", false, null));
    	mappings.add(new BulkLoadPropertyMappingDTO("Parent Stereo Category", null, true, "unknown"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Chemist", null, true, "cchemist"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Number", null, true, "10"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Synthesis Date", "Lot Synthesis Date", true, "2015-06-24"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Notebook Page", null, true, "A1234-043"));
    	mappings.add(new BulkLoadPropertyMappingDTO("Lot Purity Measured By", null, true, "HPLC"));
    	BulkLoadSDFPropertyRequestDTO propertiesRequestDTO = new BulkLoadSDFPropertyRequestDTO(fileName, 15, null, null, mappings);
    	BulkLoadPropertiesDTO results = bulkLoadService.readSDFPropertiesFromFile(propertiesRequestDTO);
    	mappings = results.getBulkLoadProperties();
    	logger.debug("Trying to register with the following mappings: "+BulkLoadPropertyMappingDTO.toJsonArray(mappings));
    	BulkLoadRegisterSDFRequestDTO registerRequestDTO = new BulkLoadRegisterSDFRequestDTO(fileName, "cchemist", null, mappings);
    	String json = registerRequestDTO.toJson();  	
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/bulkload/registerSdf")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/text"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	
    }

}