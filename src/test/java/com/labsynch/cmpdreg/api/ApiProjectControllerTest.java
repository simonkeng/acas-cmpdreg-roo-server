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

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.dto.BatchProjectDTO;
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
public class ApiProjectControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiProjectControllerTest.class);
	
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
    public void getBatchProjects() throws Exception {
    	Collection<Lot> lots = Lot.findLotEntries(0, 1100);
    	Collection<BatchProjectDTO> requestDTOs = new ArrayList<BatchProjectDTO>();
    	for (Lot lot : lots){
    		BatchProjectDTO requestDTO = new BatchProjectDTO(lot.getCorpName(), null);
    		requestDTOs.add(requestDTO);
    	}
    	String json = BatchProjectDTO.toJsonArray(requestDTOs);
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/projects/getBatchProjects")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
//    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<BatchProjectDTO> responseDTOs = BatchProjectDTO.fromJsonArrayToBatchProes(responseJson);
    	for (BatchProjectDTO responseDTO : responseDTOs){
    		Assert.assertNotNull(responseDTO.getRequestName());
    		Assert.assertNotNull(responseDTO.getProjectCode());
    	}
    }

}