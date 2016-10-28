package com.labsynch.cmpdreg.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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
import com.labsynch.cmpdreg.dto.PreferredNameDTO;
import com.labsynch.cmpdreg.service.BulkLoadService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config.xml"})
@Transactional
public class ApiPreferredIdControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiPreferredIdControllerTest.class);
	
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
    @Transactional
    public void getBatchPreferredName() throws Exception {
    	PreferredNameDTO one = new PreferredNameDTO();
		one.setRequestName("CMPD-0000001-001");
		PreferredNameDTO two = new PreferredNameDTO();
		two.setRequestName("CMPD-0000002-001");
		PreferredNameDTO three = new PreferredNameDTO();
		three.setRequestName("Not-A-Lot-Corp-Name");
		Collection<PreferredNameDTO> preferredNameDTOs = new ArrayList<PreferredNameDTO>();
		preferredNameDTOs.add(one);
		preferredNameDTOs.add(two);
		preferredNameDTOs.add(three);
    	String json = PreferredNameDTO.toJsonArray(preferredNameDTOs);  	
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/getPreferredName")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	preferredNameDTOs = PreferredNameDTO.fromJsonArrayToPreferredNameDTO(responseJson);
    	for (PreferredNameDTO preferredNameDTO : preferredNameDTOs){
    		if (preferredNameDTO.getRequestName().contains("Not-A-")){
    			Assert.assertEquals("", preferredNameDTO.getPreferredName());
    		}else{
    			Assert.assertEquals(preferredNameDTO.getRequestName(), preferredNameDTO.getPreferredName());
    		}
    	}
    	
    }
    
    @Test
    @Transactional
    public void getParentPreferredName() throws Exception {
    	PreferredNameDTO one = new PreferredNameDTO();
		one.setRequestName("CMPD-0000001");
		PreferredNameDTO two = new PreferredNameDTO();
		two.setRequestName("CMPD-0000002");
		PreferredNameDTO three = new PreferredNameDTO();
		three.setRequestName("Not-A-Parent-Corp-Name");
		Collection<PreferredNameDTO> preferredNameDTOs = new ArrayList<PreferredNameDTO>();
		preferredNameDTOs.add(one);
		preferredNameDTOs.add(two);
		preferredNameDTOs.add(three);
    	String json = PreferredNameDTO.toJsonArray(preferredNameDTOs);  	
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/getPreferredName/parent")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	preferredNameDTOs = PreferredNameDTO.fromJsonArrayToPreferredNameDTO(responseJson);
    	for (PreferredNameDTO preferredNameDTO : preferredNameDTOs){
    		if (preferredNameDTO.getRequestName().contains("Not-A-")){
    			Assert.assertEquals("", preferredNameDTO.getPreferredName());
    		}else{
    			Assert.assertEquals(preferredNameDTO.getRequestName(), preferredNameDTO.getPreferredName());
    		}
    	}
    	
    }

}