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
import java.util.List;

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
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.StereoCategory;
import com.labsynch.cmpdreg.dto.BatchProjectDTO;
import com.labsynch.cmpdreg.dto.BulkLoadPropertiesDTO;
import com.labsynch.cmpdreg.dto.BulkLoadPropertyMappingDTO;
import com.labsynch.cmpdreg.dto.BulkLoadRegisterSDFRequestDTO;
import com.labsynch.cmpdreg.dto.BulkLoadSDFPropertyRequestDTO;
import com.labsynch.cmpdreg.dto.CodeTableDTO;
import com.labsynch.cmpdreg.dto.ParentDTO;
import com.labsynch.cmpdreg.dto.ParentEditDTO;
import com.labsynch.cmpdreg.dto.ParentValidationDTO;
import com.labsynch.cmpdreg.service.BulkLoadService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config.xml"})
@Transactional
public class ApiParentControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiParentControllerTest.class);
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    
    @Autowired
    BulkLoadService bulkLoadService;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
 //   @Test
    @Transactional
    public void validateParent_noOp() throws Exception {
    	List<Parent> parents = Parent.findParentEntries(0, 2);
    	Parent firstParent = parents.get(0);
    	Parent secondParent = parents.get(1);
    	
    	String json = firstParent.toJson();
    	logger.debug(json);
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/parents/validateParent")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<CodeTableDTO> affectedLots = CodeTableDTO.fromJsonArrayToCoes(responseJson);
    	for (CodeTableDTO lot : affectedLots){
    		Assert.assertNotNull(lot.getCode());
    		Assert.assertNotNull(lot.getName());
    		Assert.assertNotNull(lot.getComments());
    	}
    }
    
//    @Test
    @Transactional
    public void validateParent_changeStereoCategory() throws Exception {
    	List<Parent> parents = Parent.findParentEntries(0, 2);
    	Parent firstParent = parents.get(0);
    	Parent secondParent = parents.get(1);
    	Parent testParent = new Parent();
    	testParent.setMolStructure(firstParent.getMolStructure());
    	testParent.setStereoCategory(firstParent.getStereoCategory());
    	testParent.setStereoComment(firstParent.getStereoComment());
    	testParent.setCorpName(secondParent.getCorpName());
    	String json = testParent.toJson();
    	logger.debug(json);
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/parents/validateParent")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isBadRequest())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ParentDTO> dupeParents = ParentDTO.fromJsonArrayToParentDTO(responseJson);
    	for (ParentDTO parent : dupeParents){
    		Assert.assertNotNull(parent.getCorpName());
    		Assert.assertNotNull(parent.getStereoCategory());
    		Assert.assertNotNull(parent.getStereoComment());
    	}
    }
    
 //   @Test
	@Transactional
	public void validateAndUpdateParent_changeStereoCategoryAndStructure() throws Exception{
		List<Parent> parents = Parent.findParentEntries(0, 2);
    	Parent firstParent = parents.get(0);
    	Parent secondParent = parents.get(1);
    	Parent testParent = new Parent();
    	testParent.setMolStructure(secondParent.getMolStructure());
    	if (firstParent.getStereoCategory().getCode().equals("unknown")){
    		testParent.setStereoCategory(StereoCategory.findStereoCategorysByCodeEquals("achiral").getSingleResult());
    	}else{
    		testParent.setStereoCategory(StereoCategory.findStereoCategorysByCodeEquals("unknown").getSingleResult());
    	}
    	testParent.setId(firstParent.getId());
    	testParent.setStereoComment(firstParent.getStereoComment());
    	testParent.setCorpName(firstParent.getCorpName());
    	testParent.setChemist(firstParent.getChemist());
    	testParent.setCommonName(firstParent.getCommonName());
    	testParent.setIgnore(firstParent.getIgnore());
    	testParent.setParentNumber(firstParent.getParentNumber());
    	testParent.setRegistrationDate(firstParent.getRegistrationDate());
    	testParent.setCompoundType(firstParent.getCompoundType());
    	testParent.setParentAnnotation(firstParent.getParentAnnotation());
    	String json = testParent.toJson();
    	logger.debug(json);
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/parents/validateParent")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<CodeTableDTO> affectedLots = CodeTableDTO.fromJsonArrayToCoes(responseJson);
    	for (CodeTableDTO lot : affectedLots){
    		Assert.assertNotNull(lot.getCode());
    		Assert.assertNotNull(lot.getName());
    		Assert.assertNotNull(lot.getComments());
    	}
    	
    	String updateJson = testParent.toJson();
    	logger.debug(updateJson);
    	MockHttpServletResponse updateResponse = this.mockMvc.perform(post("/api/v1/parents/updateParent")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(updateJson)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String updateResponseJson = response.getContentAsString();
    	logger.info(updateResponseJson);
    	Collection<CodeTableDTO> affectedLotsFromUpdate = CodeTableDTO.fromJsonArrayToCoes(updateResponseJson);
    	for (CodeTableDTO lot : affectedLotsFromUpdate){
    		Assert.assertNotNull(lot.getCode());
    		Assert.assertNotNull(lot.getName());
    		Assert.assertNotNull(lot.getComments());
    	}

	}

    @Test
    @Transactional
    public void updateParentMetaSingle() throws Exception {
    	
		ParentEditDTO parentDTO = new ParentEditDTO();
		parentDTO.setCorpName("CMPD-0000001");
		parentDTO.setComment("parent comment update");
		parentDTO.setChemistCode("cchemist");
		parentDTO.setCommonNameAliases("apple;banana-pepper;pear");
		String json = parentDTO.toJson();

    	logger.debug(json);
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/parents/updateParent/metadata")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);

    }
    
 //   @Test
    @Transactional
    public void updateParentMetaArray() throws Exception {
    	
    	Collection<ParentEditDTO> parentDTOCollection = new HashSet<ParentEditDTO>();
		ParentEditDTO parentDTO = new ParentEditDTO();
		parentDTO.setCorpName("CMPD-0000001");
//		parentDTO.setCorpName("CAS690");
		parentDTO.setComment("parent comment update");
		//parentDTO.setChemistCode("cchemist");
		parentDTO.setCommonNameAliases("apple;banana-pepper;pear");
		parentDTOCollection.add(parentDTO);
		String json = ParentEditDTO.toJsonArray(parentDTOCollection);

    	logger.debug(json);
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/parents/updateParent/metadata/jsonArray")
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