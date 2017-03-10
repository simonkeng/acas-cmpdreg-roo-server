package com.labsynch.cmpdreg.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.HashSet;

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

import com.labsynch.cmpdreg.dto.LotDTO;
import com.labsynch.cmpdreg.dto.ReparentLotDTO;
import com.labsynch.cmpdreg.service.BulkLoadService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config.xml"})
@Transactional
public class ApiParentLotControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiParentLotControllerTest.class);
	
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
    public void updateLotMetaSingle() throws Exception {
    	
		LotDTO lotDTO = new LotDTO();
		lotDTO.setLotCorpName("CMPD-0000001-001");
		lotDTO.setColor("blue 105");
		lotDTO.setBarcode("barcode TEST 105");
		lotDTO.setLotAliases("sierra;yosemite;yellowstone");
		String json = lotDTO.toJson();

    	logger.debug(json);
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/parentLot/updateLot/metadata")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	
    }
    
    @Test
    @Transactional
    public void updateLotMetaArray() throws Exception {
    	
    	Collection<LotDTO> lotDTOCollection = new HashSet<LotDTO>();
		LotDTO lotDTO = new LotDTO();
		lotDTO.setLotCorpName("CMPD-0000001-001");
		lotDTO.setColor("blue 105");
		lotDTO.setBarcode("barcode TEST 105");
		lotDTO.setLotAliases("sierra;yosemite;yellowstone");
		lotDTOCollection.add(lotDTO);
		String json = LotDTO.toJsonArray(lotDTOCollection);

    	logger.debug(json);
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/parentLot/updateLot/metadata/jsonArray")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);

    }
    
    @Test
    @Transactional
    public void reparentLotSingle() throws Exception {
		ReparentLotDTO lotDTO = new ReparentLotDTO();
		lotDTO.setLotCorpName("CMPD-0000002-001");
		lotDTO.setParentCorpName("CMPD-0000001");
		String json = lotDTO.toJson();

    	logger.debug(json);
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/parentLot/reparentLot")
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